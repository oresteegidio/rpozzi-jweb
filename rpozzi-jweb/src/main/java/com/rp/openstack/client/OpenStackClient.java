/*package com.rp.openstack.client;

import java.io.InputStream;
import java.util.logging.Logger;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.openstack.OSFactory;

public class OpenStackClient {
	static String auth_url = "https://keystone2.open.ibmcloud.com";
	static String swift_url = "https://swift2.open.ibmcloud.com/v1/AUTH_0c90518a0f2b4158b4e114d846300c43";
	static String sdk_auth_url = "https://keystone2.open.ibmcloud.com";
	static String project = "0d077451-3089-4a18-afea-fe72188a1d33";
	static String region = "dal09";
	static String userid = "roberto_pozzi@it.ibm.com";
	static String password = "Z)Lqy-Bz4|]T)09i";
	static final String API_VERSION = "v2.0";
	static String containerName = "rp_object_container";
	static Logger LOG = Logger.getLogger(OpenStackClient.class.getName());
	
	public void upload(InputStream in) {
		OSClient os = OSFactory.builder().endpoint(auth_url + "/" + API_VERSION).credentials(userid, password).tenantName(project).authenticate();
		LOG.info("Openstack Authentication token = " + os.getToken().getId());
		String etag = os.objectStorage().objects().put(containerName, "pippo", Payloads.create(in));
	}

}*/