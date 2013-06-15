package com.timvisee.minecraftrunner.profile;

import java.io.File;

public class DefaultProfile extends Profile {
	
	public static final String DEFAULT_PROFILE_NAME = "Default";
	
	/**
	 * Constructor
	 */
	public DefaultProfile(int id) {
		super(id, DEFAULT_PROFILE_NAME);
	}
	
	public void setName(String name) { }
	
	public void setUseCustomDirectory(boolean useCustomDir) { }
	
	public void setCustomDirectory(File dir) { }
	
	public void setCustomDirectory(String dirPath) { }
	
	public void setUseCustomJar(boolean useCustomJar) { }
	
	public void setCustomJar(File jar) { }
	
	public void setCustomJar(String jarPath) { }
	
	public String toString() {
		return DEFAULT_PROFILE_NAME;
	}
}
