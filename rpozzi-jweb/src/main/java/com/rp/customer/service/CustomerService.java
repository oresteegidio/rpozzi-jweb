package com.rp.customer.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rp.customer.dao.jpa.CustomerDao;
import com.rp.customer.model.Customer;
import com.rp.jpa.JpaFactory;
import com.rp.todo.dao.jpa.TodoListDao;
import com.rp.todo.model.TODO;

@Path("/customers")
public class CustomerService {
	private EntityManager em = JpaFactory.getInstance().createEntityManager();
	private UserTransaction utx = JpaFactory.getInstance().getUserTransaction();
	private CustomerDao customerDao = new CustomerDao(em);
	private TodoListDao todoListDao = new TodoListDao(em);

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomerById(@PathParam("id") long id) {
		Customer customer = null;
		try {
			customer = customerDao.getCustomerById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> getCustomers() {
		List<Customer> list = null;
		try {
			list = customerDao.getCustomerList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@GET
	@Path("/provinces/{prov}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomersByProv(@PathParam("prov") String prov) {
		CustomerRestClient restCli = new CustomerRestClient();
		String s = restCli.getCustomersByProv(prov);
		return s;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Customer insert() {
		Customer customer = new Customer();
		TODO todo = new TODO();
		try {
			// SET CUSTOMER TO INSERT
			//customer.setId(1);
			customer.setName("Test");
			// SET TODOLIST TO INSERT
			todo.setId(10);
			todo.setName("PIPPO PLUTO");
			// START TRANSACTION
			utx.begin();
			customerDao.create(customer);
			//todoListDao.create(todo);
			utx.commit();
			System.out.println("Customer ID : " + customer.getId() + " - Customer Name : " + customer.getName());
			System.out.println("TODO ID : " + todo.getId() + " - TODO Name : " + todo.getName());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (utx.getStatus() == javax.transaction.Status.STATUS_ACTIVE) {
					utx.rollback();
				}
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		}
		return customer;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Customer deleteById(@FormParam("customerId") long customerId, @FormParam("todoId") long todoId) {
		Customer customer = new Customer();
		TODO todo = new TODO();
		try {
			// SET CUSTOMER TO DELETE
			customer.setId(customerId);
			// SET TODOLIST TO DELETE
			todo.setId(todoId);
			System.out.println("CustomerId : " + customerId);
			System.out.println("TodoId : " + todoId);
			// START TRANSACTION
			utx.begin();
			customerDao.remove(customer);
			todoListDao.remove(todo);
			utx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (utx.getStatus() == javax.transaction.Status.STATUS_ACTIVE) {
					utx.rollback();
				}
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		}
		return customer;
	}
}