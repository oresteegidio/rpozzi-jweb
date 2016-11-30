package com.rp.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JpaFactory {
	private static JpaFactory _theInstance = null;

	public JpaFactory() {
		super();
	}

	public static JpaFactory getInstance() {
		if (_theInstance == null) {
			_theInstance = new JpaFactory();
		}
		return _theInstance;
	}
	
	public EntityManager createEntityManager() {
		System.out.println("********* START - JpaFactory.createEntityManager()");
		EntityManager em = null;
		InitialContext ic;
		try {
			System.out.println("######### Initializing InitialContext ...");
			ic = new InitialContext();
			System.out.println("######### InitialContext initialized : " + ic);
			System.out.println("######### Looking Up EntityManager ...");
			em = (EntityManager) ic.lookup("java:comp/env/jpa/entitymanager");
			System.out.println("######### EntityManager created : " + em);
			//em = _createEntityManager();
		} catch (NamingException e) {
			e.printStackTrace();
			//em = _createEntityManager();
		}
		System.out.println("********* END - JpaFactory.createEntityManager()");
		return em;
	}
	
	public UserTransaction getUserTransaction() {
		InitialContext ic;
		try {
			ic = new InitialContext();
			return (UserTransaction) ic.lookup("java:comp/UserTransaction");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private EntityManager _createEntityManager() {
		String driver = "";
		String user = "";
		String password = "";
		String databaseUrl = "";
		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
		// Parse it to obtain the for DB connection info
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		if (VCAP_SERVICES != null) {
			System.out.println("JpaFactory.createEntityManager() - VCAP_SERVICES content: " + VCAP_SERVICES);
			// parse the VCAP JSON structure
			JSONParser json = new JSONParser();
			JSONObject obj = null;
			try {
				obj = (JSONObject) json.parse(VCAP_SERVICES);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String thekey = null;
			Set<String> keys = obj.keySet();
			System.out.println("Searching through VCAP keys");
			// Look for the VCAP key that holds the SQLDB information
			for (String eachkey : keys) {
				System.out.println("Key is: " + eachkey);
				if (eachkey.contains("sqldb")) {
					thekey = eachkey;
				}
			}
			if (thekey == null) {
				System.out.println("Cannot find any SQLDB service in the VCAP; exiting");
			}

			JSONArray list = (JSONArray) obj.get(thekey);
			obj = (JSONObject) list.get(0);
			String name = (String) obj.get("name");
			System.out.println("Service found: " + obj.get("name"));
			obj = (JSONObject) obj.get("credentials");
			
			String databaseHost = (String) obj.get("host");
			String databaseName = (String) obj.get("db");
			Long port = (Long) obj.get("port");
			user = (String) obj.get("username");
			password = (String) obj.get("password");
			databaseUrl = "jdbc:db2://" + databaseHost + ":" + port + "/" + databaseName;
			driver = "com.ibm.db2.jcc.DB2Driver";
			System.out.println(databaseUrl);
		} else {
			System.out.println("VCAP_SERVICES is null. Fallback to connect using following parameters:");
			driver = "com.myqsl.jdbc.Driver";
			databaseUrl = "jdbc:mysql://192.168.99.100:3307/rpmysql?relaxAutoCommit=true";
			//databaseUrl = "jdbc:mysql://cap-sg-prd-3.integration.ibmcloud.com:15550/rpmysql?relaxAutoCommit=true";
			user = "root";
			password = "robi";
			System.out.println("	Driver = " + driver);
			System.out.println("	Database URL = " + databaseUrl);
			System.out.println("	Username = " + user);
			System.out.println("	Password = " + password);
		}

		// programmatically create a JPA EntityManager
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("javax.persistence.jdbc.driver", driver);
		properties.put("javax.persistence.jdbc.url", databaseUrl);
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);
		// "customer" was defined at persistence.xml
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("restaurant", properties);
		return factory.createEntityManager();
	}
}