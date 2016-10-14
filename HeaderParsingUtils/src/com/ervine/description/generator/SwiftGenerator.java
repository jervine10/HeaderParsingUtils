package com.ervine.description.generator;

import java.util.List;

public class SwiftGenerator {
	
	private String CONSTANT_PREFIX = "";

	public String generateSwiftClass(List<Property> properties, String className) {
		CONSTANT_PREFIX = className;
		
		String swiftClass = "//  Copyright © AccuWeather. All rights reserved.\n\nimport UIKit\n\n";
		
		swiftClass += constants(properties, className);
		swiftClass += classDeclaration(className);
		swiftClass += propertyDefinitions(properties);
		swiftClass += hashValue(properties);
		swiftClass += overrideInit();
		swiftClass += decode(properties);
		swiftClass += encode(properties);
		swiftClass += "\n}\n";
		swiftClass += equals(properties, className);
		
		return swiftClass + "\n";
	}
	
	private String constants(List<Property> properties, String className) {
		String constants = "";
		
		// private let name = ""
		for(Property property : properties) {
			String propertyName = property.getPropertyName();
			constants += "private let " + CONSTANT_PREFIX + property.getCapitalName() + " = \"" + propertyName + "\"\n";
		}
		constants += "\n";
		
		return constants;
	}
	
	private String classDeclaration(String className) {
		String string = "class " + className + ": NSObject, NSCoding {\n\n";
		
		return string;
	}
	
	private String overrideInit() {
		return "\toverride init() {\n\n\t}\n\n";
	}
	
	private String propertyDefinitions(List<Property> properties) {
		String implementation = "";
		
		for (Property property : properties) {
			switch (property.getPropertyType()) {
				case OBJECT:
					ClassType classType = property.getClassType();
					String swiftName = getSwiftClassName(classType.getClassName());

					if (classType.isArray()) {
						implementation += "\tvar " + property.getPropertyName() + ": [" + swiftName + "]" + (property.getNullable() ? "?" : "") + "\n";
					} else {
						implementation += "\tvar " + property.getPropertyName() + ": " + swiftName + (property.getNullable() ? "?" : "") + "\n";
					}
					break;
				case PRIMITIVE:
					if (property.getPrimitiveType() != PrimitiveType.ENUM) {
						String defaultValue = "0";
						if (property.getPrimitiveType() == PrimitiveType.BOOL) {
							defaultValue = "false";
						}
						implementation += "\tvar " + property.getPropertyName() + ": " + property.getPrimitiveType().getSwiftPrimitiveType().getName() + (property.getNullable() ? "?" : " = " + defaultValue) + "\n";
					} else {
						implementation += "\tvar " + property.getPropertyName() + ": " + property.getEnumName() + (property.getNullable() ? "?" : "") + "\n";
					}
					break;
			}
		}
		implementation += "\n";
		
		return implementation;
	}
	
	private String hashValue(List<Property> properties) {
		String hashValue = "\toverride var hashValue: Int {\n" +
				"\t\tlet prime = 31\n" +
				"\t\tvar result = 1\n" +
				"%s"+
				"\t\treturn result\n" +
		"\t}\n\n";
		
		String implementation = "";
		for (Property property : properties) {
			String nullabilitySpecifier = property.getNullable() ? "?" : "";
			
			if (property.getPropertyType() == PropertyType.PRIMITIVE && property.getPrimitiveType() == PrimitiveType.BOOL) {
				implementation += "\t\tresult = prime * result + (!" + property.getPropertyName() + " ? 1231 : 1237)\n";
			} else {
				String descriptionText = "";
				if (property.getClassType() != null && property.getClassType().isArray()) {
					descriptionText = ".description";
				}
				
				String rawValue = "";
				if (property.getPropertyType() == PropertyType.PRIMITIVE && property.getPrimitiveType() == PrimitiveType.ENUM) {
					rawValue = ".rawValue";
				}
				
				if (property.getNullable()) {
					implementation += "\t\tresult = prime * result + (" + property.getPropertyName() + rawValue +  nullabilitySpecifier + descriptionText + ".hashValue ?? 0)\n";
				} else {
					implementation += "\t\tresult = prime * result + " + property.getPropertyName() + rawValue + descriptionText + "\n";
				}
			}
		}
		
		return String.format(hashValue, implementation);
	}
	
	private String encode(List<Property> properties) {
		String nsCoding = "\tfunc encode(with aCoder: NSCoder) {\n" +
				"%s" +
		"\t}\n";
		
		String implementation = "";
		
		String optionalFormat = "\t\tif %s != nil {\n" +
				"\t%s" +
		"\t\t}\n";
		
		for (Property property : properties) {
			String code = "";
			if (property.getPrimitiveType() != null && property.getPrimitiveType() == PrimitiveType.ENUM) {
				if (property.getNullable()) {
					code = "\t\tif let " + property.getPropertyName() + "RawValue = " + property.getPropertyName() + " {\n"
							+ "\t%s\n"
					+ "\t\t}\n";
					code = String.format(code, "\t\taCoder.encode(" + property.getPropertyName() + "RawValue, forKey:" + CONSTANT_PREFIX + property.getCapitalName() + ")");
				} else {
					code = "\t\taCoder.encode(" + property.getPropertyName() + ".rawValue, forKey:" + CONSTANT_PREFIX + property.getCapitalName() + ")\n";
				}
			} else {
				String nullability = "";
				if (property.getNullable()) {
					nullability = "!";
				}
				code = "\t\taCoder.encode(" + property.getPropertyName() + nullability + ", forKey:" + CONSTANT_PREFIX + property.getCapitalName() + ")\n";
			}

			if (property.getPrimitiveType() != null && property.getPrimitiveType() == PrimitiveType.ENUM) {
				implementation += code;
			} else if (property.getNullable()) {
				implementation += String.format(optionalFormat, property.getPropertyName(), code);
			} else {
				implementation += code;
			}
		}
		
		return String.format(nsCoding, implementation);
	}
	
	private String decode(List<Property> properties) {
		String nsCoding = "\trequired init?(coder aDecoder: NSCoder) {\n" +
				"%s" +
		"\t}\n\n";
		
		String implementation = "";
		
		for (Property property : properties) {
			String nullablePart = "\t\t%s";
			if (property.getNullable()) {
				nullablePart = "\t\tif aDecoder.containsValue(forKey: " + CONSTANT_PREFIX + property.getCapitalName() + ") {\n" + "\t\t\t%s\t\t}\n";
			}
			
			String decodingPart = "";
			switch(property.getPropertyType()) {
				case OBJECT:
					String className = getSwiftClassName(property.getClassType().getClassName());
					if (property.getClassType().isArray()) {
						className  = "[" + className + "]";
					}
					
					decodingPart = property.getPropertyName() + " = aDecoder.decodeObject(forKey: " + CONSTANT_PREFIX + property.getCapitalName() + ") as? " + className + "\n";
					
					implementation += String.format(nullablePart, decodingPart);
					break;
				case PRIMITIVE:
					switch(property.getPrimitiveType()) {
						case BOOL:
							decodingPart = property.getPropertyName() + " = aDecoder.decodeBool(forKey: "
									+ CONSTANT_PREFIX + property.getCapitalName() + ")\n";
							
							implementation += String.format(nullablePart, decodingPart);
							break;
						case ENUM:
							String primitiveName = "";
							if (property.getPrimitiveType() != PrimitiveType.ENUM) {
								primitiveName = property.getPrimitiveType().getSwiftPrimitiveType().getName();
							}
							
							primitiveName = property.getEnumName();
							decodingPart = "let " + property.getPropertyName() + "RawValue = aDecoder.decodeInteger(forKey: " + CONSTANT_PREFIX + property.getCapitalName() + ") \n" 
									+ "\t\t" + property.getPropertyName() + " = " + primitiveName + "(rawValue: " + property.getPropertyName() + "RawValue)!\n";
							
							implementation += String.format(nullablePart, decodingPart);
							break;
						case LONG:
						case INT:
						case NSINTEGER:
						case NSUINTEGER:
							decodingPart = property.getPropertyName() + " = aDecoder.decodeInteger(forKey: "
									+ CONSTANT_PREFIX + property.getCapitalName() + ")\n";
							
							implementation += String.format(nullablePart, decodingPart);
							break;
					}
					break;
			}
		}
		
		return String.format(nsCoding, implementation);
	}
	
	private String equals(List<Property> properties, String className) {
		String equals = "\nfunc == (lhs: " + className + ", rhs: " + className + ") -> Bool {\n"
				+ "%s"
		+ "}\n";
		
		String implementation = "";
		
		// private let name = ""
		for(Property property : properties) {
			String nullablePart = "";
			if (property.getNullable()) {
				nullablePart = "\tif lhs." + property.getPropertyName() + " == nil && rhs." + property.getPropertyName() + " != nil {\n\t\treturn false\n\t} else %s\n";
			}
			
			String nullablitySpecifier = "";
			if (property.getClassType() != null && property.getClassType().isArray()) {
				nullablitySpecifier = "!";
			}
			
			String code = "";
			code = "if " + "lhs." + property.getPropertyName() + nullablitySpecifier + " != rhs." + property.getPropertyName() + nullablitySpecifier + " {\n\t\treturn false\n\t}" ;
			code = String.format(nullablePart, code);
			
			implementation += code;
		}
		implementation += "\treturn true\n";
		
		return String.format(equals, implementation);
	}
	
	private String getSwiftClassName(String name) {
		String swiftName = name;
			
		if (name.equals("NSString")) {
			swiftName = "String";
		} else if (name.equals("NSDate")) {
			swiftName = "Date";
		} else if (name.equals("NSCalendar")) {
			swiftName = "Calendar";
		} else if (name.equals("NSTimeZone")) {
			swiftName = "TimeZone";
		} else if (name.equals("NSURL")) {
			swiftName = "URL";
		}
		
		return swiftName;
	}
	
}
