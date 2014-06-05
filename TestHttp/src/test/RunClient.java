package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Pas de dependances autres que celles de la JVM.
 * Envoie une requete HTTP : http://localhost:8080/rest/licence/check?query=i_am_the_client
 * Recupere et affiche la reponse dans la console
 * 
 */
public class RunClient {

	public static void main(String[] args) throws IOException {
		BufferedReader rd  = null;
		StringBuilder sb = null;
		String line = null;
		URL url = new URL("http://localhost:8080/rest/licence/check?query=i_am_the_client");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("GET");
		httpCon.setReadTimeout(10000);
		httpCon.connect();
		rd  = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
        sb = new StringBuilder();
      
        while ((line = rd.readLine()) != null)
        {
            sb.append(line);
        }
      
        System.out.println(sb.toString());
        
        rd.close();
	}

}
