package licencecontrol.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import licencecontrol.util.Crypto;

public class LicenceControl {
	// Singleton
	private static LicenceControl licenceController = new LicenceControl();
	private final String token;
	
	private LicenceControl() {
		token = generateToken();
	}
	
	public static LicenceControl getInstance() {
		return licenceController;
	}
	
	/**
	 * Effectue un contrôle de licence sur le serveur
	 * @throws MalformedURLException
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public void controlOnServer() throws MalformedURLException, RuntimeException, IOException {
		BufferedReader rd  = null;
		StringBuilder sb = null;
		String line = null;
		String query = getData();
		// Récupération d'une éventuelle clé temporaire
		final String tempKey =  getTempKey();
		if (!tempKey.isEmpty()) {
			// Si une clé temporaire est présente, on est soit en validation soit en post-crash.
			query += ";" + tempKey;
		}
		URL url = new URL("http://localhost:8080/rest/licence/register?query="+getData());
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
        
        if (sb.toString().equals(token+"\n")) System.out.println("Contrôle OK");
        else {
        	System.out.println(sb.toString());
        }
        rd.close();
	}
	
	/**
	 * Retourne une eventuelle clé temporaire
	 * @return la clé
	 * @throws IOException
	 */
	private String getTempKey() throws IOException {
		InputStream inputStream = new FileInputStream(getTempKeyPath());
		BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));

		String tempKey = stream.readLine();
		// Todo gerer null
		stream.close();
		if (tempKey == null) {
			tempKey = "";
		}
		return tempKey;
	}

	private String getData() throws RuntimeException, IOException {
		String data = getCheckSum() + ";" + getLicence() + ";" + getToken();
		try {
			return Crypto.encryptData(data, Crypto.getPublicKey() );
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeySpecException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Génération d'un token aléatoire sécurisé.
	 * @return token
	 */
	private String generateToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString();
	}
	
	/**
	 * Production du checksum du jar de choco.
	 * @return la chaine représentant le checkSum
	 */
	private String getCheckSum() {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            fis = new FileInputStream(getPath());
            byte[] dataBytes = new byte[1024];
            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();

            for (int i=0; i<mdbytes.length; i++) {
            	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100 , 16).substring(1));
            }
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
        	if (fis != null) {
        		try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }

        return sb.toString();
	}
	
	/**
	 * Récupération du fichier de licence, qui doit se trouver à côté du jar
	 * @return la chaine de caractère de la licence
	 * @throws IOException exception lors de la lecture
	 */
	private String getLicence() throws IOException {
		InputStream inputStream = new FileInputStream(getLicencePath());
		BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));

		String licence = stream.readLine();
		// Todo gerer null
		stream.close();
		return licence;
	}
	
	/**
	 * Récupération du chemin d'accès au jar 
	 * @return chemin d'accès
	 * @throws UnsupportedEncodingException Exception de décodage
	 */
	private String getPath() {
		// récupération du chemin d'accès au jar
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = "";
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
			decodedPath = decodedPath.replace('/', File.separatorChar);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Erreur de décodage du chemin d'accès au jar de Choco");
			System.exit(0);
		}
		// Suppression du premier slash
		return decodedPath.substring(1);
	}
	
	/**
	 * Récupération du chemin d'accès à la licence
	 * @return chemin d'accès
	 */
	private String getLicencePath() {
		File file = new File(getPath());
		file.getParent();
		return (new File(getPath())).getParent() + File.separatorChar + "licence.txt";
	}
	
	/**
	 * récupération du chemin d'accès à la clé temporaire
	 * @return chemin d'accès
	 */
	private String getTempKeyPath() {
		File file = new File(getPath());
		file.getParent();
		return (new File(getPath())).getParent() + File.separatorChar + "temp.txt";
	}

	public String getToken() {
		return token;
	}
}
