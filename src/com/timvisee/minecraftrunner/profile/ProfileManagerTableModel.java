package com.timvisee.minecraftrunner.profile;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ProfileManagerTableModel implements TableModel {
	
	private ProfileManager pm;
	
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    
	public ProfileManagerTableModel(ProfileManager pm) {
		this.pm = pm;
	}

    public Profile getElementAt(int index) {
        return this.pm.getProfileByIndex(index);
    }

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex) {
		case 0:
			return "Profile";
			
		case 1:
			return "Path";
		
		default:
			return null;
		}
	}

	@Override
	public int getRowCount() {
		return this.pm.getProfilesCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Get the profile
		Profile p = this.pm.getProfileByIndex(rowIndex);
		
		switch(columnIndex) {
		case 0:
			return p.getName();
			
		case 1:
			return p.getDirectoryPath();
			
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return !(this.pm.getProfileByIndex(rowIndex) instanceof DefaultProfile);
			
		default:
			return false;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// Get the profile
		Profile p = this.pm.getProfileByIndex(rowIndex);
		
		switch(columnIndex) {
		case 0:
			try {
				p.setName((String) aValue);
			} catch(ClassCastException e) { }
			break;
			
		default:
			return;
		}
	}
}
