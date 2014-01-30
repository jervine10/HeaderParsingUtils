package com.accuweather.description.generator;

import java.util.List;

public class NSDictionaryGenerator {

	private String CONSTANT_PREFIX = "";
	
	public String generateDictionaryMethods(List<Property> properties, String classname) {
		
		CONSTANT_PREFIX += classname;
		
		String constantPart = constants(properties);
		String encodePart = encode(properties);
		String decodePart = decode(properties, classname);
		
		return constantPart + "\n\n" + decodePart + "\n\n" + encodePart;
	}

	private String constants(List<Property> properties) {
		String constants = "";
		
		// const NSString *name = @"";
		for(Property property : properties) {
			String propertyName = property.getPropertyName();
			constants += "static NSString * const " + CONSTANT_PREFIX + property.getCapitalName() + " = @\"" + propertyName + "\";\n";
		}
		
		return constants;
	}
	
	private String encode(List<Property> properties) {
		String toDictionary = "- (NSDictionary *)dictionaryRepresentation {\n" +
				"return @{\n%s\n};\n" +
		"}";
		
		String implementation = "";
		for (Property property : properties) {
			switch(property.getPropertyType()) {
			case OBJECT:
				implementation += "\t" + CONSTANT_PREFIX + property.getCapitalName() +  " : [self." + property.getPropertyName() + " dictionaryRepresentation],\n"; 
				break;
			case PRIMITIVE:
				switch(property.getPrimitiveType()) {
				case BOOL:
				case ENUM:
				case INT:
				case LONG:
				case NSINTEGER:
				case NSUINTEGER:
				default:
					implementation += "\t" + CONSTANT_PREFIX + property.getCapitalName() +  " : @(self." + property.getPropertyName() + "),\n";
				}
				break;
			}
		}
		
		// remove trailing comma
		int index = implementation.lastIndexOf(',');
		if (index != -1) {
			implementation = implementation.substring(0, index);
		}
		
		return String.format(toDictionary, implementation);
	}
	
	private String decode(List<Property> properties, String classname) {
		String nsCoding = "+ (instancetype)fromDictionaryRepresentation:(NSDictionary *)dictionary {\n" +
				"\t" + classname + " *object = [[" + classname + " alloc] init];\n" +
					"%s" +
				"\t\n" +
				"\nreturn object;\n" +
		"}";
		
		String implementation = "";
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case OBJECT:
					implementation += "\tobject." + property.getPropertyName() + "= [" + classname + " fromDictionaryRepresentation:dictionary["
							+ CONSTANT_PREFIX + property.getCapitalName() + "]];\n"; 
					break;
				case PRIMITIVE:
					switch(property.getPrimitiveType()) {
						case LONG:
							// TODO is int64 right here?
							implementation += "\tobject." + property.getPropertyName() + " = [dictionary[" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "] longValue];\n"; 
							break;
						case BOOL:
							implementation += "\tobject." + property.getPropertyName() + " = [dictionary[" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "] boolValue];\n";
							break;
						case ENUM:
						case INT:
						case NSINTEGER:
							implementation += "\tobject." + property.getPropertyName() + " = [dictionary[" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "] integerValue];\n";
							break;
						case NSUINTEGER:
							implementation += "\tobject." + property.getPropertyName() + " = [dictionary[" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "] unsignedIntegerValue];\n";
							break;
					}
					break;
			}
		}
		
		
		return String.format(nsCoding, implementation);
	}
	
}

