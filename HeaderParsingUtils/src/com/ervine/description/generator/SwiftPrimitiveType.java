package com.ervine.description.generator;

public enum SwiftPrimitiveType {

	Int("Int"),
	UInt("UInt"),
	Bool("Bool"),
	Long("Long");
	
	private String name;
	
	private SwiftPrimitiveType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
