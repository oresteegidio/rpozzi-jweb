package com.rp.rest.jersey.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JerseyRestClient {
	
	private String keystorePath;
	private String keystorePassword;
	private String truststorePath;
	private String truststorePassword;
	
	public JerseyRestClient() {
		super();
	}

	public JerseyRestClient(String keystorePath, String keystorePassword, String truststorePath, String truststorePassword) {
		super();
		this.keystorePath = keystorePath;
		this.keystorePassword = keystorePassword;
		this.truststorePath = truststorePath;
		this.truststorePassword = truststorePassword;
	}

	public static void main(String[] args) {
		_GETNoAuth();
		_GETHttpBasicAuth();
	}
	
	public String get(String path, HashMap<String, String> headers) {
		return get(path, headers, false, false);
	}
	
	public String get(String path, HashMap<String, String> headers, boolean authNeeded, boolean isSSL) {
		String jsonStr = null;
		if (!authNeeded && !isSSL) {
			jsonStr = callREST_GET_NoAuth_NoSSL(path, headers);
		}
		return jsonStr;
	}
	
	public String callREST_GET_NoAuth_NoSSL(String path, HashMap<String, String> headers) {
		System.out.println("Calling REST Endpoint '" + path + "' - GET Method, No Authentication, No SSL");
		// create the rest client instance
		Client client = ClientBuilder.newClient();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(path);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		if (headers != null) {
			Set<String> headerNameSet = headers.keySet();
			for (Iterator<String> iterator = headerNameSet.iterator(); iterator.hasNext();) {
				String headerName = iterator.next();
				String f = headers.get(headerName);
				invocationBuilder.header(headerName, headers.get(headerName));
			}
		}
		// perform a GET on the resource.
		Response response = invocationBuilder.get();
		System.out.println("STATUS = " + response.getStatus());
		String jsonStr = response.readEntity(String.class);
		return jsonStr;
	}
	
	public String callREST_GET_BasicAuth_NoSSL(String path, String username, String password) {
		System.out.println("Calling REST Endpoint '" + path + "' - GET Method, Basic Authentication, No SSL");
		// create config and register Http Basic Authentication provider
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(feature);
		// create the rest client instance
		Client client = ClientBuilder.newClient(clientConfig);
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(path);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		// perform a GET on the resource.
		Response response = invocationBuilder.get();
		System.out.println("STATUS = " + response.getStatus());
		String jsonStr = response.readEntity(String.class);
		return jsonStr;
	}
	
	public String callREST_GET_NoAuth_TLS(String path) {
		System.out.println("Calling REST Endpoint '" + path + "' - GET Method, No Authentication, TLS");
		// instantiate SSLContext using keystore and trustore generated for Bluemix Secure Gateway
		SSLContext sslContext = createSSLContext();
		// create the rest client instance
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(path);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		// perform a GET on the resource.
		Response response = invocationBuilder.get();
		System.out.println("STATUS = " + response.getStatus());
		String jsonStr = response.readEntity(String.class);
		return jsonStr;
	}
	
	private static void _GETNoAuth() {
		System.out.println("#################### " + JerseyRestClient.class.getName() + "._GETNoAuth() - START");
		String path = "http://mobile-bologna-cantieri.mybluemix.net/api/cantieri/riqurbana";
		String jsonStr = new JerseyRestClient().callREST_GET_NoAuth_NoSSL(path, null);
		// Parsing and printing result
		JSONParser json = new JSONParser();
		JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) json.parse(jsonStr);
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println("#################### " + JerseyRestClient.class.getName() + "._GETNoAuth() - END");
		}
	}
	
	private static void _GETHttpBasicAuth() {
		System.out.println("******************** " + JerseyRestClient.class.getName() + "._GETHttpBasicAuth() - START");
		String path = "http://rpozzi-jweb.mybluemix.net/rest/items";
		String jsonStr = new JerseyRestClient().callREST_GET_BasicAuth_NoSSL(path, "robipozzi", "robipozzi");
		// Parsing and printing result
		JSONParser json = new JSONParser();
		JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) json.parse(jsonStr);
			for (Iterator<JSONObject> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObj = (JSONObject) iterator.next();
				String obj = jsonObj.toJSONString();
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			System.out.println("******************** " + JerseyRestClient.class.getName() + "._GETHttpBasicAuth() - END");
		}
	}
	
	private SSLContext createSSLContext() {
		SslConfigurator sslConfig = SslConfigurator.newInstance();
		sslConfig.keyStoreType("PKCS12");
		sslConfig.keyStoreFile(keystorePath);
		sslConfig.keyStorePassword(keystorePassword);
		sslConfig.trustStoreType("JKS");
		sslConfig.trustStoreFile(truststorePath);
		sslConfig.trustStorePassword(truststorePassword);
		SSLContext sslContext = sslConfig.createSSLContext();
		return sslContext;
	}

	public String getKeystorePath() {
		return keystorePath;
	}

	public void setKeystorePath(String keystorePath) {
		this.keystorePath = keystorePath;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getTruststorePath() {
		return truststorePath;
	}

	public void setTruststorePath(String truststorePath) {
		this.truststorePath = truststorePath;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}
	
}