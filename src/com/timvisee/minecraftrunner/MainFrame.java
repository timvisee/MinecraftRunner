package com.timvisee.minecraftrunner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import com.timvisee.minecraftrunner.game.LaunchTask;
import com.timvisee.minecraftrunner.player.Player;
import com.timvisee.minecraftrunner.player.PlayerManager;
import com.timvisee.minecraftrunner.player.PlayerManagerComboBoxModel;
import com.timvisee.minecraftrunner.player.PlayersFrame;
import com.timvisee.minecraftrunner.profile.Profile;
import com.timvisee.minecraftrunner.profile.ProfileManager;
import com.timvisee.minecraftrunner.profile.ProfileManagerComboBoxModel;
import com.timvisee.minecraftrunner.profile.ProfilesFrame;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -4911040003703939979L;
	
	// GUI fields
	private JButton launchBtn;
	
	private JComboBox<Profile> profField;
	private JComboBox<Player> userField;
	private JPasswordField passField;

	private ProfileManager profManager;
	private PlayerManager playerManager;
	
	private String autoConnectServerHost = "";
	private String autoConnectServerPort = "25565";
	
	/**
	 * Constructor
	 */
	public MainFrame() {
		// Construct super class
		super(MinecraftRunner.FRAME_TITLE);
		
		// Construct the profile manager
		this.profManager = new ProfileManager();
		this.profManager.load();
		
		// Construct the player manager and load the data
		this.playerManager = new PlayerManager();
		this.playerManager.load();
		
		// Build the frame's UI
		buildUI();
		
		// If a player has been selected, check if the password should be filled in by default
		Player p = getSelectedPlayer();
		if(p != null)
			if(p.isPasswordStored())
				passField.setText(p.getPassword());
		
		// Set up the action listeners
		AHandler handler = new AHandler();
		launchBtn.addActionListener(handler);
	}
	
	/**
	 * Build the UI of the frame
	 */
	public void buildUI() {
		// Set the layout
		setLayout(new BorderLayout(0, 0));

		// Set some frame stuff
		setSize(300, getHeight());
		setResizable(false);
		
		// Set the icon of the frame
		try {
            InputStream in = MinecraftRunner.class.getResourceAsStream("/res/icon.png");
            if(in != null)
                setIconImage(ImageIO.read(in));
        } catch (IOException e) { }
		
		// Define the launch button
		launchBtn = new JButton("Launch Minecraft");
		
		// Create a buttons panel to put all the buttons in
		JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 2, 4, 4));
        final JButton newsBtn = new JButton("News");
        final JButton profsBtn = new JButton("Manage Profiles");
        final JButton playersBtn = new JButton("Manage Players");
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        buttonsPanel.add(launchBtn);
        buttonsPanel.add(newsBtn);
        buttonsPanel.add(profsBtn);
        buttonsPanel.add(playersBtn);
        
        final MainFrame instance = this;
        profsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Show the profiles manager dialog
                JDialog profsDialog = new ProfilesFrame(instance, instance.profManager);
                profsDialog.setVisible(true);
                
                // Update the profiles field
                profField.updateUI();
                
                // Save the data
                instance.profManager.save();
            }
        });
        playersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Show the players manager dialog
                JDialog playersDialog = new PlayersFrame(instance, instance.playerManager);
                playersDialog.setVisible(true);
                
                // Update the users field
                userField.updateUI();

                // Save the data
                instance.playerManager.save();
            }
        });
        newsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Show the news button
                JFrame newsFrame = new NewsFrame(instance);
                newsFrame.setVisible(true);
            }
        });
        //buttonsPanel.add(optionsBtn);
		
		// Create the main panel
		JPanel mainPnl = new JPanel();
		mainPnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));
		mainPnl.add(getUserPanel());
		mainPnl.add(buttonsPanel);
		mainPnl.setPreferredSize(new Dimension(getWidth(), mainPnl.getPreferredSize().height));
		add(mainPnl);
		
		// Add some menu bar options on systems with a menu bar available
		if(Platform.getPlatform().equals(Platform.MAC_OS_X)) {
			MenuBar menuBar = new MenuBar();
			Menu fileMenu = new Menu("File");
			MenuItem prefsItem = new MenuItem("Preferences");
			fileMenu.add(prefsItem);
			Menu helpMenu = new Menu("Help");
			MenuItem aboutItem = new MenuItem("About");
			helpMenu.add(aboutItem);
			menuBar.add(fileMenu);
			menuBar.add(helpMenu);
			setMenuBar(menuBar);
		}
		
		// Set up a listener for items being selected in the users field
        userField.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				Player p = null;
				if(event.getItem() instanceof Player)
					p = (Player) event.getItem();
				
				if(p.isPasswordStored()) {
					passField.setText(p.getPassword());
				} else
					passField.setText("");
			}
        });
		
		// Pack the frame
		pack();
	}
	
	
	
	
	
	
	public JPanel getUserPanel() {
		JPanel panel = new JPanel();
		
		GridBagConstraints fieldC = new GridBagConstraints();
        fieldC.fill = GridBagConstraints.HORIZONTAL;
        fieldC.weightx = 1.0;
        fieldC.gridwidth = GridBagConstraints.REMAINDER;
        fieldC.insets = new Insets(2, 1, 2, 1);

        GridBagConstraints labelC = (GridBagConstraints) fieldC.clone();
        labelC.weightx = 0.0;
        labelC.gridwidth = 1;
        labelC.insets = new Insets(1, 1, 1, 10);

        GridBagConstraints checkboxC = (GridBagConstraints) fieldC.clone();
        checkboxC.insets = new Insets(5, 2, 1, 2);

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        JLabel profLbl = new JLabel("Profile:", SwingConstants.LEFT);
        JLabel userLbl = new JLabel("Username:", SwingConstants.LEFT);
        JLabel passLbl = new JLabel("Password:", SwingConstants.LEFT);

        profField = new JComboBox<Profile>(new ProfileManagerComboBoxModel(this.profManager));
        if(profField.getItemCount() > 0)
        	profField.setSelectedIndex(0);
      
		userField = new JComboBox<Player>(new PlayerManagerComboBoxModel(this.playerManager));
        if(userField.getItemCount() > 0)
        	userField.setSelectedIndex(0);
		userField.setEditable(true);
		passField = new JPasswordField();

        profLbl.setLabelFor(profField);
        userLbl.setLabelFor(userField);
        passLbl.setLabelFor(passField);
        layout.setConstraints(profField, fieldC);
        layout.setConstraints(userField, fieldC);
        layout.setConstraints(passField, fieldC);
        
        panel.add(profLbl, labelC);
        panel.add(profField, fieldC);
        panel.add(userLbl, labelC);
        panel.add(userField, fieldC);
        panel.add(passLbl, labelC);
        panel.add(passField, fieldC);
		
		return panel;
	}
	
	// Action listener
	private class AHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// Was the launch button pressed
			if(e.getSource().equals(launchBtn)) {
				
				// Disable the launch button
				launchBtn.setEnabled(false);
				
				launch();
				
				// Enable the launch button
				launchBtn.setEnabled(true);
			}
		}
	}
	
	public String getSelectedLogin() {
		Object selectedUser = userField.getSelectedItem();
		if(selectedUser instanceof Player)
			return ((Player) selectedUser).getLogin();
		return selectedUser.toString();
	}
	
	public Player getSelectedPlayer() {
		Object selectedUser = userField.getSelectedItem();
		if(selectedUser instanceof Player)
			return (Player) selectedUser;
		return null;
	}
	
	/**
	 * Launch Minecraft
	 */
	public void launch() {
		// Get the user's login and password
		final String login = getSelectedLogin();
		final String pass = new String(passField.getPassword());
		
		// Get the selected profile
		Profile prof = (Profile) profField.getSelectedItem();
		
		// Launch Minecraft
		launch(login, pass, prof);
	}
	
	/**
	 * Launch Minecraft
	 * @param login Login (username)
	 * @param pass Password
	 */
	public void launch(String login, String pass, Profile prof) {
		// Make sure the profile instance is not null
		if(prof == null)
			return;
		
		// Create a thread to login
		ProgressFrame progFrame = new ProgressFrame(this, "Launching Minecraft...");
		
		// Launch Minecraft
		LaunchTask lt = new LaunchTask(this, progFrame, login, pass, prof);
		if(!this.autoConnectServerHost.equals("") && !this.autoConnectServerHost.equals("")) {
			lt.setAutoConnectToServer(true);
			lt.setAutoConnectToServerHost(this.autoConnectServerHost);
			lt.setAutoConnectToServerPort(this.autoConnectServerPort);
		}
		
		Thread t = new Thread(lt);
		t.start();
		
		// Make the progression frame visible
		progFrame.setVisible(true);
	}
	
	public void setUsername(String login) {
		this.userField.getEditor().setItem(login);
	}
	
	public void setPassword(String pass) {
		this.passField.setText(pass);
	}
	
	public void setAutoConnectServer(String host, String port) {
		this.autoConnectServerHost = host;
		this.autoConnectServerPort = port;
	}
}
