package com.rp.customer.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CustomerRestClient {

	/*===== Jersey based client =====*/
	public String getCustomersByProv(String prov) {
		String path = "http://cloudknow-italy-web.mybluemix.net/jaxrs/clients/getClientsByProv/" + prov;
		// create the rest client instance
		Client client = ClientBuilder.newClient();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(path);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		// perform a GET on the resource. The resource will be returned as plain text
		Response response = invocationBuilder.get();
		System.out.println(response.getStatus());
		String jsonStr = response.readEntity(String.class);
		System.out.println(jsonStr);
		return jsonStr;
	}

}