package com.timvisee.minecraftrunner.player;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class PlayerManagerTableModel implements TableModel {

	private PlayerManager pm;
	
    private EventListenerList listeners = new EventListenerList();
	
	public PlayerManagerTableModel(PlayerManager pm) {
		this.pm = pm;
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return this.pm.getPlayersCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			Player p = this.pm.getPlayer(rowIndex);
			
			if(p.hasAlias())
				return p.getAlias();
			return p.getLogin();
			
		case 1:
			return this.pm.getPlayer(rowIndex).getLogin();
			
		default:
			return null;
		}
	}
	
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return "Alias";
            
        case 1:
            return "Login";
            
        default:
            return null;
        }
    }

    public Player getElementAt(int index) {
        return this.pm.getPlayer(index);
    }

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(TableModelListener.class, l);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		switch(arg0) {
		case 0:
		case 1:
			return String.class;
			
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return true;
			
		default:
			return false;
		}
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove(TableModelListener.class, l);
	}

	@Override
	public void setValueAt(Object val, int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			try {
				final String alias = (String) val;
				Player p = this.pm.getPlayer(rowIndex);
				
				// If the alias is an empty string, reset the alias
				if(alias.trim().equals(""))
					p.resetAlias();
				else
					p.setAlias(alias);
				
				// Save the updated player data
				this.pm.save();
				
			} catch(ClassCastException e) {
				return;
			}
			break;
		
		default:
		}
	}
}
