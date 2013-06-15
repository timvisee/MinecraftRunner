package com.timvisee.minecraftrunner.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.timvisee.minecraftrunner.MinecraftRunner;
import com.timvisee.minecraftrunner.configuration.ConfigurationSection;
import com.timvisee.minecraftrunner.configuration.YamlConfiguration;


public class PlayerManager {
	
	private List<Player> players = new ArrayList<Player>();
	
	/**
	 * Constructor
	 */
	public PlayerManager() { }
	
	/**
	 * Constructors
 	 * @param players List of players
	 */
	public PlayerManager(List<Player> players) {
		this.players = players;
	}
	
	/**
	 * Get the list of players
	 * @return List of players
	 */
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public void setPlayers(List<Player> players) {
		if(players == null)
			this.players.clear();
		else
			this.players = players;
	}
	
	public void addPlayer(Player p) {
		// Make sure the player is not null
		if(p == null)
			return;
		
		// Add the player to the list
		this.players.add(p);
	}
	
	/**
	 * Get a player by index
	 * @param i Index of the player
	 * @return Player or null
	 */
	public Player getPlayer(int i) {
		return this.players.get(i);
	}
	
	/**
	 * Get the player count
	 * @return Player's count
	 */
	public int getPlayersCount() {
		return this.players.size();
	}
	
	/**
	 * Check if there's any player with the specified login
	 * @param login Login to check for
	 * @return False if there's no player listed with this login
	 */
	public boolean isPlayerWithLogin(String login) {
		for(Player p : this.players)
			if(p.getLogin().equals(login))
				return true;
		return false;
	}
	
	/**
	 * Remove a player by index
	 * @param i Index
	 */
	public void removePlayer(int i) {		
		if(i + 1 > this.players.size() || i < 0)
			return;
		
		this.players.remove(i);
	}
	
	/**
	 * Get the default players file
	 * @return Default players file
	 */
	public File getPlayersFile() {
		return new File(MinecraftRunner.getDataFolder(), "data/players.yml");
	}

	/**
	 * Load the players from an external file
	 * @return False if failed
	 */
	public boolean load() {
		return load(getPlayersFile());
	}

	/**
	 * Load the players from an external file
	 * @param f File to load the players from
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
		
		// Make sure the 'players' section exists
		if(!config.isConfigurationSection("players"))
			return false;
		
		// Get the players configuration section
		ConfigurationSection playersSection = config.getConfigurationSection("players");
		
		// Get a list of keys
		List<String> keys = playersSection.getKeys("");
		
		// Define the list to put all the players in
		List<Player> newPlayers = new ArrayList<Player>();
		
		// Loop through all the keys and load the file
		for(String key : keys) {
			// Get the player's sectoin
			ConfigurationSection pSection = playersSection.getConfigurationSection(key);
			
			// Make sure the 'login' section is available
			if(!pSection.isSet("login"))
				continue;
			
			// Construct a new player
			Player p = new Player(pSection.getString("login", "Player"));

			if(pSection.isString("pass"))
				p.setPassword(pSection.getString("pass", ""));
			
			if(pSection.isString("alias"))
				p.setAlias(pSection.getString("alias", ""));
			
			// Add the player to the list
			newPlayers.add(p);
		}
		
		// Replace the player list
		this.players = newPlayers;
		
		return true;
	}
	
	/**
	 * Save the list of players to an external file
	 * @return False if failed
	 */
	public boolean save() {
		return save(getPlayersFile());
	}
	
	/**
	 * Save the list of players to an external file
	 * @param f File to save the players to
	 * @return False if failed
	 */
	public boolean save(File f) {
		// Make sure the file is not null
		if(f == null)
			return false;
		
		// Construct a configuration to store the players in
		YamlConfiguration config = new YamlConfiguration();
		
		// Create the players section
		ConfigurationSection playersSection = config.createConfigurationSection("players");
		
		// Put the players into the configuration file
		int i = 0;
		for(Player p : this.players) {
			// Create a section for the current player
			ConfigurationSection pSection = playersSection.createConfigurationSection(String.valueOf(i));
			
			// Store the user's data
			if(p.hasAlias())
				pSection.set("alias", p.getAlias());
			pSection.set("login", p.getLogin());
			if(p.isPasswordStored())
				pSection.set("pass", p.getPassword());
			
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
	
	// TODO: Add more functions
}
