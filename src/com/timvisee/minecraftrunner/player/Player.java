package com.timvisee.minecraftrunner.player;

public class Player {
	
	private String alias;
	private String login;
	private String pass;
	
	/**
	 * Constructor
	 * @param login Login
	 */
	public Player(String login) {
		this.login = login;
	}
	
	/**
	 * Constructor
	 * @param login Player's login
	 * @param pass Player's pass
	 */
	public Player(String login, String pass) {
		this.login = login;
		this.pass = pass;
	}
	
	/**
	 * Constructor
	 * @param login Player's login
	 * @param pass Player's pass
	 * @param alias Player's alias
	 */
	public Player(String login, String pass, String alias) {
		this.login = login;
		this.pass = pass;
		this.alias = alias;
	}
	
	/**
	 * Get the player's alias
	 * @return Player's alias
	 */
	public String getAlias() {
		return this.alias;
	}
	
	/**
	 * Set the player's alias
	 * @param alias Player's alias
	 */
	public void setAlias(String alias) {
		this.alias = alias.trim();
	}
	
	public boolean hasAlias() {
		// Make sure the alias variable is not null
		if(this.alias == null)
			return false;
		
		// Does the player have a custom alias
		return (!this.alias.trim().equals(""));
	}
	
	/**
	 * Reset the player's alias
	 */
	public void resetAlias() {
		this.alias = "";
	}
	
	/**
	 * Get the player's login
	 * @return Player's login
	 */
	public String getLogin() {
		return this.login;
	}
	
	/**
	 * Get the player's password
	 * @return Player's password
	 */
	public String getPassword() {
		return this.pass;
	}
	
	/**
	 * Set the player's password
	 * @param pass Player's password
	 */
	public void setPassword(String pass) {
		this.pass = pass;
	}
	
	public boolean isPasswordStored() {
		// Make sure the pass variable is not null
		if(this.pass == null)
			return false;
		
		// Is the password stored
		return (this.pass.length() > 0);
	}
	
	/**
	 * Clear the player's password
	 */
	public void clearPassword() {
		this.pass = "";
	}
	
	public String toString() {
		if(hasAlias())
			return this.alias;
		return this.login;
	}
}
