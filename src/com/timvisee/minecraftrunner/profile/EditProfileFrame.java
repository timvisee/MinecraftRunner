package com.timvisee.minecraftrunner.profile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.timvisee.minecraftrunner.util.FileUtils;

public class EditProfileFrame extends ProfileFrame {

	private static final long serialVersionUID = 207390128498870843L;
	
	private Profile p;
	
	public static final String DIALOG_TITLE = "Edit Profile";
	
	public EditProfileFrame(JDialog owner, ProfileManager pm, Profile p) {
		super(owner, DIALOG_TITLE);
		
		// Store the profile
		this.p = p;
		
		nameField.setText(this.p.getName());
		nameField.selectAll();
		baseCheckbox.setSelected(this.p.getUseCustomDirectory());
		baseField.setText(this.p.getDirectoryPath());
		baseField.setEnabled(this.p.getUseCustomDirectory());
		baseBrowseBtn.setEnabled(this.p.getUseCustomDirectory());
		customJarCheckbox.setSelected(this.p.getUseCustomJar());
		customJarField.setText(this.p.getJarPath());
		customJarField.setEnabled(this.p.getUseCustomJar());
		customJarBrowseBtn.setEnabled(this.p.getUseCustomJar());
		
		// Set up the listener for the 'create' button
		final EditProfileFrame instance = this;
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!editProfile())
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
	public boolean editProfile() {
		// Validate all the details first
		if(!validateDetails())
			return false;
		
		// Update the profile
		this.p.setName(nameField.getText());
		
		if(baseCheckbox.isSelected())
			this.p.setCustomDirectory(baseField.getText());
		else
			this.p.setUseCustomDirectory(false);
		if(customJarCheckbox.isSelected())
			this.p.setCustomJar(customJarField.getText());
		else
			this.p.setUseCustomJar(false);
		
		return true;
	}
}
