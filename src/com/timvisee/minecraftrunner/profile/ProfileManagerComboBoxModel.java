package com.timvisee.minecraftrunner.profile;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ProfileManagerComboBoxModel implements ComboBoxModel<Profile> {

	private ProfileManager pm;
	
    private List<ListDataListener> listeners = new ArrayList<ListDataListener>();
	
    private Object selected = null;
    
	public ProfileManagerComboBoxModel(ProfileManager pm) {
		this.pm = pm;
	}

    public Profile getElementAt(int index) {
        return this.pm.getProfileByIndex(index);
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
		return this.pm.getProfilesCount();
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
