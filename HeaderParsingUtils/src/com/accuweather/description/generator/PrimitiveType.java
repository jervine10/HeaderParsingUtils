package com.accuweather.description.generator;

public enum PrimitiveType {
	
	// For a list of format specifiers go here:
	// http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/Strings/Articles/formatSpecifiers.html
	
	NSINTEGER("NSInteger", "%d"),
	NSUINTEGER("NSUInteger", "%d"),
	INT("int", "%d"),
	BOOL("BOOL", "%d"),
	LONG("long", "%ld"),
	ENUM("", "%d");
	
	private String name;
	private String formatSpecifier;

	private PrimitiveType(String name, String formatSpecifier) {
		this.name = name;
		this.formatSpecifier = formatSpecifier;
	}
	
	public String getName() {
		return name;
	}

	public String getFormatSpecifier() {
		return formatSpecifier;
	}

}
