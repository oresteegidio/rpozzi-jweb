package com.rp.restaurant.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rp.jpa.JpaFactory;
import com.rp.rest.client.RestClient;
import com.rp.restaurant.dao.jpa.RestaurantDao;
import com.rp.restaurant.model.Restaurant;
import com.rp.todo.dao.jpa.TodoListDao;
import com.rp.todo.model.TODO;

@Path("/restaurants")
public class RestaurantService {
	private EntityManager em = JpaFactory.getInstance().createEntityManager();
	private UserTransaction utx = JpaFactory.getInstance().getUserTransaction();
	private RestaurantDao restaurantDao = new RestaurantDao(em);
	private TodoListDao todoListDao = new TodoListDao(em);
	// private String itemsEndpoint = "http://localhost:3001/items";
	// private String itemsEndpoint = "http://192.168.99.100:3001/items";
	private String itemsEndpoint = "http://cap-sg-prd-3.integration.ibmcloud.com:15602/items";

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant getRestaurantById(@PathParam("id") int id) {
		System.out.println("********* START - RestaurantService.getRestaurantById()");
		System.out.println("######### ID = " + id);
		Restaurant restaurant = null;
		try {
			restaurant = restaurantDao.getRestaurantById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("********* END - RestaurantService.getRestaurantById()");
		return restaurant;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurants() {
		System.out.println("********* START - RestaurantService.getRestaurants()");
		List<Restaurant> list = null;
		try {
			list = restaurantDao.getRestaurantList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("********* END - RestaurantService.getRestaurants()");
		return list;
	}

	@GET
	@Path("/jdbc")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurantsJdbc() {
		System.out.println("********* START - RestaurantService.getRestaurantsJdbc()");
		String jndiName = "jdbc/rpozziRestaurants";
		List<Restaurant> list = null;
		DataSource ds = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			InitialContext ctx = new InitialContext();
			System.out.println("######### Looking up jndiName = " + jndiName + " ...");
			ds = (DataSource) ctx.lookup(jndiName);
			System.out.println("######### Got Datasource : " + ds);
			System.out.println("######### Getting Connection ...");
			connection = ds.getConnection();
			System.out.println("######### Got Connection : " + connection);
			String sqlQueryAll = "SELECT * FROM RESTAURANTS";
			System.out.println("######### Preparing Statement with sql : " + sqlQueryAll);
			stmt = connection.prepareStatement(sqlQueryAll);
			System.out.println("######### Got Statement : " + stmt);
			System.out.println("######### Executing query ...");
			rs = stmt.executeQuery();
			list = new ArrayList<>();
			while (rs.next()) {
				System.out.println("ID = " + rs.getString(1) + " - NAME = " + rs.getString(2));
				Restaurant restaurant = new Restaurant();
				restaurant.setId((new Integer(rs.getString(1))).intValue());
				restaurant.setName(rs.getString(2));
				list.add(restaurant);
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("********* END - RestaurantService.getRestaurantsJdbc()");
		return list;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant insert(final MyJaxBean in) {
		System.out.println("********* START - RestaurantService.insert()");
		System.out.println("######### RESTAURANT ID = " + in.getRestaurantId());
		System.out.println("######### TODO ID = " + in.getTodoId());
		Restaurant restaurant = new Restaurant();
		TODO todo = new TODO();
		try {
			// SET RESTAURANT TO INSERT
			restaurant.setId(in.getRestaurantId());
			restaurant.setName("Another Restaurant");
			// SET TODOLIST TO INSERT
			todo.setId(in.getTodoId());
			todo.setName("ANOTHER TODO");
			// START TRANSACTION
			utx.begin();
			restaurantDao.create(restaurant);
			todoListDao.create(todo);
			utx.commit();
			System.out.println("Restaurant ID : " + restaurant.getId() + " - Restaurant Name : " + restaurant.getName());
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
		System.out.println("********* END - RestaurantService.insert()");
		return restaurant;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/jdbc")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant insertJdbc(final MyJaxBean in) {
		System.out.println("********* START - RestaurantService.insertJdbc()");
		System.out.println("######### RESTAURANT ID = " + in.getRestaurantId());
		System.out.println("######### TODO ID = " + in.getTodoId());
		System.out.println("######### DB TYPE = " + in.getDbType());
		String jndiName = getJndiName(in.getDbType());
		DataSource ds = null;
		Connection connection = null;
		PreparedStatement stmtRestaurants = null;
		PreparedStatement stmtTodolist = null;
		Restaurant restaurant = new Restaurant();
		restaurant.setId(in.getRestaurantId());
		restaurant.setName("New Restaurant");
		TODO todo = new TODO();
		todo.setId(in.getTodoId());
		todo.setName("New Todo");
		try {
			InitialContext ctx = new InitialContext();
			System.out.println("######### Looking up jndiName = java:comp/env/" + jndiName + " ...");
			ds = (DataSource) ctx.lookup("java:comp/env/" + jndiName);
			System.out.println("######### Got Datasource : " + ds);
			System.out.println("######### Getting Connection ...");
			connection = ds.getConnection();
			connection.setAutoCommit(false);
			System.out.println("######### Got Connection : " + connection);
			/*########### INSERT INTO RESTAURANT - START ###########*/
			String sqlRestaurantInsert = "INSERT INTO RESTAURANTS (ID, RESTNAME) VALUES (?, ?)";
			System.out.println("######### Preparing Statement with sql : " + sqlRestaurantInsert);
			stmtRestaurants = connection.prepareStatement(sqlRestaurantInsert);
			stmtRestaurants.setInt(1, restaurant.getId());
			stmtRestaurants.setString(2, restaurant.getName());
			System.out.println("######### Got Statement : " + stmtRestaurants);
			System.out.println("######### Executing insert into RESTAURANTS...");
			int i = stmtRestaurants.executeUpdate();
			System.out.println("######### Insert into RESTAURANTS executed : " + i);
			/*########### INSERT INTO RESTAURANT - END ###########*/
			/*=========== INSERT INTO TODOLIST - START ===========*/
			String sqlTodolistInsert = "INSERT INTO TODOLIST (L_ID, C_NAME) VALUES (?, ?)";
			System.out.println("######### Preparing Statement with sql : " + sqlTodolistInsert);
			stmtTodolist = connection.prepareStatement(sqlTodolistInsert);
			stmtTodolist.setLong(1, todo.getId());
			stmtTodolist.setString(2, todo.getName());
			System.out.println("######### Got Statement : " + stmtTodolist);
			System.out.println("######### Executing insert into TODOLIST...");
			int j = stmtTodolist.executeUpdate();
			System.out.println("######### Insert into TODOLIST executed : " + j);
			/*=========== INSERT INTO TODOLIST - END ===========*/
			connection.commit();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (stmtRestaurants != null) {
				try {
					stmtRestaurants.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmtTodolist != null) {
				try {
					stmtTodolist.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("********* END - RestaurantService.insertJdbc()");
		return restaurant;
	}

	@GET
	@Path("/simulate")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getSimulatedRestaurants() {
		System.out.println("********* START - RestaurantService.getSimulatedRestaurants()");
		List<Restaurant> list = null;
		try {
			Restaurant r1 = new Restaurant();
			r1.setId(1);
			r1.setName("RISTORANTE 1");
			Restaurant r2 = new Restaurant();
			r2.setId(2);
			r2.setName("RISTORANTE 2");
			list = new ArrayList<>();
			list.add(r1);
			list.add(r2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("********* END - RestaurantService.getSimulatedRestaurants()");
		return list;
	}

	@GET
	@Path("/items")
	@Produces(MediaType.APPLICATION_JSON)
	public String getItems() {
		RestClient client = new RestClient(itemsEndpoint);
		String s = client.GETinvoke();
		return s;
	}
	
	private String getJndiName(String dbType) {
		String jndiName = "jdbc/rpozziRestaurants";
		if (dbType != null && dbType.equals("db2")) {
			jndiName = "jdbc/rpozziRestaurantsDB2";
		}
		return jndiName;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public void setRestaurantDao(RestaurantDao restaurantDao) {
		this.restaurantDao = restaurantDao;
	}

	public void setTodoListDao(TodoListDao todoListDao) {
		this.todoListDao = todoListDao;
	}
}