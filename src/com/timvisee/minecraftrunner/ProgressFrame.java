package com.timvisee.minecraftrunner;

import java.awt.Dialog;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

// public class LoginDialog extends JDialog implements ProgressListener {
public class ProgressFrame extends JDialog {
    
	private static final long serialVersionUID = 9067989759873285740L;
	private JProgressBar progressBar;
    private JLabel statusLbl;
    
    // private JButton cancelButton;
    
    public ProgressFrame(Window owner, String title) {
    	// Construct the super class
        super(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        
        // Build the frame
        buildUI();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setSize(300, getHeight());
        setLocationRelativeTo(owner);
    }
    
    private void buildUI() {
        JPanel container = new JPanel();
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
        statusLbl = new JLabel("Initializing...", SwingConstants.LEADING);
        statusPanel.add(statusLbl);
        statusPanel.add(Box.createHorizontalGlue());
        container.add(statusPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(1000);
        //progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 20));
        buttonPanel.add(progressBar);
        //buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        //cancelButton = new JButton("Cancel");
        //buttonPanel.add(cancelButton);
        container.add(buttonPanel);
        
        add(container); 
        
        pack();
    }
    
    /**
     * Set the title of the progression window
     */
    public void setTitle(String title) {
    	super.setTitle(title);
    }
    
    /**
     * Set the status line in the progression frame
     * @param status Status text
     */
    public void setStatus(String status) {
    	this.statusLbl.setText(status);
    }
}
