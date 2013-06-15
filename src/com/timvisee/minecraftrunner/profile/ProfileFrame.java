package com.timvisee.minecraftrunner.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.timvisee.minecraftrunner.MinecraftRunner;
import com.timvisee.minecraftrunner.util.FileUtils;
import com.timvisee.minecraftrunner.util.MCUtils;

public abstract class ProfileFrame extends JDialog {

	private static final long serialVersionUID = 5363319517090557033L;

	protected JTextField nameField;
	protected JCheckBox baseCheckbox;
	protected JTextField baseField;
	protected JButton baseBrowseBtn;
	protected JCheckBox customJarCheckbox;
	protected JTextField customJarField;
	protected JButton customJarBrowseBtn;
	
	protected JButton okBtn;
	protected JButton cancelBtn;
	
	/**
	 * Constructor
	 * @param owner Owner
	 * @param p Player to edit
	 */
	public ProfileFrame(JDialog owner, String dialogTitle) {
		// Construct the parent class
		super(owner, dialogTitle, true);
		
		/*JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select a profile directory");
		chooser.showDialog(this, "Select");*/
		
		// Build the dialog UI
		buildUI();
		
		// Focus the name field
		nameField.selectAll();
		
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
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        okBtn.setPreferredSize(new Dimension(70, okBtn.getPreferredSize().height));
        cancelBtn.setPreferredSize(new Dimension(70, cancelBtn.getPreferredSize().height));
        buttonsPanel.add(okBtn);
        buttonsPanel.add(cancelBtn);

        final ProfileFrame instance = this;
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instance.dispose();
            }
        });
        
        JPanel mainPnl = new JPanel();
		mainPnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));
		mainPnl.add(getProfilePanel());
		mainPnl.add(buttonsPanel, BorderLayout.SOUTH);
		mainPnl.setPreferredSize(new Dimension(getWidth(), mainPnl.getPreferredSize().height));
		add(mainPnl);
		
		pack();
	}
	
	public JPanel getProfilePanel() {
		final ProfileFrame self = this;
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Profile"));
		
		GridBagConstraints fieldC = new GridBagConstraints();
        fieldC.fill = GridBagConstraints.HORIZONTAL;
        fieldC.weightx = 1.0;
        fieldC.gridwidth = GridBagConstraints.REMAINDER;
        fieldC.insets = new Insets(2, 1, 2, 1);

        GridBagConstraints labelC = (GridBagConstraints) fieldC.clone();
        labelC.weightx = 0.0;
        labelC.gridwidth = 1;
        labelC.insets = new Insets(1, 3, 1, 10);

        GridBagConstraints checkboxC = (GridBagConstraints) fieldC.clone();
        checkboxC.insets = new Insets(5, 2, 1, 2);

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        JLabel nameLbl = new JLabel("Name:", SwingConstants.LEFT);
        nameField = new JTextField("My Profile");

        baseCheckbox = new JCheckBox("Use custom directory");
        baseCheckbox.setSelected(true);
        JLabel dirLbl = new JLabel("Directory:", SwingConstants.LEFT);
        baseField = new JTextField();
        baseField.setText(MCUtils.getBaseDirectory().getAbsolutePath());
        baseBrowseBtn = new JButton("...");
        baseField.setEnabled(baseCheckbox.isSelected());
        baseBrowseBtn.setEnabled(baseCheckbox.isSelected());
		
		customJarCheckbox = new JCheckBox("Use custom JAR file");
		customJarCheckbox.setSelected(false);
        JLabel customJarLbl = new JLabel("Custom JAR:", SwingConstants.LEFT);
		customJarField = new JTextField();
		customJarField.setText(MCUtils.getMinecraftJar().getAbsolutePath());
		customJarBrowseBtn = new JButton("...");
		customJarField.setEnabled(customJarCheckbox.isSelected());
        customJarBrowseBtn.setEnabled(customJarCheckbox.isSelected());
		
        baseCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JCheckBox checkbox = (JCheckBox) e.getSource();
            	baseField.setEnabled(checkbox.isSelected());
                baseBrowseBtn.setEnabled(checkbox.isSelected());
            }
        });
        customJarCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JCheckBox checkbox = (JCheckBox) e.getSource();
            	customJarField.setEnabled(checkbox.isSelected());
            	customJarBrowseBtn.setEnabled(checkbox.isSelected());
            }
        });

        nameLbl.setLabelFor(nameField);
        dirLbl.setLabelFor(baseField);
        customJarLbl.setLabelFor(customJarField);

        layout.setConstraints(nameField, fieldC);
        layout.setConstraints(baseField, fieldC);
        layout.setConstraints(customJarField, fieldC);
        
        panel.add(nameLbl, labelC);
        panel.add(nameField, fieldC);
        
        JPanel basePnl = new JPanel();
        basePnl.setLayout(new BoxLayout(basePnl, BoxLayout.X_AXIS));
        dirLbl.setLabelFor(basePnl);
        baseBrowseBtn.setPreferredSize(
        		new Dimension(baseField.getPreferredSize().height + 5, baseField.getPreferredSize().height));
        basePnl.add(baseField);
        basePnl.add(Box.createHorizontalStrut(3));
        basePnl.add(baseBrowseBtn);
        panel.add(baseCheckbox, checkboxC);
        panel.add(dirLbl, labelC);
        panel.add(basePnl, fieldC);
        
        baseBrowseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Get the current path
            	final String curPath = baseField.getText();
            	
            	// Create a directory chooser window
            	JFileChooser chooser = new JFileChooser();
            	chooser.setAcceptAllFileFilterUsed(false);
            	chooser.setMultiSelectionEnabled(false);
            	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            	if(FileUtils.isValidPath(curPath) && (new File(curPath)).isDirectory())
            		chooser.setCurrentDirectory(new File(curPath));
            	else
            		chooser.setCurrentDirectory(MCUtils.getBaseDirectory());
        		chooser.setDialogTitle("Select a profile folder");
        		int result = chooser.showDialog(self, "Select");
        		
        		// TODO: JAR file filter
        		
        		// Make sure any folder was selected
        		if(result != JFileChooser.APPROVE_OPTION)
        			return;
        		
        		// TODO: Check if the folder exists
        		
        		// Set the path
        		baseField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        JPanel customJarPnl = new JPanel();
        customJarPnl.setLayout(new BoxLayout(customJarPnl, BoxLayout.X_AXIS));
        customJarLbl.setLabelFor(customJarPnl);
        customJarBrowseBtn.setPreferredSize(
        		new Dimension(customJarField.getPreferredSize().height + 5, customJarField.getPreferredSize().height));
        customJarPnl.add(customJarField);
        customJarPnl.add(Box.createHorizontalStrut(3));
        customJarPnl.add(customJarBrowseBtn);
        panel.add(customJarCheckbox, checkboxC);
        panel.add(customJarLbl, labelC);
        panel.add(customJarPnl, fieldC);
        
        customJarBrowseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Get the current path
            	final String curPath = customJarField.getText();
            	
            	// Create a file chooser window
            	JFileChooser chooser = new JFileChooser();
            	chooser.setAcceptAllFileFilterUsed(false);
            	chooser.setMultiSelectionEnabled(false);
            	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            	if(FileUtils.isValidPath(curPath) && (new File(curPath)).isFile())
            		chooser.setSelectedFile(new File(curPath));
            	else
            		chooser.setSelectedFile(MCUtils.getMinecraftJar());
        		chooser.setDialogTitle("Select a JAR file");
        		int result = chooser.showDialog(self, "Select");
        		
        		// TODO: JAR file filter
        		
        		// Make sure any file was selected
        		if(result != JFileChooser.APPROVE_OPTION)
        			return;
        		
        		// TODO: Check if the file exists
        		
        		// Set the path
        		customJarField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
		
		return panel;
	}
}
