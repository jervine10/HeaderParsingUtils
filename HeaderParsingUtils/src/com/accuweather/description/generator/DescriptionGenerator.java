package com.accuweather.description.generator;

import java.util.Collections;
import java.util.List;

public class DescriptionGenerator {

	public String generateDescription(List<Property> properties, String classname) {
		String description = buildDescription(classname, properties);
		
		return description;
	}
	
	private String buildDescription(String classname, List<Property> properties) {
		// Sort the properties
		Collections.sort(properties);
		
		String description = "- (NSString *)description {\n";
		description += "\treturn [NSString stringWithFormat:@\"<" + classname + ":";
		
		// Build the actual string portion of the description method
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case PRIMITIVE:
					String primitivePart = " " + property.getPropertyName() + ": " 
							+ property.getPrimitiveType().getFormatSpecifier() + ";";
					description += primitivePart;
					break;
				case OBJECT:
					String objectPart = " " + property.getPropertyName() + ": %@;";
					description += objectPart;
					break;
			}
		}
		
		description += ">\",";
		// now build the parameters portion
		for (Property property : properties) {
			switch(property.getPropertyType()) {
				case PRIMITIVE:
					String part = " self." + property.getPropertyName() + ",";
					description += part;
					break;
				case OBJECT:
					String objectPart = " self." + property.getPropertyName() + ",";
					description += objectPart;
					break;
			}
		}
		
		// remove trailing comma;
		description = description.substring(0, description.lastIndexOf(','));
		
		description += "];\n";
		description += "}";
		
		return description;
	}
}
