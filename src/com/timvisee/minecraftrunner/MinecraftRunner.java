package com.timvisee.minecraftrunner;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MinecraftRunner {
	
	public static final String VERSION = "0.1";
	public static final String FRAME_TITLE = "Minecraft Runner " + VERSION + " Alpha";
	
	// MinecraftRunner instance
	public static MinecraftRunner instance;

	/**
	 * Constructor
	 */
	public MinecraftRunner() {
		// Store the instance
		instance = this;
	}
	
	/**
	 * Main method
	 * @param args Main arguments
	 */
	public static void main(final String[] args) {
		// Construct the class
		new MinecraftRunner();
		
		// Set the HTTP agent for Minecraft Runner
        System.setProperty("http.agent", "MinecraftRunner/0.1 (+http://www.timvisee.com)");
		
		// Try to set the LAF of the program to the system's default
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
		
		// Create the main window
        SwingUtilities.invokeLater(
        		new Runnable() {
		            @Override
		            public void run() {
		                MainFrame frame = new MainFrame();
		                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        		frame.setLocationRelativeTo(null);
		        		
		        		// Is the username param set
		        		ArgsParser argsParser = new ArgsParser(args);
		        		if(argsParser.isFlagSet("username"))
		        			frame.setUsername(argsParser.getFlag("username"));
		        		if(argsParser.isFlagSet("password"))
		        			frame.setPassword(argsParser.getFlag("password"));
		        		if(argsParser.isFlagSet("username") && argsParser.isFlagSet("password") && argsParser.isFlagSet("auto-launch")) {
		        			
		        			if(argsParser.getFlag("auto-launch").equalsIgnoreCase("true")) {
		        				if(argsParser.isFlagSet("server")) {
		        					String server = argsParser.getFlag("server");
		        					
		        					String host = "";
		        					String port = "25565";
		        					if(!server.contains(":"))
		        						host = server;
		        					else {
		        						host = server.split(":")[0];
		        						port = server.split(":")[1];
		        					}
		        					
		        					frame.setAutoConnectServer(host, port);
		        				}
		        				
		        				frame.launch();
		        				
		        			} else
		        				frame.setVisible(true);
		        		} else
		        			frame.setVisible(true);
		            }
        		});
	}
	
	/**
	 * Get the data folder for Minecraft Runner
	 * @return Minecraft Runner's data folder
	 */
	public static File getDataFolder() {
		// Get the app data folder
		File appData = null;
		
		// Get the data folder
		return new File(appData, "MinecraftRunner");
	}
}
