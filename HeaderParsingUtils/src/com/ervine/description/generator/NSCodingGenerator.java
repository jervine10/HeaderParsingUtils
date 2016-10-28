package com.ervine.description.generator;

import java.util.List;

public class NSCodingGenerator {

	private String CONSTANT_PREFIX = "";
	
	public String generatorNSCodingMethods(List<Property> properties, String classname) {
		
		CONSTANT_PREFIX += classname;
		
		String constantPart = constants(properties);
		String encodePart = encode(properties);
		String decodePart = decode(properties);
		
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
		String nsCoding = "- (void)encodeWithCoder:(NSCoder *)aCoder {\n" +
				"%s" +
		"}";
		
		String implementation = "";
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case OBJECT:
					implementation += "\t[aCoder encodeObject:self." + property.getPropertyName() +
							" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
					break;
				case PRIMITIVE:
					switch(property.getPrimitiveType()) {
						case BOOL:
							implementation += "\t[aCoder encodeBool:self." + property.getPropertyName() +
									" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case ENUM:
							implementation += "\t[aCoder encodeInteger:self." + property.getPropertyName() +
									" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case INT:
							implementation += "\t[aCoder encodeInteger:self." + property.getPropertyName() +
									" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case LONG:
							// TODO is int64 right here?
							implementation += "\t[aCoder encodeInt64:self." + property.getPropertyName() +
									" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case NSINTEGER:
							implementation += "\t[aCoder encodeInteger:self." + property.getPropertyName() +
									" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case NSUINTEGER:
							implementation += "\t[aCoder encodeInteger:self." + property.getPropertyName() +
									" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case CGFLOAT:
							implementation += "\t[aCoder encodeFloat:self." + property.getPropertyName() +
							" forKey:" + CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
					}
					break;
			}
		}
		
		return String.format(nsCoding, implementation);
	}
	
	private String decode(List<Property> properties) {
		String nsCoding = "- (instancetype)initWithCoder:(NSCoder *)aDecoder {\n" +
				"\tif (self = [super init]) {\n" +
					"%s" +
				"\t}\n" +
				"\nreturn self;\n" +
		"}";
		
		
		String implementation = "";
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case OBJECT:
					implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeObjectForKey:"
							+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
					break;
				case PRIMITIVE:
					switch(property.getPrimitiveType()) {
						case LONG:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeInt64ForKey:" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case BOOL:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeBoolForKey:" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case ENUM:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeIntegerForKey:"
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case INT:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeIntegerForKey:" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case NSINTEGER:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeIntegerForKey:"
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case NSUINTEGER:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeIntegerForKey:" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
						case CGFLOAT:
							implementation += "\t\tself." + property.getPropertyName() + " = [aDecoder decodeFloatForKey:" 
									+ CONSTANT_PREFIX + property.getCapitalName() + "];\n"; 
							break;
					}
					break;
			}
		}
		
		
		return String.format(nsCoding, implementation);
	}
	
}
