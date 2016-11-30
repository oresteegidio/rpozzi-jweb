package com.rp.restaurant.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RESTAURANTS")
/*
 * define O-R mapping of RESTAURANTS table
 */
public class Restaurant {
	@Id
	// primary key
	@Column(name = "ID")
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@SequenceGenerator(name = "SEQ", sequenceName = "OPENJPA_SEQUENCE_TABLE", allocationSize = 1)
	int id;
	@Basic
	@Column(name = "RESTNAME")
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int pk) {
		id = pk;
	}
	
}