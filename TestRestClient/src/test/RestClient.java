package test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

public class RestClient {

	public static void main(String[] args) {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget webTarget = client.target("http://localhost:8080/rest/licence/check");
		WebTarget resourceWebTargetWihQuery = webTarget.queryParam("query", "hello");
		Invocation.Builder invocationBuilder = resourceWebTargetWihQuery.request(MediaType.TEXT_PLAIN_TYPE);
		Response response = invocationBuilder.get();
		System.out.println(response.getStatus());
	}

}
