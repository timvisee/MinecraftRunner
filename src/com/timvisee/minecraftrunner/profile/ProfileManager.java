package com.timvisee.minecraftrunner.profile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.timvisee.minecraftrunner.MinecraftRunner;
import com.timvisee.minecraftrunner.configuration.ConfigurationSection;
import com.timvisee.minecraftrunner.configuration.YamlConfiguration;
import com.timvisee.minecraftrunner.util.FileUtils;

public class ProfileManager {
	
	public static String DATA_FILE = "C:/Users/Tim/Desktop/res/profiles.yml";
	
	private List<Profile> profs = new ArrayList<Profile>();
	
	/**
	 * Constructor
	 */
	public ProfileManager() {
		// Add the default profile
		this.profs.add(new DefaultProfile(getUniqueId()));
	}
	
	/**
	 * Add a profile
	 * @param p Profile to add
	 */
	public void addProfile(Profile p) {
		this.profs.add(p);
	}
	
	/**
	 * Check if there's any profile with an ID
	 * @param id The profile ID to check for
	 * @return True if there's any profile with this ID
	 */
	public boolean isProfileWithId(int id) {
		for(Profile p : this.profs)
			if(p.getId() == id)
				return true;
		return false;
	}
	
	/**
	 * Get a list of profiles
	 * @return Custom list
	 */
	public List<Profile> getProfiles() {
		return this.profs;
	}
	
	/**
	 * Set the list of profiles
	 * @return Profiles list
	 */
	public void setProfiles(List<Profile> profs) {
		this.profs = profs;
	}
	
	/**
	 * Return the profiles count
	 * @return Profiles count
	 */
	public int getProfilesCount() {
		return this.profs.size();
	}
	
	/**
	 * Get a profile by it's ID
	 * @param id Profile ID
	 * @return Profile with ID or null
	 */
	public Profile getProfile(int id) {
		for(Profile p : profs)
			if(p.getId() == id)
				return p;
		return null;
	}
	
	/**
	 * Get a profile by index
	 * @param i Profile index
	 * @return Profile with ID or null
	 */
	public Profile getProfileByIndex(int i) {
		return this.profs.get(i);
	}
	
	/**
	 * Remove a profile
	 * @param p Profile to remove
	 */
	public void removeProfile(Profile p) {
		removeProfile(p.getId());
	}
	
	/**
	 * Remove a profile
	 * @param id Profile ID to remove
 	 */
	public void removeProfile(int id) {
		for(int i = 0; i < this.profs.size(); i++) {
			if(this.profs.get(i).getId() == id) {
				this.profs.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Remove a profile by index
	 * @param i Index
	 */
	public void removeProfileByIndex(int i) {
		this.profs.remove(i);
	}
	
	/**
	 * Get an unique ID
	 * @return Unique ID
	 */
	public int getUniqueId() {
		int highestId = -1;
		for(Profile p : this.profs)
			if(highestId < p.getId())
				highestId = p.getId();
		return (highestId + 1);
	}
	
	/**
	 * Get the default file to save the profiles in
	 * @return Default profiles file
	 */
	public File getDefaultFile() {
		return new File(FileUtils.getAppData(), "data/profiles.yml");
	}
	
	/**
	 * Load the profiles from an external file
	 * @return False if failed
	 */
	public boolean load() {
		return load(getDefaultFile());
	}
	
	/**
	 * Load the profiles from an external file
	 * @param f File to load the profiles from
	 * @return False if failed
	 */
	public boolean load(File f) {
		// Make sure the file is not null
		if(f == null)
			return false;
		
		// Make sure the file exists and that the file is a file
		if(!f.exists() || !f.isFile())
			return false;
		
		// Load the external configuration file
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		// TODO: File version check from the 'version' node
		
		// Make sure the 'profiles' section exists
		if(!config.isConfigurationSection("profiles"))
			return false;
		
		// Get the profiles configuration section
		ConfigurationSection profsSection = config.getConfigurationSection("profiles");
		
		// Get a list of keys
		List<String> keys = profsSection.getKeys("");
		
		// Define the list to put all the profiles in
		List<Profile> newProf = new ArrayList<Profile>();
		
		// Loop through all the keys and load the file
		boolean defProfAvailable = false;
		for(String key : keys) {
			// Get the profile's section
			ConfigurationSection pSection = profsSection.getConfigurationSection(key);
			
			// Make sure the 'id' node is set and that it's an integer
			if(!pSection.isInt("id"))
				continue;
			
			// Is the current entry the default profile
			if(pSection.getBoolean("default", false)) {
				DefaultProfile defProf = new DefaultProfile(pSection.getInt("id"));
				
				// Add the profile to the list
				newProf.add(defProf);
				defProfAvailable = true;
				continue;
			}
			
			// Make sure the 'name' node is set and that it's a string
			if(!pSection.isSet("name"))
				continue;
			
			// Construct a new profile
			Profile p = new Profile(pSection.getInt("id"), pSection.getString("name"));

			if(pSection.isString("customDir"))
				p.setCustomDirectory(pSection.getString("customDir", null));
			
			if(pSection.isString("customJar"))
				p.setCustomJar(pSection.getString("customJar", null));
			
			// Add the profile to the list
			newProf.add(p);
		}
		
		// Make sure there's any default profile available
		if(!defProfAvailable) {
			DefaultProfile defProf = new DefaultProfile(getUniqueId());
			newProf.add(0, defProf);
		}
		
		// Replace the profiles list
		this.profs = newProf;
		
		return true;
	}
	
	/**
	 * Save the list of profiles to an external file
	 * @return False if failed
	 */
	public boolean save() {
		return save(getDefaultFile());
	}
	
	/**
	 * Save the list of profiles to an external file
	 * @param f File to save the profiles to
	 * @return False if failed
	 */
	public boolean save(File f) {
		// Make sure the file is not null
		if(f == null)
			return false;
		
		// Construct a configuration to store the players in
		YamlConfiguration config = new YamlConfiguration();
		
		// Create the profiles section
		ConfigurationSection profsSection = config.createConfigurationSection("profiles");
		
		// Put the profiles into the configuration file
		int i = 0;
		for(Profile p : this.profs) {
			// Create a section for the current profile
			ConfigurationSection pSection = profsSection.createConfigurationSection(String.valueOf(i));
			
			// Store the profile data
			pSection.set("id", p.getId());
			if(p instanceof DefaultProfile) {
				pSection.set("default", true);
			} else {
				pSection.set("default", false);
				pSection.set("name", p.getName());
				if(p.getUseCustomDirectory())
					pSection.set("customDir", p.getCustomDirectoryPath());
				if(p.getUseCustomJar())
					pSection.set("customJar", p.getCustomJarPath());
			}
			
			// Increase the index counter
			i++;
		}
		
		// Add the version number to the config file
		config.set("version", MinecraftRunner.VERSION);
		
		// Store the configuration into an external file
		try {
			config.save(f);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
