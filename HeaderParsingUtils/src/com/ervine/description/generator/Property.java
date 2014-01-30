package com.ervine.description.generator;


public class Property implements Comparable<Property> {

	private String propertyName;
	private String capitalName;
	private PropertyType propertyType;
	private PrimitiveType primitiveType;
	
	public Property(String propertyName, PropertyType type) {
		this.propertyName = propertyName;
		this.propertyType = type;
		
		setCapitalName(propertyName);
	}
	
	public Property(String propertyName, PropertyType type, PrimitiveType primitiveType) {
		this(propertyName, type);
		
		this.primitiveType = primitiveType;
	}
	
	public PropertyType getPropertyType() {
		return propertyType;
	}
	
	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
	}

	@Override
	public String toString() {
		return "Property [propertyName=" + propertyName + ", propertyType="
				+ propertyType + ", primitiveType=" + primitiveType + "]";
	}

	@Override
	public int compareTo(Property o) {
		Integer otherOrdinal = o.propertyType.ordinal();
		Integer thisOrdinal = this.propertyType.ordinal();
		return thisOrdinal.compareTo(otherOrdinal);
	}

	public String getCapitalName() {
		return capitalName;
	}

	private void setCapitalName(String constantName) {
		this.capitalName = capitalize(propertyName);
	}
	
	private String capitalize(String string) {
		String firstLetter = string.substring(0, 1);
		String capitalized = firstLetter.toUpperCase() + string.substring(1, string.length());
		
		return capitalized;
	}
	
}
