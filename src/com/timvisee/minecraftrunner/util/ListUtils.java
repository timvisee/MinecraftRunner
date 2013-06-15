package com.timvisee.minecraftrunner.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

	/**
	 * Move the item of a List one up
	 * @param items List with items
	 * @param itemIndex Item index to move one up
	 * @return New List
	 */
	public static <T> List<T> moveItemUp(List<T> items, int itemIndex) {
		return moveItem(items, itemIndex, itemIndex - 1);
	}
	
	/**
	 * Move the item of a List one up
	 * @param items List with items
	 * @param itemIndex Item index to move one down
	 * @return New List
	 */
	public static <T> List<T> moveItemDown(List<T> items, int itemIndex) {
		return moveItem(items, itemIndex, itemIndex + 1);
	}
	
	/**
	 * Move an item of a List to a different position
	 * @param items List with items
	 * @param itemIndex Item index to move
	 * @param newItemIndex Item index to move the item to
	 * @return New List
	 */
	public static <T> List<T> moveItem(List<T> items, int itemIndex, int newItemIndex) {
		// Make sure the items list is not null
		if(items == null)
			return null;
		
		// Make sure the list contains at least 2 items
		if(items.size() < 2)
			return items;
		
		// Make sure the index is not out of bounds
		if(itemIndex >= items.size() || newItemIndex >= items.size() ||
				itemIndex < 0 || newItemIndex < 0)
			return items;
		
		// The two indexes have to be different
		if(itemIndex == newItemIndex)
			return items;
		
		// Store the item and all the other items into a different list
		T item = items.get(itemIndex);
		List<T> others = new ArrayList<T>();
		others.addAll(items);
		others.remove(itemIndex);
		
		// Build the the new list
		List<T> newList = new ArrayList<T>(items.size());
		newList.addAll(others.subList(0, newItemIndex));
		newList.add(item);
		newList.addAll(others.subList(newItemIndex, others.size()));
		
		// Return the new list
		return newList;
	}
}
