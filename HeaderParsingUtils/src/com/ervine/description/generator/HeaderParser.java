package com.ervine.description.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeaderParser {

	private List<PrimitiveType> primitives = Arrays.asList(PrimitiveType.values());
	
	public String extractClassname(List<String> lines) {
		// Extract the classname;
		String classname = "";
		for (String line : lines) {
			if (line.contains("@interface")) {
				classname = line.substring(line.indexOf("@interface") 
						+ "@interface".length(), line.indexOf(':'));
				
				classname = stripLeadingWhiteSpace(classname.trim());
				
				break;	
			}
		}
		
		return classname;
	}
	
	public List<Property> extractProperties(List<String> lines) {
		
		List<Property> properties = new ArrayList<Property>();
		for (String line : lines) {
			if (line.contains("@property")) {
				// we're interested in this line
				String afterRightBrace = line.substring(line.indexOf(')') + 1);
				afterRightBrace = stripLeadingWhiteSpace(afterRightBrace);
				
				Property property = getPropertyFromLine(afterRightBrace);
				if (property != null) {
					properties.add(property);
				}
			}
		}
		
		return properties;
		
	}
	
	private String stripLeadingWhiteSpace(String line) {
		while (line.indexOf(" ") == 0) {
			// trim leading whitespace
			line = line.substring(1, line.length());
		}
		
		return line;
	}

	private Property getPropertyFromLine(String line) {
		for (PrimitiveType primitive : primitives) {
			if (primitive != PrimitiveType.ENUM && line.startsWith(primitive.getName())) {
				String propertyName = stripPrimitivePropertyFromLine(primitive.getName(), line);
				Property property = new Property(propertyName, PropertyType.PRIMITIVE, primitive);
				return property;
			} else if (primitive == PrimitiveType.ENUM && !line.contains("*")) {
				// make sure the line doesn't contain an id keyword
				if (!line.contains("id ") && !line.contains("id<")) {
					// No star, assuming it's an enum
					String propertyName = stripEnumPropertyFromLine(line);
					Property property = new Property(propertyName, PropertyType.PRIMITIVE, PrimitiveType.ENUM);
					return property;
				}
			}
		}
		
		// must have been an object
		String propertyName = stripObjectPropertyFromLine(line);
		Property property = new Property(propertyName, PropertyType.OBJECT);
		
		return property;
	}
	
	private String stripObjectPropertyFromLine(String line) {
		String property = "";
		if (line.contains("*")) {
			property = line.substring(line.indexOf('*') + 1, line.indexOf(';'));	
		} else if (line.contains("id ")) {
			property = line.substring(line.indexOf("id ") + 3, line.indexOf(';'));
		} else if (line.contains("id<")) {
			property = line.substring(line.indexOf('>') + 1, line.indexOf(';'));
		}
		property = stripLeadingWhiteSpace(property);
		
		return property;
	}

	private String stripPrimitivePropertyFromLine(String primitive, String line) {
		String property = line.substring(line.indexOf(primitive) + primitive.length(), line.indexOf(';'));
		property = stripLeadingWhiteSpace(property);
		
		return property;
	}
	
	private String stripEnumPropertyFromLine(String line) {
		String[] segments = line.split(" ");
		String property = "";
		if (segments.length >= 2) {
			property = segments[1];
			// remove trailing semicolon
			property = property.substring(0, property.lastIndexOf(';'));
		}
		return property;
	}
	
}
