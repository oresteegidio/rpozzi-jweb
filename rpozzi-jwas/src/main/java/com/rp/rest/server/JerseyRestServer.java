package com.rp.rest.server;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyRestServer extends ResourceConfig {

	public JerseyRestServer() {
		super();
		packages("com.rp.restaurant.service");
	}
	
}