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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.timvisee.minecraftrunner.MinecraftRunner;

public class EditPlayerFrame extends JDialog {

	private static final long serialVersionUID = 5363319517090557033L;

	public static final String DIALOG_TITLE = "Edit Player";
	
	private Player p;

	private JCheckBox aliasCheckbox;
	private JTextField aliasField;
	//private JTextField userField;
	private JCheckBox passCheckbox;
	private JPasswordField passField;
	
	/**
	 * Constructor
	 * @param owner Owner
	 * @param p Player to edit
	 */
	public EditPlayerFrame(JDialog owner, Player p) {
		// Construct the parent class
		super(owner, DIALOG_TITLE, true);
		
		// Store the player param
		this.p = p;
		
		buildUI();
		
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
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        okBtn.setPreferredSize(new Dimension(70, okBtn.getPreferredSize().height));
        cancelBtn.setPreferredSize(new Dimension(70, cancelBtn.getPreferredSize().height));
        buttonsPanel.add(okBtn);
        buttonsPanel.add(cancelBtn);

        final EditPlayerFrame instance = this;
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
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

        JLabel userVal = new JLabel("");
        
        //userField = new JTextField();
        passCheckbox = new JCheckBox("Remember password");
		passField = new JPasswordField();
		
		userVal.setText(this.p.getLogin());
		if(this.p.isPasswordStored()) {
			passField.setText(this.p.getPassword());
			passCheckbox.setSelected(true);
		} else {
			passField.setText("");
			passCheckbox.setSelected(false);
		}
		passField.setEnabled(passCheckbox.isSelected());
		
        passCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JCheckBox checkbox = (JCheckBox) e.getSource();
            	passField.setEnabled(checkbox.isSelected());
            }
        });

        userLbl.setLabelFor(userVal);
        passLbl.setLabelFor(passField);
        layout.setConstraints(userVal, fieldC);
        layout.setConstraints(passField, fieldC);
        
        panel.add(userLbl, labelC);
        panel.add(userVal, fieldC);
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
        
        if(this.p.hasAlias()) {
        	aliasField.setText(this.p.getAlias());
        	aliasCheckbox.setSelected(true);
        } else {
        	aliasField.setText("");
        	aliasCheckbox.setSelected(false);
        }
        aliasField.setEnabled(aliasCheckbox.isSelected());
        
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
	
	public void apply() {
		if(aliasCheckbox.isSelected())
			this.p.setAlias(aliasField.getText());
		else
			this.p.resetAlias();
		
		if(passCheckbox.isSelected())
			this.p.setPassword(new String(passField.getPassword()));
		else
			this.p.clearPassword();
	}
}
