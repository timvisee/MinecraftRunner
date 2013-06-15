package com.timvisee.minecraftrunner.player;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.timvisee.minecraftrunner.MinecraftRunner;

public class AddPlayerFrame extends JDialog {

	private static final long serialVersionUID = 5363319517090557033L;

	public static final String DIALOG_TITLE = "Add Player";
	
	private PlayerManager pm;

	private JCheckBox aliasCheckbox;
	private JTextField aliasField;
	private JTextField userField;
	private JCheckBox passCheckbox;
	private JPasswordField passField;
	
	/**
	 * Constructor
	 * @param owner Owner
	 * @param p Player to edit
	 */
	public AddPlayerFrame(JDialog owner, PlayerManager pm) {
		// Construct the parent class
		super(owner, DIALOG_TITLE, true);
		
		// Store the player param
		this.pm = pm;
		
		// Build the dialog UI
		buildUI();
		
		// Focus the username field
		userField.selectAll();
		
		// Set the location of the dialog
		setLocationRelativeTo(owner);
	}
	
	/**
	 * Build the frame UI
	 */
	public void buildUI() {
		// Set the layout
		setLayout(new BorderLayout(0, 0));
		
		// Set some frame stuff
		setSize(350, getHeight());
		setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		// Set the icon of the frame
		try {
            InputStream in = MinecraftRunner.class.getResourceAsStream("/res/icon.png");
            if(in != null)
                setIconImage(ImageIO.read(in));
        } catch (IOException e) { }
		
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        JButton addBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        addBtn.setPreferredSize(new Dimension(70, addBtn.getPreferredSize().height));
        cancelBtn.setPreferredSize(new Dimension(70, cancelBtn.getPreferredSize().height));
        buttonsPanel.add(addBtn);
        buttonsPanel.add(cancelBtn);

        final AddPlayerFrame instance = this;
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!addPlayer())
                	return;
                
                instance.dispose();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instance.dispose();
            }
        });
        
        JPanel mainPnl = new JPanel();
		mainPnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));
		mainPnl.add(getAccountPanel());
		mainPnl.add(getAliasPanel());
		mainPnl.add(buttonsPanel, BorderLayout.SOUTH);
		mainPnl.setPreferredSize(new Dimension(getWidth(), mainPnl.getPreferredSize().height));
		add(mainPnl);
        
        /*final MainFrame instance = this;
        profsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog profsDialog = new ProfilesFrame(instance, instance.profManager);
                profsDialog.setVisible(true);
            }
        });
        playersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog playersDialog = new PlayersFrame(instance, instance.playerManager);
                playersDialog.setVisible(true);
            }
        });
        newsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
		add(mainPnl);*/
		
		// Add some menu bar options on systems with a menu bar available
		/*if(Platform.getPlatform().equals(Platform.MAC_OS_X)) {
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
		}*/
		
		pack();
	}
	
	public JPanel getAccountPanel() {
		JPanel panel = new JPanel();
		
		panel.setBorder(BorderFactory.createTitledBorder("Account"));
		
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

        JLabel userLbl = new JLabel("Username:", SwingConstants.LEFT);
        JLabel passLbl = new JLabel("Password:", SwingConstants.LEFT);
        
        userField = new JTextField();
        passCheckbox = new JCheckBox("Remember password");
		passField = new JPasswordField();
		
		passCheckbox.setSelected(true);
		passField.setEnabled(true);
		
        passCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JCheckBox checkbox = (JCheckBox) e.getSource();
            	passField.setEnabled(checkbox.isSelected());
            }
        });

        userLbl.setLabelFor(userField);
        passLbl.setLabelFor(passField);
        layout.setConstraints(userField, fieldC);
        layout.setConstraints(passField, fieldC);
        
        panel.add(userLbl, labelC);
        panel.add(userField, fieldC);
        panel.add(passCheckbox, checkboxC);
        panel.add(passLbl, labelC);
        panel.add(passField, fieldC);
		
		return panel;
	}
	
	public JPanel getAliasPanel() {
		JPanel panel = new JPanel();
		
		panel.setBorder(BorderFactory.createTitledBorder("Alias"));
		
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

        JLabel aliasLbl = new JLabel("Alias:", SwingConstants.LEFT);

        aliasCheckbox = new JCheckBox("Use custom alias");
        aliasField = new JTextField();
        
        aliasCheckbox.setSelected(false);
        aliasField.setEnabled(false);
        
        aliasCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JCheckBox checkbox = (JCheckBox) e.getSource();
            	aliasField.setEnabled(checkbox.isSelected());
            }
        });
        
        aliasLbl.setLabelFor(aliasField);
        layout.setConstraints(aliasField, fieldC);

        panel.add(aliasCheckbox, checkboxC);
        panel.add(aliasLbl, labelC);
        panel.add(aliasField, fieldC);
		
		return panel;
	}
	
	/**
	 * Add a player
	 * @return True if succeed
	 */
	public boolean addPlayer() {
		// Get the login (trimmed)
		final String login = userField.getText().trim();
		
		// Validate the username
		if(login.equals("")) {
			JOptionPane.showMessageDialog(this, "Please fill in a username", "Invalid Username", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// There may not be a player with this login already
		if(this.pm.isPlayerWithLogin(login)) {
			JOptionPane.showMessageDialog(this, "This account already exists!", "Account does already exist!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
			
		
		Player p = new Player(login);
		
		if(passCheckbox.isSelected())
			p.setPassword(new String(passField.getPassword()));
		
		if(aliasCheckbox.isSelected())
			p.setAlias(aliasField.getText());
		
		// Add the player to the list
		this.pm.addPlayer(p);
		
		return true;
	}
}
