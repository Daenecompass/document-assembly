package org.draftingbyrules.bean;

public class Fact {
	
	private String factName;
	private String factValue;

	public String getFactName() {
		return factName;
	}
	public Fact(String factName, String factValue) {
		this.factName = factName;
		this.factValue = factValue;
	}
	
	public void setFactName(String factName) {
		this.factName = factName;
	}
	public String getFactValue() {
		return factValue;
	}
	public void setFactValue(String factValue) {
		this.factValue = factValue;
	}
	
}
