package com.rp.todo.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;

import com.rp.jpa.JpaFactory;
import com.rp.todo.model.TODO;

//@Path("/todolist")
/**
 * CRUD service of todo list table. It uses REST style.
 *
 */
public class TODOListResource {
	private EntityManager em = JpaFactory.getInstance().createEntityManager();

	//@POST
	public Response create(@FormParam("name") String name, @FormParam("age") int age) {
		TODO todo = new TODO();
		todo.setName(name);
		em.getTransaction().begin();
		em.persist(todo);
		em.getTransaction().commit();
		return Response.ok(todo.toString()).build();
	}

	//@DELETE
	public Response delete(@QueryParam("id") long id) {
		TODO todo = em.find(TODO.class, id);
		if (todo != null) {
			em.getTransaction().begin();
			em.remove(todo);
			em.getTransaction().commit();
			return Response.ok().build();
		} else
			return Response.status(Status.NOT_FOUND).build();
	}

	//@PUT
	public Response update(@FormParam("id") long id, @FormParam("name") String name, @FormParam("age") int age) {
		TODO todo = em.find(TODO.class, id);
		if (todo != null) {
			todo.setName(name);
			// TODO check if null
			em.getTransaction().begin();
			em.refresh(todo);
			em.getTransaction().commit();
			return Response.ok().build();
		} else
			return Response.status(Status.NOT_FOUND).build();
	}

	/*@GET
	@Produces(MediaType.APPLICATION_JSON)*/
	public Response get(@QueryParam("id") long id) throws Exception {
		String response = null;
		ObjectMapper mapper = new ObjectMapper();
		if (id == 0) {
			List<TODO> list = em.createQuery("SELECT t FROM TODO t").getResultList();
			try {
				response = mapper.writeValueAsString(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.ok(response).build();
		}
		TODO todo = em.find(TODO.class, id);
		if (todo != null){
			try {
				response = mapper.writeValueAsString(todo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.ok(response).build();
		}
		else
			return Response.status(Status.NOT_FOUND).build();
	}

}