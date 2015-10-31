**Rule-Based Dialog-Driven Legal Document Assembly**

Author: Marko Markovic

Aim of this software project is to demonstrate legal document assembly, using interactive fact collecting, based on legal rules.
It is developed as a part of PhD studies on the Faculty of Technical Science, University of Novi Sad,
 and to present an idea which should be a starting point of PhD thesis.

It is Eclipse project, written in the Java programming language and it uses [tuProlog](http://apice.unibo.it/xwiki/bin/view/Tuprolog/) library.
As a source for Prolog rules, regulations in the Republic of Serbia were considered (The Criminal Procedure Code, The Criminal Code, The Law on Road Traffic Safety).

The project structure is as follows:<br/>
/<br/>
&nbsp;&nbsp;&nbsp;&nbsp;motion_to_indict.xml - document template <br/>
&nbsp;&nbsp;&nbsp;&nbsp;readme.txt - this file <br/>
&nbsp;&nbsp;&nbsp;&nbsp;traffic_accident.pl - Prolog rules <br/>
/src/org/draftingbyrules <br/>
&nbsp;&nbsp;&nbsp;&nbsp;Main.java - contains main method and initiates all document assembling phases <br/> 
/src/org/draftingbyrules/bean <br/>
&nbsp;&nbsp;&nbsp;&nbsp;ArgumentNode.java - representation of a node on argument graph <br/>
&nbsp;&nbsp;&nbsp;&nbsp;Fact.java - fact name and fact value for both, prolog rules and document template fields <br/>
/src/org/draftingbyrules/utils <br/>
&nbsp;&nbsp;&nbsp;&nbsp;AkomaNtosoUtil.java - creates [Akoma Ntoso](http://www.akomantoso.org/) document, based on existing template <br/>
&nbsp;&nbsp;&nbsp;&nbsp;CafUtil.java - creates argument graph in CAF ([Carneades](http://carneades.github.io/) argument format) <br/>
&nbsp;&nbsp;&nbsp;&nbsp;UiUtil.java - enables collecting data, through console, entered by user <br/>
/lib <br/>
&nbsp;&nbsp;&nbsp;&nbsp;tuprolog.jar - tuProlog library <br/>


Parameters necessary for proper program execution are stored in Main.java and corresponds to current configuration.
When started, program gathers relevant facts through console. For Prolog rules, questions about fulfillment of predicates and variable values are being asked.
Also, for document template, values needed for document content are being asked.
After answering on all questions, the argument graph and the motion to indict are generated.

Questions on Prolog facts are being asked both, on fulfillment and on variable values.
Question on fulfillment is yes-no question where user answers with letters 'y' and 'n' (assuming 'y' as default value).
Questions on variable values also offers default value. It is the first value, for variable with the same name, already given by user.

For example, unsafe distance as the traffic rules violation, would be determined by following question:
```
    unsafe_distance(Defendant) [Y/n]: 
```
After answering (with 'y', 'n' or only &lt;enter&gt;) variable value should be entered:
```
    Defendant: 
```
If, for instance, value 'john' is entered, existence of defendant's negligence would be determined by:
```
    negligence(Defendant) [Y/n]: 
```
while default value for variable is offered:
```
    Defendant [john]: 
```
