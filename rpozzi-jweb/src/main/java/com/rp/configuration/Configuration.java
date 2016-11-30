package com.rp.configuration;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {
	private static Configuration _theInstance = null;
	private HashMap<String, Object> _map = null;
	public static final String USERPRINCIPAL = "USERPRINCIPAL";
	// API MANAGER configuration parameters
	public static final String APIMANAGER_BASEPATH = "apiManagerBasePath";
	// Secure Gateway endpoints configuration parameters
	public static final String SECURE_GATEWAY_ENDPOINT = "secureGatewayEndpoint";
	public static final String SECURE_GATEWAY_ENDPOINT_PORT = "secureGatewayEndpointPort";
	// Security configuration parameters
	public static final String SECURITY_KEYSTORE = "securityKeyStore";
	public static final String SECURITY_TRUSTSTORE = "securityTrustStore";
	
	private Configuration() {
		super();
	}
	
	public static Configuration getInstance() {
		if (_theInstance == null) {
			_theInstance = new Configuration();
			_theInstance.init();
		}
		return _theInstance;
	}
	
	public static Configuration getInstance(Properties props) {
		if (_theInstance == null) {
			_theInstance = new Configuration();
			_theInstance.init(props);
		}
		return _theInstance;
	}
	
	public Object get(String key) {
		return _map.get(key);
	}
	public void put(String key, Object obj) {
		_map.put(key,obj);
	}
	private void init() {
		if (_map == null) {
			_map = new HashMap<String, Object>();
		}
	}
	private void init(Properties props) {
		if (_map == null) {
			_map = new HashMap<String, Object>();
		}
		Enumeration<Object> keys = props.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			Object value = props.getProperty(key);
			System.out.println(this.getClass().getName() + ".init(Properties props) --> key = " + key + " = " + value);
			_map.put(key, value);
		}
		
	}
}