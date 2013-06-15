package com.timvisee.minecraftrunner.player;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class PlayerManagerComboBoxModel implements ComboBoxModel<Player> {

	private PlayerManager pm;
	
    private List<ListDataListener> listeners = new ArrayList<ListDataListener>();
	
    private Object selected;
    
	public PlayerManagerComboBoxModel(PlayerManager pm) {
		this.pm = pm;
	}

    public Player getElementAt(int index) {
        return this.pm.getPlayer(index);
    }

	@Override
	public void addListDataListener(ListDataListener l) {
		this.listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		this.listeners.remove(l);
	}

	@Override
	public int getSize() {
		return this.pm.getPlayersCount();
	}

	@Override
	public Object getSelectedItem() {
		return this.selected;
	}

	@Override
	public void setSelectedItem(Object item) {
		this.selected = item;
	}
}
