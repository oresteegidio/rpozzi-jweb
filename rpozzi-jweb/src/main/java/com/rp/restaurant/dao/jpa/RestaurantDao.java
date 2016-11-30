package com.rp.restaurant.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import com.rp.restaurant.model.Restaurant;

public class RestaurantDao {
	private EntityManager em;

	public RestaurantDao(EntityManager em) {
		super();
		this.em = em;
	}
	
	public Restaurant getRestaurantById(int id) throws Exception {
		Restaurant restaurant = em.find(Restaurant.class, id);
		return restaurant;
	}
	
	public List<Restaurant> getRestaurantList() throws Exception {
		System.out.println("********* START - RestaurantDao.getRestaurantList()");
		List<Restaurant> list = em.createQuery("SELECT c FROM Restaurant c").getResultList();
		System.out.println("********* END - RestaurantDao.getRestaurantList()");
		return list;
	}
	
	public void create(Restaurant restaurant) {
		System.out.println("Inserting new restaurant: " + restaurant.getId() + " - " + restaurant.getName() + " ...");
		try {
			em.persist(restaurant);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void setEm(EntityManager em) {
		this.em = em;
	}	
}