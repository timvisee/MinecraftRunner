package com.timvisee.minecraftrunner.profile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.timvisee.minecraftrunner.util.FileUtils;

public class CreateProfileFrame extends ProfileFrame {

	private static final long serialVersionUID = 207390128498870843L;
	
	private ProfileManager pm;
	
	public static final String DIALOG_TITLE = "Create Profile";
	
	public CreateProfileFrame(JDialog owner, ProfileManager pm) {
		super(owner, DIALOG_TITLE);
		
		// Store the profile manager
		this.pm = pm;
		
		// Set the button text
		okBtn.setText("Save");
		
		// Set up the listener for the 'create' button
		final CreateProfileFrame instance = this;
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!addProfile())
                	return;
                
                instance.dispose();
            }
        });
	}
	
	/**
	 * Validate all the entered details
	 * @return False if something was not valid
	 */
	public boolean validateDetails() {
		// Validate the name
		if(nameField.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Please fill in a profile name", "Invalid Profile Name", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// Validate the directory path
		if(baseCheckbox.isSelected()) {
			if(!FileUtils.isValidPath(baseField.getText())) {
				JOptionPane.showMessageDialog(this, "Please select a valid profile directory or use the default one", "Invalid Profile Directory", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		// Validate the custom JAR file
		if(customJarCheckbox.isSelected()) {
			if(!FileUtils.isValidPath(customJarField.getText())) {
				JOptionPane.showMessageDialog(this, "Please select a valid JAR file or use the default one", "Invalid Profile JAR", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		
		// Everything seems to be valid
		return true;
	}
	
	/**
	 * Add a player
	 * @return True if succeed
	 */
	public boolean addProfile() {
		int id = pm.getUniqueId();
		String name = nameField.getText();
		File dir = null;
		File jar = null;
		
		// Validate all the details
		if(!validateDetails())
			return false;

		if(baseCheckbox.isSelected())
			dir = new File(baseField.getText());
		if(customJarCheckbox.isSelected())
			jar = new File(customJarField.getText());
		
		Profile p = new Profile(id, name, dir, jar);
		
		// Add the profile to the list
		this.pm.addProfile(p);
		
		return true;
	}
}