package com.timvisee.minecraftrunner.profile;

import java.io.File;

import com.timvisee.minecraftrunner.util.MCUtils;

public class Profile {

	private int id;
	private String name;
	private boolean useCustomDir;
	private File customDir;
	private boolean useCustomJar;
	private File customJar;
	
	/**
	 * Constructor
	 * @param id Profile ID
	 * @param name Profile name
	 */
	public Profile(int id, String name) {
		this.id = id;
		this.name = name;
		this.useCustomDir = false;
		this.useCustomJar = false;
	}
	
	/**
	 * Constructor
	 * @param id Profile ID
	 * @param name Profile name
	 * @param customDir Custom directory
	 * @param customJar Custom jar file
	 */
	public Profile(int id, String name, File customDir, File customJar) {
		this.id = id;
		this.name = name;
		this.customDir = customDir;
		this.useCustomDir = (customDir != null);
		this.customJar = customJar;
		this.useCustomJar = (customJar != null);
	}
	
	/**
	 * Get the profile ID
	 * @return Profile ID
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Get the name of the profile
	 * @return Profile name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the name of the profile
	 * @param name Profile name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the default directory
	 * @return Default directory
	 */
	public File getDefaultDirectory() {
		return MCUtils.getBaseDirectory();
	}
	
	/**
	 * Get the default directory path
	 * @return Default directory path
	 */
	public String getDefaultDirectoryPath() {
		return getDefaultDirectory().getAbsolutePath();
	}
	
	/**
	 * Get the directory that should be used
	 * @return Directory that should be used
	 */
	public File getDirectory() {
		// Return the custom directory if it should be used, and if it's setup
		if(getUseCustomDirectory() && this.customDir != null)
			return this.customDir;
		
		// Return the default directory
		return getDefaultDirectory();
	}

	/**
	 * Get the directory path that should be used
	 * @return Directory path that should be used
	 */
	public String getDirectoryPath() {
		return getDirectory().getAbsolutePath();
	}
	
	/**
	 * Check if a custom directory should be used
	 * @return True if a custom directory should be used
	 */
	public boolean getUseCustomDirectory() {
		return this.useCustomDir;
	}
	
	/**
	 * Set if a custom directory should be used
	 * @param useCustomDir True if a custom directory should be used
	 */
	public void setUseCustomDirectory(boolean useCustomDir) {
		this.useCustomDir = useCustomDir;
	}
	
	/**
	 * Get the directory of the profile
	 * @return Directory path
	 */
	public File getCustomDirectory() {
		return this.customDir;
	}
	
	/**
	 * Get the directory path of the profile
	 * @param customDir Directory path of profile
	 */
	public String getCustomDirectoryPath() {
		return this.customDir.getAbsolutePath();
	}
	
	/**
	 * Set the directory of the profile
	 * @param dir Directory profile
	 */
	public void setCustomDirectory(File dir) {
		this.customDir = dir;
		this.useCustomDir = (dir != null);
	}
	
	/**
	 * Set the directory of the profile
	 * @param dirPath Directory profile
	 */
	public void setCustomDirectory(String dirPath) {
		setCustomDirectory(new File(dirPath));
	}
	
	/**
	 * Get the default JAR file
	 * @return Default JAR file
	 */
	public File getDefaultJar() {
		return MCUtils.getMinecraftJar();
	}

	/**
	 * Get the default Jar file path
	 * @return Default JAR file path
	 */
	public String getDefaultJarPath() {
		return getDefaultJar().getAbsolutePath();
	}
	
	/**
	 * Get the JAR file that should be used
	 * @return Jar file that should be used
	 */
	public File getJar() {
		// Should the custom jar be used
		if(getUseCustomJar() && this.customJar != null) {
			// Make sure the jar exists
			if(this.customJar.exists())
				return this.customJar;
			
			// Get the jar file from the custom directory
			File jar = MCUtils.getMinecraftJar(MCUtils.getMinecraftDirectory(getDirectory()));
			if(jar.exists())
				return jar;
		}
		
		// Return the default jar file
		return getDefaultJar();
	}
	
	/**
	 * Get the JAR file path that should be used
	 * @return JAR file path that should be used
	 */
	public String getJarPath() {
		return getJar().getAbsolutePath();
	}
	
	/**
	 * Check if a custom Jar file should be used
	 * @return True if a custom jar file should be used
	 */
	public boolean getUseCustomJar() {
		return this.useCustomJar;
	}

	/**
	 * Set if a custom jar file should be used
	 * @param useCustomJar
	 */
	public void setUseCustomJar(boolean useCustomJar) {
		this.useCustomJar = useCustomJar;
	}
	
	/**
	 * Get the custom jar file
	 * @return Custom jar file (path)
	 */
	public File getCustomJar() {
		return this.customJar;
	}

	/**
	 * Get the custom jar file
	 * @return Custom jar file (path)
	 */
	public String getCustomJarPath() {
		return this.customJar.getAbsolutePath();
	}
	
	/**
	 * Set the file path to the custom jar
	 * @param jar Custom jar file path
	 */
	public void setCustomJar(File jar) {
		this.customJar = jar;
		this.useCustomJar = (jar != null);
	}

	/**
	 * Set the file path to the custom jar
	 * @param customJar Custom jar file path
	 */
	public void setCustomJar(String jarPath) {
		setCustomJar(new File(jarPath));
	}
	
	public String toString() {
		return this.name;
	}
} 
