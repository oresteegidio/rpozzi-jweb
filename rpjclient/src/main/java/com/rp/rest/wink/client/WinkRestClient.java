package com.rp.rest.wink.client;

/*import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;*/

public class WinkRestClient {

	public static void main(String[] args) {
		/*String path = "http://cloudknow-italy-web.mybluemix.net/jaxrs/clients/getClientsByProv/MI";
		// create the rest client instance
		RestClient client = new RestClient();
		// create the resource instance to interact with
		Resource resource = client.resource(path);
		// perform a GET on the resource. The resource will be returned as plain text
		String jsonStr = resource.accept("application/json").get(String.class);
		System.out.println(jsonStr);

		JSONParser json = new JSONParser();
		JSONArray obj = null;
		try {
			obj = (JSONArray) json.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (Iterator<JSONObject> iterator = obj.iterator(); iterator.hasNext();) {
			JSONObject jsonObj = (JSONObject) iterator.next();
			jsonObj.keySet();
		}*/
	}

}