package com.salesforce.tests.dependency;

import java.util.LinkedHashSet;

public class CommandHandler {
	
	public Graph obj;
	
	public CommandHandler(){
		
		obj = new Graph();
	}
	
	//Method to add dependencies in the graph
	public void addDependency(String line){
		String[] dependencies = line.split(" ");
		String source = dependencies[1];
		for(int i=2;i<dependencies.length;i++){
			obj.addEdge(source, dependencies[i]);
		}
	}
	
	//Method to install the components and its dependencies
	public void installDependencies(String line){
		String[] splittedLine = line.split(" ");
		String sourceInstaller = splittedLine[1];
		obj.installComponents(sourceInstaller);
	}
	
	//Method to list the installed components
	public void listInstalledComponents(){
		obj.listComponents();
	}
	
	//Method to remove an installed component
	public void removeInstalledComponent(String line){
		String[] splittedLine = line.split(" ");
		String removeComponent = splittedLine[1];
		obj.removeComponent(removeComponent);
	}
}
