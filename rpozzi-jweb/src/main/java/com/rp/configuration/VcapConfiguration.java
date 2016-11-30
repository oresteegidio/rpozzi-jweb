package com.rp.configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VcapConfiguration {
	private static VcapConfiguration _theInstance = null;

	private VcapConfiguration() {
		super();
	}

	public static VcapConfiguration getInstance() {
		if (_theInstance == null) {
			_theInstance = new VcapConfiguration();
			_theInstance.init();
		}
		return _theInstance;
	}

	private void init() {
		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
		String vcapServices = System.getenv("VCAP_SERVICES");
		if (vcapServices == null) {
			System.out.println("VCAP_SERVICES not found");
			return;
		}
		System.out.println("VCAP_SERVICES = " + vcapServices);
		JSONParser json = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) json.parse(vcapServices);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}