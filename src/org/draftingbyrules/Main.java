package org.draftingbyrules;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.draftingbyrules.bean.ArgumentNode;
import org.draftingbyrules.bean.Fact;
import org.draftingbyrules.utils.AkomaNtosoUtil;
import org.draftingbyrules.utils.CafUtil;
import org.draftingbyrules.utils.UiUtil;

import alice.tuprolog.ExecutionContext;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import alice.tuprolog.event.SpyEvent;
import alice.tuprolog.event.SpyListener;

public class Main {

	private static String defaultPrologFilename = "./traffic_accident.pl";
	private static String aknFilename = "motion_to_indict.xml";
	private static String cafFilename = "argument_graph.xml";

	private static List<String> definedPredicates = new ArrayList<String>();
	private static List<String> undefinedPredicates = new ArrayList<String>();
	private static List<Term> undefinedTerms = new ArrayList<Term>();
	
	private static String prologQuery = "committed(X,art289par1).";

	private static ArrayList<Fact> facts = new ArrayList<Fact>();
	private static ArrayList<ArgumentNode> argumentNodes = new ArrayList<ArgumentNode>();
	private static ArgumentNode parent = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String prologFile = args.length>0 ? args[1] : defaultPrologFilename;

		Prolog engine = new Prolog();
		try {
			engine.setTheory(new Theory(new FileInputStream(prologFile)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator iter = engine.getTheory().iterator(engine);
		while (iter.hasNext()) {
			Term term = (Term)iter.next();
			examineDefinedPredicates(term);
		}
		iter = engine.getTheory().iterator(engine);
		while (iter.hasNext()) {
			Term term = (Term)iter.next();
			examineUndefinedPredicates(term);
		}
		String result = "";
		String value = "";
		for (Term term: undefinedTerms) {
			Struct struct = (Struct)term;
			String values = "";
			result = UiUtil.readValue(struct.toString() + " [Y/n]: ");
			result = ("".equals(result) || "y".equalsIgnoreCase(result)) ? "true" : "false"; // Y (true) is default value
			for (int i=0; i<struct.getArity(); i++) {
				Term arg = struct.getArg(i);
				if (arg instanceof Var) {
					String varName = ((Var) arg).getName();
					String defaultValue = getFactValue(varName);
					value = UiUtil.readValue(varName + (defaultValue!=null?(" [" + defaultValue + "]"):"") + ": ");
					value = (defaultValue != null && value.length() == 0) ? defaultValue : value;  // if nothing entered, first entered value is taken
					facts.add(new Fact(varName, value));
				} else {
					value = arg.toString();
				}
				values += (values.length()>0 ? "," : "") + value;
			}
			try {
				// System.out.println(struct.getName() + "(" + values + ") :- " + result + ".");
				engine.addTheory(new Theory(struct.getName() + "(" + values + ") :- " + result + "."));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			engine.addSpyListener(new SpyListener() {
				public void onSpy(SpyEvent spyEvent) {
					List<ExecutionContext> eStack = spyEvent.getSnapshot().getExecutionStack();
					if ("Eval".equals(spyEvent.getSnapshot().getNextStateName())) {
						ExecutionContext ec = eStack.get(0);
						String name = ec.getCurrentGoal().getName();
						int depth = ec.getDepth();
						if (ec.getCurrentGoal().getPrimitive() == null &&
								!">".equals(name) && !"<".equals(name))
							if (parent == null || depth != parent.getDepth()) {
								ArgumentNode node = new ArgumentNode();
								node.setText(name);
								node.setDepth(depth);
								for (int i=argumentNodes.size()-1; i>=0; i--)
									if (argumentNodes.get(i).getDepth()+1 == depth && node.getParent() == null)
										node.setParent(argumentNodes.get(i));
								argumentNodes.add(node);
								parent = node;
							}
					} else if ("Back".equals(spyEvent.getSnapshot().getNextStateName())) {
						parent = parent.getParent();
						argumentNodes.remove(argumentNodes.size()-1);
					}
				}
			});
			engine.setSpy(true);
			
			SolveInfo info = engine.solve(prologQuery);
			if (info.isSuccess()) {
				Term solution = info.getSolution();
				CafUtil.createCaf(argumentNodes, cafFilename);
				System.out.println("The argument graph (" + cafFilename + ") successfully generated.");
			} else {
				System.out.println("For given facts the Prolog query hasn't solution.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		AkomaNtosoUtil.updateIndictment(aknFilename, facts);
		System.out.println("The motion to indict (" + aknFilename + ") successfully generated.");
	}
	
	private static void examineDefinedPredicates(Term term) {
		if (term instanceof Struct) {
			Struct struct = (Struct)term;
			if (struct.getName().equals(":-")) {
				definedPredicates.add( ((Struct)struct.getArg(0)).getName() );
			} else {
				definedPredicates.add( struct.getName() );
			}
		}
	}

	private static void examineUndefinedPredicates(Term term) {
		if (term instanceof Struct) {
			Struct struct = (Struct)term;
			int startIndex = 0;
			if (struct.getName().equals(":-"))
				startIndex = 1;
			for (int i=startIndex; i<struct.getArity(); i++) {
				Term argTerm = struct.getArg(i);
				if (argTerm instanceof Struct) {
					Struct argStruct = (Struct)argTerm;
					if (!isInList(definedPredicates, argStruct.getName()) &&
							!isInList(undefinedPredicates, argStruct.toString()) &&
							!isBuiltinKeyword(argStruct.getName()) &&
							!argStruct.isAtom()) {
						undefinedPredicates.add( argStruct.toString() );
						undefinedTerms.add(argStruct);
					}
					examineUndefinedPredicates(argStruct);
				}
			}
		}
	}

	private static boolean isBuiltinKeyword(String keyword) {
		if (keyword.equals("true") ||
				keyword.equals("false") ||
				keyword.equals("<") ||
				keyword.equals("=<") ||
				keyword.equals(">") ||
				keyword.equals("=>") ||
				keyword.equals("once") ||
				keyword.equals(",") ||
				keyword.equals(";"))
			return true;
		return false;
	}
	private static boolean isInList(List<String> list, String string) {
		for (String item: list)
			if (string.equals(item))
				return true;
		return false;
	}

	private static String getFactValue(String factName) {
		for (Fact f: facts)
			if (f.getFactName().equals(factName))
				return f.getFactValue();
		return null;
	}

}
