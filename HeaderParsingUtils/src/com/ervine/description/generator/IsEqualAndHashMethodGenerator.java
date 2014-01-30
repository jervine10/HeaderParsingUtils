package com.ervine.description.generator;

import java.util.List;

public class IsEqualAndHashMethodGenerator {

	public String generateIsEqualMethod(List<Property> properties) {
		String isEqual = isEqual(properties);
		String hash = hash(properties);
		
		return isEqual + "\n\n" + hash;
	}

	private String isEqual(List<Property> properties) {
		String isEqual = "- (BOOL)isEqual:(id)object {\n" +
				"\tif (object == self)\n" +
				"\t\treturn YES;\n" +
				"\tif (!object || ![object isKindOfClass:[self class]])\n" +
				"\t\treturn NO;\n" +
				"%s" +
				"return YES;\n" +
		"}";
		
		String implementation = "";
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case OBJECT:
					implementation += "\tif((self." + property.getPropertyName() + " || [object " + property.getPropertyName() + "]) && " + 
							"![self." + property.getPropertyName() + " isEqual:[object " + property.getPropertyName() + "]])\n" +
								"\t\treturn NO;\n";;
					break;
				case PRIMITIVE:
					switch(property.getPrimitiveType()) {
					case BOOL:
						implementation += "\tif(self." + property.getPropertyName() + " != [object is" + property.getCapitalName() + "])\n" +
								"\t\treturn NO;\n"; 
						break;
					case ENUM:
					case INT:
					case LONG:
					case NSINTEGER:
					case NSUINTEGER:
						implementation += "\tif(self." + property.getPropertyName() + " != [object " + property.getPropertyName() + "])\n" +
								"\t\treturn NO;\n"; 
						break;
				}
					break;
			}
		}
		
		return String.format(isEqual, implementation);
	}
	
	private String hash(List<Property> properties) {
		String hash = "- (NSUInteger)hash {\n" +
				"\tNSUInteger prime = 31;\n" +
				"\tNSUInteger result = 1;\n" +
				"%s"+
				"return result;\n" +
		"}";
		
		String implementation = "";
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case OBJECT:
					implementation += "\tresult = prime * result + (!self." + property.getPropertyName() 
						+ " ? 0 : [self." + property.getPropertyName() + " hash]);\n";
					break;
				case PRIMITIVE:
					switch(property.getPrimitiveType()) {
					case BOOL:
						implementation += "\tresult = prime * result + (!self." + property.getPropertyName() + " ? 1231 : 1237);\n";
						break;
					case ENUM:
					case INT:
					case LONG:
					case NSINTEGER:
					case NSUINTEGER:
						implementation += "\tresult = prime * result + self." + property.getPropertyName() + ";\n"; 
						break;
				}
					break;
			}
		}
		
		return String.format(hash, implementation);
	}
	
}
