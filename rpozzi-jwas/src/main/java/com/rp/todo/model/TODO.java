package com.rp.todo.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TODOLIST")
/*
 * define O-R mapping of todolist table
 */
public class TODO {
	@Id
	// primary key
	@Column(name = "L_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	@Basic
	@Column(name = "C_NAME")
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