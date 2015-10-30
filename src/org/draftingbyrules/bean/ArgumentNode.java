package org.draftingbyrules.bean;

public class ArgumentNode {

	private String text;
	private int depth;
	private ArgumentNode parent;

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public ArgumentNode getParent() {
		return parent;
	}
	public void setParent(ArgumentNode parent) {
		this.parent = parent;
	}
	
}
