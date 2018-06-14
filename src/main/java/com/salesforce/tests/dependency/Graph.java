package com.salesforce.tests.dependency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Stack;

public class Graph {
	private static final int TOTAL_VERTEX_COUNT = 1000;
	private LinkedList<Integer> adjList[];
	private HashMap<String,String> map;
	private int vertex;
	LinkedHashSet<String> removedComponents;
	public LinkedHashSet<String> installedComponents;
	public HashSet<String> independentInstall;
	public CommandHandler obj;
	//constructor
	public Graph(){
		map = new HashMap<>();
		adjList = new LinkedList[TOTAL_VERTEX_COUNT];
		this.vertex = 0;
		removedComponents = new LinkedHashSet<>();
		installedComponents = new LinkedHashSet<>();
		independentInstall = new HashSet<>();
		for(int index=0; index<TOTAL_VERTEX_COUNT; index++){
			adjList[index]  =new LinkedList<>();
		}
	}
	
	//Method to add a single dependency in the graph
	public boolean addEdge(String source, String destination){
		int sourceInt = -1, destinationInt = -1;
		//check if source exists in the map
		if(!map.containsKey(source)){
			map.put(source, Integer.toString(this.vertex));
			map.put(Integer.toString(this.vertex), source);
			this.vertex++;
		}
		//check if source exists in the map
		if(!map.containsKey(destination)){
				map.put(destination, Integer.toString(this.vertex));
				map.put(Integer.toString(this.vertex), destination);
				this.vertex++;
		}
		sourceInt = Integer.parseInt(map.get(source));
		destinationInt = Integer.parseInt(map.get(destination));
		if(adjList[destinationInt].contains(sourceInt)){
			System.out.println(destination  + " depends on " + source + ", ignoring command");
			return false;
		}
		adjList[sourceInt].add(destinationInt);
		return false;
	}
	
	//topological sort helper
	public void topologicalSortHelper(int vertex, boolean[] visited, Stack<Integer> stack){
		visited[vertex] = true;
		Iterator<Integer> iter = adjList[vertex].iterator();
		Integer index;
		while(iter.hasNext()){
			index = iter.next();
			if(visited[index] == false){
				topologicalSortHelper(index, visited, stack);
			}
		}
		stack.push(new Integer(vertex));
	}
	
	//topological sort
	public void topologicalSort(String sourceOfInstall, LinkedHashSet<String> installedComponents){
		Stack<Integer> stack = new Stack();
		boolean visited[] = new boolean[this.vertex];
		Arrays.fill(visited, false);
		for(int index=0; index<visited.length; index++){
			if(visited[index] == false){
				topologicalSortHelper(index, visited, stack);
			}	
		}
		boolean isStartOfInstall = false;
		while(!stack.isEmpty()){
			int val = stack.pop();
			String component = map.get(Integer.toString(val));
			if(isStartOfInstall && !installedComponents.contains(component)){
				if(adjList[Integer.parseInt(map.get(sourceOfInstall))].contains(val)){
					System.out.println("Installing " + component);
					installedComponents.add(component);
				}
			}
			
			if(!isStartOfInstall && component.equals(sourceOfInstall)){
				isStartOfInstall = true;
			}
		}
		
		if(!installedComponents.contains(sourceOfInstall)){
			System.out.println("Installing " + sourceOfInstall);
			installedComponents.add(sourceOfInstall);
		}
	}
	
	//Helper method to remove a component
	public boolean removeHelper(String component, String original){
		boolean safeToRemove = true;
		for(int i=0; i<this.vertex;i++){
			if(adjList[i].contains(Integer.parseInt(map.get(component)))){
				safeToRemove = false;
			}
		}
		if(safeToRemove){
			if(component.equals(original)){
				System.out.println("Removing " + component);
				removedComponents.add(component);
				installedComponents.remove(component);
				independentInstall.remove(component);
				int componentIndex = Integer.parseInt(map.get(component));
				LinkedList<Integer> listOfComponents = adjList[componentIndex];
				adjList[componentIndex] = new LinkedList<>();
				if(listOfComponents.size()>0){
					for(int destination: listOfComponents){
						removeHelper(map.get(Integer.toString(destination)),original);
					}
				}
			}
			else{
				if(!independentInstall.contains(component)){
					System.out.println("Removing " + component);
					removedComponents.add(component);
					installedComponents.remove(component);
					independentInstall.remove(component);
					int componentIndex = Integer.parseInt(map.get(component));
					LinkedList<Integer> listOfComponents = adjList[componentIndex];
					adjList[componentIndex] = new LinkedList<>();
					if(listOfComponents.size()>0){
						for(int destination: listOfComponents){
							removeHelper(map.get(Integer.toString(destination)),original);
						}
					}
				}
			}
		}
		return safeToRemove;
	}
	
	//check if a component can be removed
	public void removeComponent(String component){
		if(!installedComponents.contains(component)){
			System.out.println(component + " is not installed");
			return;
		}
		boolean canBeRemoved = removeHelper(component,component);
		if(!canBeRemoved){
			System.out.println(component + " is still needed");
		}
	}
	
	//Method to install components
	public void installComponents(String sourceInstaller){
		if(installedComponents.contains(sourceInstaller)){
			System.out.println(sourceInstaller + " is already installed");
		}
		else{
			independentInstall.add(sourceInstaller);
			topologicalSort(sourceInstaller, installedComponents);
		}
	}
	
	//Method to list the installed components
	public void listComponents(){
		for(String component: installedComponents){
			System.out.println(component);
		}
	}
	
}
