package com.timvisee.minecraftrunner.game;

import java.applet.Applet;
import java.awt.Dimension;
import java.io.File;
import javax.swing.SwingUtilities;

import com.timvisee.minecraftrunner.JarClassLoader;
import com.timvisee.minecraftrunner.profile.Profile;
import com.timvisee.minecraftrunner.util.MCUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCLauncher {

    private ClassLoader classLoader;
    
    private String user;
    private String sessId;
    private Profile prof;
    
    private Map<String, String> params = new HashMap<String, String>();
    private Dimension windowDim;
	
    /**
     * Constructor
     */
    public MCLauncher(String user, String sessId, Profile prof) {
    	// Store the params
    	this.user = user;
    	this.sessId = sessId;
    	this.prof = prof;
    	
    	// Set the default window dimensions
    	windowDim = new Dimension(854, 480);
    	
    	// Set some default parameters
        params.put("stand-alone", "true");
        params.put("username", this.user);
        params.put("mppass", this.user);
        params.put("sessionid", this.sessId);
        //params.put("server", "smp.mcdragonrealms.com");
        //params.put("port", "25565");
        //params.put("fullscreen", "true");
    }
	
    /**
     * Constructor
     */
    public MCLauncher(String user, String sessId, Profile prof,String serverHost, String serverPort) {
    	// Run the main constructor
    	this(user, sessId, prof);
    	
    	// Set some aditional parameters
        params.put("server", serverHost);
        params.put("port", serverPort);
    }
    
    /**
     * Get the username of the player
     * @return Username
     */
    public String getUsername() {
    	return this.user;
    }
    
    /**
     * Set the username of the player
     * @param user Username of the player
     */
    public void setUsername(String user) {
    	this.user = user;
    }
    
    /**
     * Get the session ID of the player
     * @return Session ID of the player
     */
    public String getSessionId() {
    	return this.sessId;
    }
    
    /**
     * Set the session ID of the player
     * @param sessId Session ID of the player
     */
    public void setSessionId(String sessId) {
    	this.sessId = sessId;
    }
    
    /**
     * Get the profile
     * @return Profile
     */
    public Profile getProfile() {
    	return this.prof;
    }
    
    /**
     * Set the profile
     * @param prof Profile
     */
    public void setProfile(Profile prof) {
    	this.prof = prof;
    }
    
    /**
     * Set the window dimensions of the game window, needs to be changed before the game is launched.
     * @param dim New window dimensions
     */
    public void setWindowDimensions(Dimension dim) {
    	this.windowDim = dim;
    }
    
    /**
     * Launch Minecraft
     */
	public void launch() {
        System.out.println("Preparing minecraft to launch...");
        
        // Set up the environment and load the classes
        setupEnvironment();
        setupClassLoader();
        
        System.out.println("Launching minecraft...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	try {
            		// Load the Minecraft applet instance
    				Class<?> cls = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
    	            Applet game = (Applet) cls.newInstance();
    				
    	            // Create the game frame
    	            GameFrame frame = new GameFrame(windowDim);
    	            frame.setVisible(true);
    	            GameAppletContainer container = new GameAppletContainer(params, game);
    	            frame.start(container);
    				
    			} catch (Throwable e) {
    				e.printStackTrace();
    			}
            }
        });
	}
	
	/**
	 * Setup the Minecraft environment
	 */
    private void setupEnvironment() {
    	// Minecraft paths
    	final File baseDir = this.prof.getDirectory();
    	final File mcDir = MCUtils.getMinecraftDirectory(baseDir);
    	
    	// System properties for native classes
        System.setProperty("org.lwjgl.librarypath", new File(mcDir, "bin/natives").getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath", new File(mcDir, "bin/natives").getAbsolutePath());
        
        System.setProperty("user.home", baseDir.getAbsolutePath());
        if (!System.getProperty("user.home").equals(baseDir.getAbsolutePath())) {
            /*throw new LaunchException("user.home was supposed to be set to '" + expected + "', but it was '" +
                    System.getProperty("user.home") + "'");*/
        }
        
        // Show some debugging messages
        System.out.println("Appdata folder: " + System.getenv("APPDATA"));
        System.out.println("User.home: " + System.getProperty("user.home"));
        
        String appData = System.getenv("APPDATA");
        if (appData == null || !appData.equals(baseDir.getAbsolutePath())) {
            /*throw new LaunchException("APPDATA was supposed to be set to '" + expected + "', but it was '" +
                    appData + "'");*/
        }
        
        // set minecraft.applet.WrapperClass to support some new Forge Mod Loader builds
        System.setProperty("minecraft.applet.WrapperClass", "com.timvisee.minecraftrunner.game.GameAppletContainer");
        
        // Debugging message
        System.out.println("Base directory: " + baseDir.getAbsolutePath());
        System.out.println("What Minecraft will use: " + mcDir.toString());
    }
    
    /**
     * Setup the class loader
     */
    private void setupClassLoader() {
    	// Get the file paths to the minecraft directories
    	final File mcDir = MCUtils.getMinecraftDirectory(this.prof.getDirectory());
    	final File activeJar = this.prof.getJar();
    	
    	// Create a new list of files that needs to be loaded by the class loader
        List<File> files = new ArrayList<File>();
        
        // Addon's loader
        /*for (int i = addonPaths.size() - 1; i >= 0; i--) {
            String path = addonPaths.get(i);
            File f = new File(path);
            if (f.exists()) {
                logger.info("Addon: " + f.getAbsolutePath());
            } else {
                logger.warning("Addon doesn't exist: " + f.getAbsolutePath());
            }
            files.add(f);
        }*/
        
        // Add the files that needs to be loaded by the class loader
        files.add(new File(mcDir, "bin/lwjgl.jar"));
        files.add(new File(mcDir, "bin/jinput.jar"));
        files.add(new File(mcDir, "bin/lwjgl_util.jar"));
        files.add(activeJar);

        // Debugging message
        System.out.println("List of classpath entries generated!");
        
        // Generate the file paths to local URL's so it can be loaded by the class loader
        URL[] urls = new URL[files.size()];
        int i = 0;
        for (File file : files) {
            try {
                urls[i] = file.toURI().toURL();
            } catch (MalformedURLException e) { }
            System.out.println("Classpath: " + urls[i]);
            i++;
        }
        
        // Construct the class loader
        classLoader = new JarClassLoader(urls);
    }
}
