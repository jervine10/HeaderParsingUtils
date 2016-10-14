package com.ervine.description.generator;

public class ClassType {

	private boolean isArray = false;
	private boolean isDictionary = false;
	
	private String className;
	private String dictionaryKeyType;
	private String dictionaryValueType;

	public ClassType(String classType) {
		super();
		this.isArray = false;
		this.isDictionary = false;
		this.className = classType;
	}

	public ClassType(boolean isArray, boolean isDictionary, String classType, String dictionaryKeyType, String dictionaryValueType) {
		super();
		this.isArray = isArray;
		this.isDictionary = isDictionary;
		this.className = classType;
		this.dictionaryKeyType = dictionaryKeyType;
		this.dictionaryValueType = dictionaryValueType;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public boolean isDictionary() {
		return isDictionary;
	}

	public void setDictionary(boolean isDictionary) {
		this.isDictionary = isDictionary;
	}
	
	public String getDictionaryKeyType() {
		return dictionaryKeyType;
	}

	public void setDictionaryKeyType(String dictionaryKeyType) {
		this.dictionaryKeyType = dictionaryKeyType;
	}

	public String getDictionaryValueType() {
		return dictionaryValueType;
	}

	public void setDictionaryValueType(String dictionaryValueType) {
		this.dictionaryValueType = dictionaryValueType;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "ClassType [isArray=" + isArray + ", isDictionary="
				+ isDictionary + ", className=" + className
				+ ", dictionaryKeyType=" + dictionaryKeyType
				+ ", dictionaryValueType=" + dictionaryValueType + "]";
	}
	
}
