package com.ervine.description.generator;

public enum PrimitiveType {
	
	// For a list of format specifiers go here:
	// http://developer.apple.com/library/ios/#documentation/cocoa/conceptual/Strings/Articles/formatSpecifiers.html
	
	NSINTEGER("NSInteger", "%d", SwiftPrimitiveType.Int),
	NSUINTEGER("NSUInteger", "%d", SwiftPrimitiveType.UInt),
	INT("int", "%d", SwiftPrimitiveType.Int),
	BOOL("BOOL", "%d", SwiftPrimitiveType.Bool),
	LONG("long", "%ld", SwiftPrimitiveType.Long),
	ENUM("", "%d", null);
	
	private String name;
	private String formatSpecifier;
	private SwiftPrimitiveType swiftPrimitiveType;

	private PrimitiveType(String name, String formatSpecifier, SwiftPrimitiveType swiftPrimitiveType) {
		this.name = name;
		this.formatSpecifier = formatSpecifier;
		this.swiftPrimitiveType = swiftPrimitiveType;
	}
	
	public String getName() {
		return name;
	}

	public String getFormatSpecifier() {
		return formatSpecifier;
	}
	
	public SwiftPrimitiveType getSwiftPrimitiveType() {
		return swiftPrimitiveType;
	}

}
