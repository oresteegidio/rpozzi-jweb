package com.rp.configuration;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//This class define the /hello RESTful API to fetch the database service information
@Path("/dbinfo")
public class DbInfoResource {

	@GET
	public String getInformation() {
		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
		String vcapServices = System.getenv("VCAP_SERVICES");
		if (vcapServices == null) {
			return ("VCAP_SERVICES not found");
		}
		System.out.println("VCAP_SERVICES = " + vcapServices);		
		JSONParser json = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) json.parse(vcapServices);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String thekey = null;
		Set<String> keys = obj.keySet();
		System.out.println("Searching through VCAP keys");
		// Look for the VCAP key that holds the SQLDB information
		for (String eachkey : keys) {
			if (eachkey.contains("sqldb")) {
				thekey = eachkey;
			}
		}
		if (thekey == null) {
			return ("Cannot find any SQLDB service in the VCAP_SERVICES");
		}
		
		JSONArray list = (JSONArray) obj.get(thekey);
		obj = (JSONObject) list.get(0);
		String name = (String) obj.get("name");
		obj = (JSONObject) obj.get("credentials");
		String databaseHost = (String) obj.get("host");
		String databaseName = (String) obj.get("db");
		Long port = (Long) obj.get("port");
		String jdbcurl = (String) obj.get("jdbcurl");

		JSONObject DBInfoObj = new JSONObject();
		DBInfoObj.put("name", name);
		DBInfoObj.put("host", databaseHost);
		DBInfoObj.put("db", databaseName);
		DBInfoObj.put("port", port);
		DBInfoObj.put("jdbcurl", jdbcurl);
		
		return DBInfoObj.toString();
	}
}