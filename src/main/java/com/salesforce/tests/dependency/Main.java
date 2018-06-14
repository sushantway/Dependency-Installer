package com.salesforce.tests.dependency;

import java.util.Scanner;

/**
 * The entry point for the Test program
 */
public class Main {
	
	

    public static void main(String[] args) {
    	
    	CommandHandler obj = new CommandHandler();
    	
        //read input from stdin
        Scanner scan = new Scanner(System.in);

        while (true) {
            String line = scan.nextLine();

            //no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }
            
            //DEPEND command
            if(line.startsWith("DEPEND")){
            	System.out.println(line);
            	obj.addDependency(line);
            }
            
            //INSTALL command
            if(line.startsWith("INSTALL")){
            	System.out.println(line);
            	obj.installDependencies(line);
            }
            
            //LIST command
            if(line.startsWith("LIST")){
            	System.out.println(line);
            	obj.listInstalledComponents();
            }
            
            //REMOVE command
            if(line.startsWith("REMOVE")){
            	System.out.println(line);
            	obj.removeInstalledComponent(line);
            }
            
            //the END command to stop the program
            if ("END".equals(line)) {
                System.out.println("END");
                break;
            }

            //Please provide your implementation here

        }

    }
}