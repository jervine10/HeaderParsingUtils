package com.ervine.description.generator;


public class Property implements Comparable<Property> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((capitalName == null) ? 0 : capitalName.hashCode());
		result = prime * result
				+ ((classType == null) ? 0 : classType.hashCode());
		result = prime * result
				+ ((enumName == null) ? 0 : enumName.hashCode());
		result = prime * result
				+ ((nullable == null) ? 0 : nullable.hashCode());
		result = prime * result
				+ ((primitiveType == null) ? 0 : primitiveType.hashCode());
		result = prime * result
				+ ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result
				+ ((propertyType == null) ? 0 : propertyType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (capitalName == null) {
			if (other.capitalName != null)
				return false;
		} else if (!capitalName.equals(other.capitalName))
			return false;
		if (classType == null) {
			if (other.classType != null)
				return false;
		} else if (!classType.equals(other.classType))
			return false;
		if (enumName == null) {
			if (other.enumName != null)
				return false;
		} else if (!enumName.equals(other.enumName))
			return false;
		if (nullable == null) {
			if (other.nullable != null)
				return false;
		} else if (!nullable.equals(other.nullable))
			return false;
		if (primitiveType != other.primitiveType)
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (propertyType != other.propertyType)
			return false;
		return true;
	}

	private String propertyName;
	private String capitalName;
	private String enumName;
	private PropertyType propertyType;
	private ClassType classType;
	
	private PrimitiveType primitiveType;
	private Boolean nullable;
	
	
	public Property(String propertyName, ClassType classType, PropertyType type) {
		this.propertyName = propertyName;
		this.propertyType = type;
		this.classType = classType;
		
		setCapitalName(propertyName);
	}
	
	public Property(String propertyName, PropertyType type, PrimitiveType primitiveType) {
		this(propertyName, null, type);
		
		this.primitiveType = primitiveType;
	}
	
	public Property(String propertyName, String enumName, PropertyType type, PrimitiveType primitiveType) {
		this(propertyName, type, primitiveType);
		
		this.enumName = enumName;
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
	
	public ClassType getClassType() {
		return classType;
	}

	public void setClassType(ClassType classType) {
		this.classType = classType;
	}
	
	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public String getEnumName() {
		return enumName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	@Override
	public String toString() {
		return "Property [propertyName=" + propertyName + ", capitalName="
				+ capitalName + ", enumName=" + enumName + ", propertyType="
				+ propertyType + ", classType=" + classType
				+ ", primitiveType=" + primitiveType + ", nullable=" + nullable
				+ "]";
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
