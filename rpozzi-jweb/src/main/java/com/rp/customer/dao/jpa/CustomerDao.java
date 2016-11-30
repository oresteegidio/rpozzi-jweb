package com.rp.customer.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import com.rp.customer.model.Customer;

public class CustomerDao {
	private EntityManager em;

	public CustomerDao(EntityManager em) {
		super();
		this.em = em;
	}
	
	public Customer getCustomerById(long id) throws Exception {
		Customer customer = em.find(Customer.class, id);
		return customer;
	}
	
	public List<Customer> getCustomerList() throws Exception {
		List<Customer> list = em.createQuery("SELECT c FROM Customer c").getResultList();
		return list;
	}

	public void create(Customer customer) {
		System.out.println("Insert new customer: " + customer.getName());
		try {
			em.persist(customer);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void remove(Customer customer) {
		System.out.println("Delete customer by id : " + customer.getId());
		try {
			Customer client = em.merge(customer);
			em.remove(client);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void setEm(EntityManager em) {
		this.em = em;
	}	
}