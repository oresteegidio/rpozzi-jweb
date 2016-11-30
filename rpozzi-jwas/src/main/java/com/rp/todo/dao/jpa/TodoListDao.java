package com.rp.todo.dao.jpa;

import javax.persistence.EntityManager;

import com.rp.todo.model.TODO;

public class TodoListDao {
	private EntityManager em;

	public TodoListDao(EntityManager em) {
		super();
		this.em = em;
	}

	public void create(TODO todo) {
		System.out.println("Inserting new Todo: " + todo.getName() + " ...");
		try {
			em.persist(todo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void remove(TODO todo) {
		System.out.println("Deleting todo by id : " + todo.getId() + " ...");
		try {
			todo = em.merge(todo);
			em.remove(todo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
}