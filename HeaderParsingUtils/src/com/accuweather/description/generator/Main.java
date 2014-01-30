package com.accuweather.description.generator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Main {

	private static HeaderParser headerParser = new HeaderParser();
	
	public static void main(String[] args) throws IOException {
		
		
		if (args.length > 0 && args.length <= 2) {
			String arg0 = args[0];
			if (arg0.equals("-dd")) {
				File output = new File("descriptions.txt");
				
				// scan the top directory for .h files
				String[] extensions = {"h"};
				Collection<File> files = FileUtils.listFiles(new File(args[1]), extensions, false);
				for (File file : files) {
					String description = descriptionPath(file);
					String text = "Generated From: " + file.getName() + "\n";
					text += description + "\n\n\n";
					
					FileUtils.writeStringToFile(output, text, true);
				}
				
				System.out.println();
				System.out.println("Description methods can be found at: " + output.getAbsolutePath());
				System.out.println();
			} else if (arg0.equals("-d")) {
				String fileLocation = args[1];
				
				String description = descriptionPath(new File(fileLocation));
				System.out.println();
				System.out.println(description);
				System.out.println();
			} else if (arg0.equals("-c")) { 
				String nsCoding = nsCodingPath(new File(args[1]));

				System.out.println();
				System.out.println(nsCoding);
				System.out.println();
			} else if (arg0.equals("-cc")) {
				File output = new File("NSCodingMethods.txt");
				
				// scan the top directory for .h files
				String[] extensions = {"h"};
				Collection<File> files = FileUtils.listFiles(new File(args[1]), extensions, false);
				for (File file : files) {
					String nsCoding = nsCodingPath(file);
					
					String text = "Generated From: " + file.getName() + "\n";
					text += nsCoding + "\n\n\n";
					
					FileUtils.writeStringToFile(output, text, true);
				}
				
				System.out.println();
				System.out.println("NSCoding methods can be found at: " + output.getAbsolutePath());
				System.out.println();
			} else if(arg0.equals("-e")) {
				String isEquals = isEqualsPath(new File(args[1]));
				
				System.out.println();
				System.out.println(isEquals);
				System.out.println();
			} else if(arg0.equals("-ee")) {
				File output = new File("IsEqualMethods.txt");
				
				// scan the top directory for .h files
				String[] extensions = {"h"};
				Collection<File> files = FileUtils.listFiles(new File(args[1]), extensions, false);
				for (File file : files) {
					String isEquals = isEqualsPath(file);
					
					String text = "Generated From: " + file.getName() + "\n";
					text += isEquals + "\n\n\n";
					
					FileUtils.writeStringToFile(output, text, true);
				}
			} else if(arg0.equals("-dic")) {
				String dictionary = dicationaryPath(new File(args[1]));
				
				System.out.println();
				System.out.println(dictionary);
				System.out.println();
			} else if(arg0.equals("-ddic")) {
				File output = new File("DictionaryMethods.txt");
				
				// scan the top directory for .h files
				String[] extensions = {"h"};
				Collection<File> files = FileUtils.listFiles(new File(args[1]), extensions, false);
				for (File file : files) {
					String dictionaries = dicationaryPath(file);
					
					String text = "Generated From: " + file.getName() + "\n";
					text += dictionaries + "\n\n\n";
					
					FileUtils.writeStringToFile(output, text, true);
				}
				
				System.out.println();
				System.out.println("Dictionary methods can be found at: " + output.getAbsolutePath());
				System.out.println();
			} else if (arg0.equals("-h")) {
				printHelp();
			} 
		} else {
			printHelp();
		}
	}

	private static String descriptionPath(File file) throws IOException {
		DescriptionGenerator descriptionGenerator = new DescriptionGenerator();
		
		List<String> lines = FileUtils.readLines(file);
		
		List<Property> properties = headerParser.extractProperties(lines);
		String classname = headerParser.extractClassname(lines);
		
		String description = descriptionGenerator.generateDescription(properties, classname);
		return description;
	}

	private static String isEqualsPath(File file) throws IOException {
		IsEqualAndHashMethodGenerator isEqualsMethodGenerator = new IsEqualAndHashMethodGenerator();
		
		List<String> lines = FileUtils.readLines(file);
		
		HeaderParser headerParser = new HeaderParser();
		List<Property> properties = headerParser.extractProperties(lines);
		
		return isEqualsMethodGenerator.generateIsEqualMethod(properties);
	}

	private static String nsCodingPath(File file) throws IOException {
		NSCodingGenerator nsCodingGenerator = new NSCodingGenerator();
		
		List<String> lines = FileUtils.readLines(file);
		
		HeaderParser headerParser = new HeaderParser();
		String classname = headerParser.extractClassname(lines);
		List<Property> properties = headerParser.extractProperties(lines);
		
		String nsCoding = nsCodingGenerator.generatorNSCodingMethods(properties, classname);
		
		return nsCoding;
	}
	
	private static String dicationaryPath(File file) throws IOException {
		NSDictionaryGenerator dictionaryGenerator = new NSDictionaryGenerator();
		
		List<String> lines = FileUtils.readLines(file);
		
		List<Property> properties = headerParser.extractProperties(lines);
		String classname = headerParser.extractClassname(lines);
		
		String dictionary = dictionaryGenerator.generateDictionaryMethods(properties, classname);
		return dictionary;
	}

	private static void printHelp() {
		System.out.println();
		System.out.println("Usage: \n");
		System.out.println("\t -d \t Provide a single file to parse. Generates description methods.");
		System.out.println("\t -dd \t Provide a directory to search for .h files. Not recursive. Generates description methods.");
		System.out.println("\t -c \t Provide a single file to parse. Generates NSCoding methods and constants.");
		System.out.println("\t -cc \t Provide a directory to search for .h files. Not recursive. Generates NSCoding methods and constants.");
		System.out.println("\t -e \t Provide a single file to parse. Generates isEquals and hash methods.");
		System.out.println("\t -ee \t Provide a directory to search for .h files. Not recursive. Generates isEquals and hash methods.");
		
		System.out.println("\t -dic \t Provide a single file to parse. Generates toDictionary methods.");
		System.out.println("\t -ddic \t Provide a directory to search for .h files. Not recursive. Generates toDictionary methods.");
		
		System.out.println("\t -h \t Print this help message.");
		System.out.println();
	}
	
}
