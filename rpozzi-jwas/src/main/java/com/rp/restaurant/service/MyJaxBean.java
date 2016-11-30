package com.rp.restaurant.service;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyJaxBean {
    @XmlElement private int restaurantId;
    @XmlElement private int todoId;
    @XmlElement private String dbType;
    
	public int getRestaurantId() {
		return restaurantId;
	}
	public int getTodoId() {
		return todoId;
	}
	public String getDbType() {
		return dbType;
	}
}