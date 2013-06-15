package com.timvisee.minecraftrunner.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.timvisee.minecraftrunner.MinecraftRunner;
import com.timvisee.minecraftrunner.util.ListUtils;

public class ProfilesFrame extends JDialog {
	
	private static final long serialVersionUID = -4911040003703939979L;
	
	public static final String DIALOG_TITLE = "Manage Profiles";
	
	private ProfileManager pm;
	
	private JTable table;
	private JButton createBtn;
	private JButton editBtn;
	private JButton moveUpBtn;
	private JButton moveDownBtn;
	private JButton delBtn;
	
	/**
	 * Constructor
	 */
	public ProfilesFrame(JFrame owner, ProfileManager pm) {
		// Construct super class
		super(owner, DIALOG_TITLE, true);
		
		// Store the profile manager instance
		this.pm = pm;
		
		// Buidl the UI
		buildUI();
		
		// Make the frame's location relative to the owner frame
		setLocationRelativeTo(owner);
	}
	
	/**
	 * Build the frame UI
	 */
	public void buildUI() {
		// Set the layout
		setLayout(new BorderLayout(0, 0));

		// Set some frame stuff
		setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		// Set the icon of the frame
		try {
            InputStream in = MinecraftRunner.class.getResourceAsStream("/res/icon.png");
            if(in != null)
                setIconImage(ImageIO.read(in));
        } catch (IOException e) { }
		
		table = new JTable(new ProfileManagerTableModel(this.pm));
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setShowGrid(false);
		JScrollPane tablePane = new JScrollPane(table);
		tablePane.setBackground(Color.WHITE);
		tablePane.getViewport().setBackground(Color.WHITE);
		tablePane.setPreferredSize(new Dimension(300, 400));
		
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
			        public void valueChanged(ListSelectionEvent e) {
			            updateButtons();
			        }
			    });
		
		/*DefaultListModel model = new DefaultListModel();
		JList list = new JList(model);
		model.addElement("test123");*/
		
		// Create the main panel
		JPanel mainPnl = new JPanel();
		//mainPnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		//mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));
		mainPnl.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
		
		mainPnl.add(tablePane);
		mainPnl.add(getButtonsPanel());
		
		//mainPnl.setPreferredSize(new Dimension(getWidth(), mainPnl.getPreferredSize().height));
		add(mainPnl);
		
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
	
	public JPanel getButtonsPanel() {
		JPanel pnl = new JPanel();
		pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		JPanel btnsPanel = new JPanel();
		btnsPanel.setLayout(new GridLayout(5, 1, 8, 8));
		
        createBtn = new JButton("Create");
        editBtn = new JButton("Edit");
        moveUpBtn = new JButton("Move Up");
        moveDownBtn = new JButton("Move Down");
        delBtn = new JButton("Delete");
        
        btnsPanel.add(createBtn);
        btnsPanel.add(editBtn);
        btnsPanel.add(moveUpBtn);
        btnsPanel.add(moveDownBtn);
        btnsPanel.add(delBtn);
        
        // Update the buttons
        updateButtons();
        
        final ProfilesFrame instance = this;
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Show the profile creation window
                JDialog createProfDialog = new CreateProfileFrame(instance, instance.pm);
                createProfDialog.setVisible(true);
                
                // Update the table
                table.updateUI();
                
                // Save the data
                instance.pm.save();
            }
        });
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Make sure any row is selected
            	if(table.getSelectedRowCount() == 0)
            		return;
            	
            	// Get the index of the selected row
            	int rowIndex = table.getSelectedRow();
            	
            	// Make sure the default profile is not going to be edited
            	if(instance.pm.getProfileByIndex(rowIndex) instanceof DefaultProfile)
            		return;
            	
            	// Get the selected player
            	Profile p = instance.pm.getProfileByIndex(rowIndex);
            	
            	// Show the profile creation window
                JDialog editProfDialog = new EditProfileFrame(instance, instance.pm, p);
                editProfDialog.setVisible(true);
                
                // Update the table
                table.updateUI();
                
                // Save the data
                instance.pm.save();
            }
        });
        moveUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Get the selected item and check if any item is selected
            	if(table.getSelectedRows().length <= 0)
            		return;
            	
            	// Make sure the first selected item is not the first item in the list
            	if(table.getSelectedRow() <= 0)
            		return;
            	
            	// Move each item one up
            	for(int i : table.getSelectedRows()) {
            		instance.pm.setProfiles(ListUtils.moveItemUp(instance.pm.getProfiles(), i));
            	}
            	
            	// Set the selected rows
            	if(table.getSelectedRows()[0] != 0)
            		table.setRowSelectionInterval(table.getSelectedRows()[0] - 1, table.getSelectedRows()[table.getSelectedRows().length-1] - 1);
                
                // Update the table
                table.updateUI();
                
                // Save the data
                instance.pm.save();
            }
        });
        moveDownBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Get the selected item and check if any item is selected
            	if(table.getSelectedRows().length <= 0)
            		return;
            	
            	// Make sure the last selected item is not the last item in the list
            	if(table.getSelectedRows()[table.getSelectedRowCount() - 1] >= table.getRowCount() - 1)
            		return;
            	
            	// Move each item one up
            	for(int i = table.getSelectedRows().length - 1; i >= 0; i--) {
            		instance.pm.setProfiles(ListUtils.moveItemDown(instance.pm.getProfiles(), table.getSelectedRows()[i]));
            	}
            	
            	// Set the selected rows
            	if(table.getSelectedRows()[table.getSelectedRowCount() - 1] <  table.getRowCount() - 1)
            		table.setRowSelectionInterval(table.getSelectedRows()[0] + 1, table.getSelectedRows()[table.getSelectedRowCount() - 1] + 1);
                
                // Update the table
                table.updateUI();
                
                // Save the data
                instance.pm.save();
            }
        });
        delBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Get the selected item and check if any item is selected
            	int rowsIndex[] = table.getSelectedRows();
            	if(rowsIndex.length == 0)
            		return;
            	
            	// Remove all selected rows
            	for(int i = rowsIndex.length - 1; i >= 0; i--) {
                	
                	// Make sure the default profile is not going to be deleted
                	if(instance.pm.getProfileByIndex(rowsIndex[i]) instanceof DefaultProfile)
                		continue;
                	
                	// Remove the row
            		instance.pm.removeProfileByIndex(rowsIndex[i]);
            	}
                
                // Update the table and the buttons
                table.updateUI();
                updateButtons();
                
                // Save the data
                instance.pm.save();
            }
        });
        
        pnl.add(btnsPanel);
        pnl.setPreferredSize(new Dimension(btnsPanel.getPreferredSize().width, 400));
        
        return pnl;
	}
	
	public void updateButtons() {
		int rows[] = this.table.getSelectedRows();
		
		if(rows.length == 0) {
			editBtn.setEnabled(false);
			delBtn.setEnabled(false);
			moveUpBtn.setEnabled(false);
			moveDownBtn.setEnabled(false);
			
		} else if(rows.length == 1) {
			
			final boolean defSelected = (this.pm.getProfileByIndex(rows[0]) instanceof DefaultProfile);
			editBtn.setEnabled(!defSelected);
			delBtn.setEnabled(!defSelected);
			moveUpBtn.setEnabled(!(rows[0] <= 0));
			moveDownBtn.setEnabled(!(rows[rows.length - 1] >= (this.table.getRowCount() - 1)));
			
		} else {
			
			boolean onlyDefSelected = true;
			for(int i = 0; i < rows.length; i++) {
				if(!(this.pm.getProfileByIndex(rows[i]) instanceof DefaultProfile)) {
					onlyDefSelected = false;
					break;
				}
			}
			
			editBtn.setEnabled(false);
			delBtn.setEnabled(!onlyDefSelected);
			moveUpBtn.setEnabled(!(rows[0] <= 0));
			moveDownBtn.setEnabled(!(rows[rows.length - 1] >= (this.table.getRowCount() - 1)));
		}
	}
}
