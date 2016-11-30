package com.rp.apim.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rp.rest.jersey.client.JerseyRestClient;

public class ApiClient {
	static final String apiManagerBasePath = "https://api.apim.ibmcloud.com/robertopozziitibmcom-dev/sb";
	static final String clientID = "a000837b-c964-4000-a2e0-1c0f952c3d56";
	static final String clientSecret = "P5qV1tT2mX8bK1hV5hT8jB5xI0oP0mD5lQ7wC3nW5vH1aR6jK8";

	public static void main(String[] args) {
		_callApiItemsNoTLS();
		_callApiItemsTLS();
		_callApiComuneBologna();
		_callApiCantieri("riqurbana");
	}

	/*
	 * Calls an API exposed on API Manager in Bluemix cloud 
	 * This API: 
	 * - requires Identification with clientId and clientSecret (passed as Headers) 
	 * - requires no Authentication 
	 * - calls a REST service running on local workstation exposed via Bluemix Secure Gateway; access is not secured by any TLS certificate
	 * 
	 * REST Service is implemented as a NodeJs application
	 */
	private static void _callApiItemsNoTLS() {
		System.out.println("$$$$$$$$$$$$$$$$$ Calling Api Items Management (through Secure Gateway no TLS) - START $$$$$$$$$$$$$$$$$");
		try {
			String resourcePath = "/items";
			String path = apiManagerBasePath + resourcePath;
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("X-IBM-Client-Id", clientID);
			headers.put("X-IBM-Client-Secret", clientSecret);
			String jsonStr = new JerseyRestClient().get(path, headers);
			System.out.println(jsonStr);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONObject jsonObj = (JSONObject) json.parse(jsonStr);
			JSONArray jsonArray = (JSONArray) jsonObj.get("rows");
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject o = (JSONObject) iterator.next();
				String obj = o.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println("$$$$$$$$$$$$$$$$$ Calling Api Items Management (through Secure Gateway no TLS) - END $$$$$$$$$$$$$$$$$");
		}
	}
	
	/*
	 * Calls an API exposed on API Manager in Bluemix cloud 
	 * This API: 
	 * - requires Identification with clientId and clientSecret (passed as Headers) 
	 * - requires no Authentication 
	 * - calls a REST service running on local workstation exposed via Bluemix Secure Gateway; access is secured by a TLS certificate for Mutual Authentication
	 * 
	 * REST Service is implemented as a NodeJs application
	 */
	private static void _callApiItemsTLS() {
		System.out.println("################# Calling Api Items Management (through Secure Gateway with TLS Mutual Authentication) - START #################");
		try {
			String resourcePath = "/secure/items";
			String path = apiManagerBasePath + resourcePath;
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("X-IBM-Client-Id", clientID);
			headers.put("X-IBM-Client-Secret", clientSecret);
			String jsonStr = new JerseyRestClient().get(path, headers);
			System.out.println(jsonStr);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONObject jsonObj = (JSONObject) json.parse(jsonStr);
			JSONArray jsonArray = (JSONArray) jsonObj.get("rows");
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject o = (JSONObject) iterator.next();
				String obj = o.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println("################# Calling Api Items Management (through Secure Gateway with TLS Mutual Authentication) - END #################");
		}
	}

	/*
	 * Calls an API exposed on API Manager in Bluemix cloud.
	 * This API: 
	 * - requires Identification with clientId and clientSecret (passed as Headers) 
	 * - requires no Authentication 
	 * - calls a REST service exposed by Bluemix application Bologna-Cantieri-mobile; access is not secured by any TLS certificate
	 */
	private static void _callApiComuneBologna() {
		System.out.println("$$$$$$$$$$$$$$$$$ Calling Api Comune di Bologna - START $$$$$$$$$$$$$$$$$");
		try {
			String resourcePath = "/comunebo/cantieri/riqurbana";
			String path = apiManagerBasePath + resourcePath;
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("X-IBM-Client-Id", clientID);
			headers.put("X-IBM-Client-Secret", clientSecret);
			String jsonStr = new JerseyRestClient().get(path, headers);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONArray jsonArray = (JSONArray) json.parse(jsonStr);
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = (JSONObject) iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println("$$$$$$$$$$$$$$$$$ Calling Api Comune di Bologna - END $$$$$$$$$$$$$$$$$");
		}
	}

	/*
	 * Calls an API exposed on API Manager in Bluemix cloud.
	 * This API: 
	 * - requires Identification with clientId and clientSecret (passed as Headers)
	 * - requires no Authentication 
	 * - calls a REST service running on local workstation exposed via Bluemix Secure Gateway; access is secured by a TLS certificate for Mutual Authentication
	 * 
	 * REST Service is implemented as a WAS Liberty application
	 */
	private static void _callApiCantieri(String tipoCantiere) {
		System.out.println(
				"################## Calling Api Cantieri (through Secure Gateway with TLS Mutual Authentication) - START ##################");
		try {
			String resourcePath = "/cantieri/" + tipoCantiere;
			String path = apiManagerBasePath + resourcePath;
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("X-IBM-Client-Id", clientID);
			headers.put("X-IBM-Client-Secret", clientSecret);
			String jsonStr = new JerseyRestClient().get(path, headers);
			// Parsing and printing result
			JSONParser json = new JSONParser();
			JSONArray jsonArray = (JSONArray) json.parse(jsonStr);
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = (JSONObject) iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println(
					"################## Calling Api Cantieri (through Secure Gateway with TLS Mutual Authentication) - END ##################");
		}
	}

	private static void _callUserManagement() {
		System.out.println("================ Calling REST UserManagement - START ================");

		/*
		 * "secret": "XgPtjq2FzZ", "tokenEndpointUrl":
		 * "https://rp-auth-alvfb7i8b6-csv4.iam.ibmcloud.com/idaas/oidc/endpoint/default/token",
		 * "authorizationEndpointUrl":
		 * "https://rp-auth-alvfb7i8b6-csv4.iam.ibmcloud.com/idaas/oidc/endpoint/default/authorize",
		 * "issuerIdentifier": "rp-auth-alvfb7i8b6-csv4.iam.ibmcloud.com",
		 * "clientId": "Kw0orrrKhc",
		 */

		// String path = "https://rpozzi-nodejs.mybluemix.net/users/";
		// String path =
		// "https://rp-auth-alvfb7i8b6-csv4.iam.ibmcloud.com/idaas/oidc/endpoint/default/token";
		String path = "https://rp-auth-alvfb7i8b6-csv4.iam.ibmcloud.com/idaas/oidc/endpoint/default/authorize?client_id=Kw0orrrKhc";
		// create the rest client instance
		Client client = ClientBuilder.newClient();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(path);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		// perform a GET on the resource.
		Response response = invocationBuilder.get();
		String jsonStr = response.readEntity(String.class);
		System.out.println(jsonStr);

		MultivaluedMap<String, Object> headers = (MultivaluedMap<String, Object>) response.getHeaders();
		Set<String> keys = headers.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.println("---> " + key);
			List<Object> list = headers.get(key);
			for (Iterator<Object> iterator2 = list.iterator(); iterator2.hasNext();) {
				Object s = iterator2.next();
				System.out.println("   ===> " + s);
			}
		}

		System.out.println("================ Calling REST UserManagement - END ================");
	}

	private static void _callApiUserManagement() {
		System.out.println("***************** Calling Api UserManagement - START *****************");
		try {
			String resourcePath = "/users/getAllUsers";
			String path = apiManagerBasePath + resourcePath;
			System.out.println("Calling through path " + path);

			path += "?client_id=" + clientID + "&client_secret=" + clientSecret;

			// create the rest client instance
			Client client = ClientBuilder.newClient();
			// create the Builder instance to interact with
			WebTarget webTarget = client.target(path);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			// perform a GET on the resource.
			Response response = invocationBuilder.get();
			String jsonStr = response.readEntity(String.class);
			System.out.println(jsonStr);

			/*
			 * JSONParser json = new JSONParser(); JSONArray obj = null; try {
			 * obj = (JSONArray) json.parse(jsonStr); } catch (ParseException e)
			 * { e.printStackTrace(); } for (Iterator<JSONObject> iterator =
			 * obj.iterator(); iterator.hasNext();) { JSONObject jsonObj =
			 * (JSONObject) iterator.next(); jsonObj.keySet(); }
			 */
		} finally {
			System.out.println("***************** Calling Api UserManagement - END *****************");
		}
	}

}