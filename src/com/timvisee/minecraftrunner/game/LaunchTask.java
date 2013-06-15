package com.timvisee.minecraftrunner.game;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.timvisee.minecraftrunner.LoginSession;
import com.timvisee.minecraftrunner.ProgressFrame;
import com.timvisee.minecraftrunner.LoginSession.LoginException;
import com.timvisee.minecraftrunner.LoginSession.OutdatedLauncherException;
import com.timvisee.minecraftrunner.profile.Profile;

public class LaunchTask implements Runnable {

	private String login;
	private String pass;
	
	private Profile prof;
	
	private boolean autoConnectToServer = false;
	private String autoConnectToServerHost = "";
	private String autoConnectToServerPort = "25565";
	
	private String user;
	private String sessId;
	
	private LoginSession sess;

	private JFrame mainFrame;
	private ProgressFrame progFrame;
	
	/**
	 * Constructor
	 * @param login Login username
	 */
	public LaunchTask(JFrame mainFrame, ProgressFrame progFrame, String login, String pass, Profile prof) {
		// Store the main and the progression frame instance
		this.mainFrame = mainFrame;
		this.progFrame = progFrame;
		
		// Store the player's login and password
		this.login = login;
		this.pass = pass;
		
		// Store the profile
		this.prof = prof;
	}
	
	/**
	 * Get the player's login
	 * @return Player's login
	 */
	public String getLogin() {
		return this.login;
	}
	
	/**
	 * Task
	 */
	@Override
	public void run() {
		// Set the state of the progress frame
		this.progFrame.setTitle("Logging in...");
		this.progFrame.setStatus("Initializing...");
		
		// Login
		boolean succeed = false;
		try {
			succeed = login();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Dispose the progression frame
		this.progFrame.dispose();
		
		if(!succeed)
			return;
		
		// Hide the main window
		this.mainFrame.setVisible(false);
		
		// TODO: Check if everything in the login session is valid...
		
		// Launch Minecraft
		MCLauncher launcher;
		if(!autoConnectToServer)
			launcher = new MCLauncher(this.user, this.sessId, this.prof);
		else
			launcher = new MCLauncher(this.user, this.sessId, this.prof, this.autoConnectToServerHost, this.autoConnectToServerPort);
		launcher.setWindowDimensions(new Dimension(1280, 720));
		launcher.launch();
	}
	
	public boolean login() throws Exception {
		// Set the status line in the progression frame
		this.progFrame.setStatus("Connecting to minecraft.net...");
		
		// Construct a login session
		this.sess = new LoginSession(this.login);
		
		// TODO: Should be played in offline mode?
		
		try {
			// Try to login
			if(!this.sess.login(this.pass)) {
				JOptionPane.showMessageDialog(progFrame, "Invalid username or password!", "Invalid Details", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			// Store the username and the session ID
			this.user = this.sess.getUsername();
			this.sessId = this.sess.getSessionId();

			// Set the status
			this.progFrame.setStatus("Logged in!");
			return true;
			
		} catch (OutdatedLauncherException e) {
			JOptionPane.showMessageDialog(progFrame, "Your launcher is outdated", "Outdated Launcher", JOptionPane.ERROR_MESSAGE);
			
		} catch (LoginException e) {
			if (e.getMessage().equalsIgnoreCase("User not premium")) {
				// TODO: Start MC demo
				JOptionPane.showMessageDialog(progFrame, "You don't have a premium Minecraft account", "Not Premium", JOptionPane.ERROR_MESSAGE);
				
            } else if (e.getMessage().equalsIgnoreCase("Account migrated, use e-mail as username.")) {
				JOptionPane.showMessageDialog(progFrame, "Your account has been migrated, use your E-Mail address to login", "Account Migrated", JOptionPane.ERROR_MESSAGE);
				
            } else
				JOptionPane.showMessageDialog(progFrame, "A login error has occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			
		} catch (IOException e) {
			// TODO: Check if the login servers are down!
			JOptionPane.showMessageDialog(progFrame, "An error has occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		// Something went wrong, return false
        this.pass = null;
		return false;
	}
	
	public Profile getProfile() {
		return this.prof;
	}
	
	public void setProfile(Profile prof) {
		this.prof = prof;
	}
	
	public void setAutoConnectToServer(boolean autoConnect) {
		this.autoConnectToServer = autoConnect;
	}
	
	public void setAutoConnectToServerHost(String host) {
		this.autoConnectToServerHost = host;
	}
	
	public void setAutoConnectToServerPort(String port) {
		this.autoConnectToServerPort = port;
	}
}
