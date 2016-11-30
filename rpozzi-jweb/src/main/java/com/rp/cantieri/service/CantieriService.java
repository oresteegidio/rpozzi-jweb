package com.rp.cantieri.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rp.configuration.Configuration;
import com.rp.rest.client.RestClient;

@Path("/cantieri")
public class CantieriService {
	final String endpoint = (String) Configuration.getInstance().get(Configuration.SECURE_GATEWAY_ENDPOINT);
	final String endpointPort = (String) Configuration.getInstance().get(Configuration.SECURE_GATEWAY_ENDPOINT_PORT);
	String url = endpoint + ":" + endpointPort;

	@GET
	@Path("/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCantieri(@PathParam("type") String type) {
		url += "/api/cantieri/" + type;
		RestClient restCli = new RestClient(url, true);
		return restCli.GETinvoke();
	}
}