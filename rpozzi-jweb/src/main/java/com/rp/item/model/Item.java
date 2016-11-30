package com.rp.item.model;


public class Item {
	long id;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long pk) {
		id = pk;
	}
	
}