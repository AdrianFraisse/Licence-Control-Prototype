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
		URL url = new URL("http://localhost:8080/rest/licence/check?query=8A0C23DF330A1A9A2E8A7625FF962F89DB5D0C12AF4A8AE15FCE30D0D41291C7A15275F2BC792818752301D9E98CAA7D194C185E88A39AA97BC70D0DB46D5429DC2D23228CDD890B7F2938DF703993641A99D057C797BD6C7180691BFF9A83A643063192C864446E633F46267797B08CF8EE2FBEB3741564A7EE367088472B4FDEFC13E3C5108F06FA23B3B903898941FD181A6128B64F9E16173B45FBD0493E1A0460A149A7FCE3417560A8C96A4DE2CD76B1EB8FFCFFEAE8F70751DE2FE2D3246C5F3AE4FA8F458663C11E65B6A75C36214861477D6D4C39F8E8745A2D4118D7B2B4A3B5ACD0FBFDD472D8719979F6BE099A2E7642DABFCD8FC952BA45BFD7");
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
