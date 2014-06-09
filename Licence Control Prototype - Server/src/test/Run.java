package test;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import fr.emn.prototype.licencecontrol.server.LicenceControl;

public class Run {
	
	private final static URI BASE_URI = URI.create("http://localhost:8080/rest/");
	
	public static void main(String[] args) throws IOException {
		ResourceConfig config = new ResourceConfig(LicenceControl.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, config);
        
        System.in.read();
        
        server.shutdownNow();
	}
}
