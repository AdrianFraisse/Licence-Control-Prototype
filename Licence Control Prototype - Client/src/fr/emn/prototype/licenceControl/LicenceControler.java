package fr.emn.prototype.licenceControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class LicenceControler {
	// Singleton
	private static LicenceControler licenceControler = new LicenceControler();
	
	private LicenceControler() {}
	
	public static LicenceControler getInstance() {
		return licenceControler;
	}
	
	public String getData() throws RuntimeException, IOException {
		return getCheckSum() + ";" + getLicence() + ";" + generateToken();
	}
	
	/**
	 * G�n�ration d'un token al�atoire s�curis�.
	 * @return token
	 */
	public String generateToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString();
	}
	
	/**
	 * Production du checksum du jar de choco.
	 * @return la chaine repr�sentant le checkSum
	 */
	public String getCheckSum() {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");// MD5
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
	 * R�cup�ration du fichier de licence, qui doit se trouver � c�t� du jar
	 * @return la chaine de caract�re de la licence
	 * @throws IOException exception lors de la lecture
	 */
	public String getLicence() throws IOException {
		InputStream inputStream = new FileInputStream(getLicencePath());
		BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));

		String licence = stream.readLine();
		// Todo gerer null
		stream.close();
		return licence;
	}
	
	/**
	 * R�cup�ration du chemin d'acc�s au jar 
	 * @return chemin d'acc�s
	 * @throws UnsupportedEncodingException Exception de d�codage
	 */
	public String getPath() {
		// r�cup�ration du chemin d'acc�s au jar
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = "";
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
			decodedPath = decodedPath.replace('/', File.separatorChar);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Erreur de d�codage du chemin d'acc�s au jar de Choco");
			System.exit(0);
		}
		// Suppression du premier slash
		return decodedPath.substring(1);
	}
	
	/**
	 * R�cup�ration du chemin d'acc�s � la licence
	 * @return chemin d'acc�s
	 */
	public String getLicencePath() {
		File file = new File(getPath());
		file.getParent();
		return (new File(getPath())).getParent() + File.separatorChar + "licence.txt";
	}
}
