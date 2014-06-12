package licencecontrol.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShutdownHook extends Thread{
	
	private String licence;
	private String tempKey;
	
	@Override
	public void run() {
		System.out.println("Fermeture de la session");
		URL url;
		try {
			BufferedReader rd  = null;
			StringBuilder sb = null;
			String line = null;
			String query = getLicence() + ";" + getTempKey();
 			url = new URL("http://localhost:8080/rest/licence/unregister?query="+query);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setReadTimeout(10000);
			httpCon.connect();
			
			rd  = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
	        sb = new StringBuilder();

	        // Récéption de la réponse du serveur
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        System.out.println(sb.toString());
		} catch (IOException e) {
			System.out.println("Erreur d'entrées / sorties");
		}
	}
	
	public String getLicence() {
		return licence;
	}
	
	public String getTempKey() {
		return tempKey;
	}
	
	public void setLicence(String l) {
		licence = l;
	}
	
	public void setTempKey(String t) {
		tempKey = t;
	}
}
