package licencecontrol.util;

import java.io.IOException;
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
			String query = getLicence() + ";" + getTempKey();
 			url = new URL("http://localhost:8080/rest/licence/unregister?query="+query);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("GET");
			httpCon.setReadTimeout(10000);
			httpCon.connect();
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
