package licencecontrol.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import licencecontrol.util.Crypto;
import licencecontrol.util.ShutdownHook;

public class LicenceControl {
	// Singleton
	private static LicenceControl licenceController = new LicenceControl();
	private final String token;
	
	private String licence;
	private String tempKeyPath;
	private String checkSum;
	private ShutdownHook shutdownHook;
	
	/**
	 * Constructeur privé du singleton
	 * Attache un shutdownhook à l'éxecution
	 */
	private LicenceControl() {
		token = Crypto.generateToken();
		shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}
	
	/**
	 * Renvoie l'instance du controleur de licence, qui est un singleton
	 * @return
	 */
	public static LicenceControl getInstance() {
		return licenceController;
	}
	
	/**
	 * Effectue un contrôle de licence sur le serveur
	 * @throws MalformedURLException
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public void controlOnServer() throws IOException {
		BufferedReader rd  = null;
		StringBuilder sb = null;
		String line = null;
		URL url = new URL("http://localhost:8080/rest/licence/register?query="+getData());
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
        
        final String[] response = sb.toString().split(";");
        if (response.length == 2) {
        	// Format correct
        	if (response[0].equals(getToken())) {
        		// Vérification de l'identité du serveur et récupération de la clé temporaire
        		// Si le serveur renvoie le token, c'est que la licence a été acceptée
        		// la réponse contient donc forcément une clé temporaire
        		final String tempKey = response[1];
        		writeTempKey(tempKey);
        		shutdownHook.setTempKey(tempKey);
        	} else {
        		// L'identité du serveur a été usurpée
        		System.err.println("CHOCO -> Serveur non reconnu");
        		exit();
        	}
        	
        } else if (response.length == 1) {
        	// Réponse en erreur ou en rejet
        	int error = Integer.valueOf(response[0]);
        	switch (error) {
        	case 0 : {
        		System.err.println("CHOCO -> Erreur du serveur de controle de licence");
        		break;
        	}
        	case 1 : {
        		System.err.println("CHOCO -> Erreur de la base de donnees des licences");
        		break;
        	}
        	case 2 : {
        		System.err.println("CHOCO -> Controle de licence refuse");
        		break;
        	}
        	case 3 : {
        		System.err.println("CHOCO -> Nombre maximum utilisateurs atteint");
        		break;
        	}
        	case 4 : {
        		System.out.println("CHOCO -> Session liberee");
        		break;
        	}
        	default : System.err.println("CHOCO -> Erreur inconnue");
        	}
        	exit();
        } else {
        	System.err.println("Reponse invalide du serveur");
        	exit();
        }
        rd.close();
	}
	
	/**
	 * Ecrit la clé temporaire dans un fichier dans le même répertoire que celui de choco
	 * @param tempKey la clé temporaire à stocker
	 */
	private void writeTempKey(String tempKey) {
		try {
			FileWriter fw = new FileWriter(getTempKeyPath(), false);
			fw.write(tempKey);
			fw.close();
		} catch (IOException e) {
			System.out.println("Erreur à l'écriture de la clé temporaire");
			exit();
		}
		
	}

	/**
	 * Retourne une eventuelle clé temporaire
	 * @return la clé
	 * @throws IOException
	 */
	private String getTempKey() throws IOException {
		String tempKey;
		try {
			InputStream inputStream = new FileInputStream(getTempKeyPath());
			BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));
			tempKey = stream.readLine();
			stream.close();
		} catch (FileNotFoundException e) {
			tempKey = "";
		}
		
		if (tempKey == null) {
			tempKey = "";
		}
		return tempKey;
	}
	
	/**
	 * Génération et cryptage des données de la requète 
	 * @return données cryptés
	 * @throws RuntimeException
	 * @throws IOException
	 */
	private String getData() throws RuntimeException, IOException {
		String data = getLicence() + ";" + getCheckSum() + ";" + getToken();
		String tempKey = getTempKey();
		if (!tempKey.isEmpty()) {
			// S'il y a un clé temporaire, on la concatène à la requète
			data += ";" + tempKey;
		}
		try {
			// Cryptage de la requète
			return Crypto.encryptData(data, Crypto.getPublicKey() );
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeySpecException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Production du checksum du jar de choco.
	 * @return la chaine représentant le checkSum
	 */
	private String getCheckSum() {
		if (checkSum == null) {
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
	        checkSum = sb.toString();
		}
        return checkSum;
	}
	
	/**
	 * Récupération de la licence dans le fichier de licence, qui doit se trouver à côté du jar
	 * @return la chaine de caractère de la licence
	 * @throws IOException exception lors de la lecture
	 */
	private String getLicence() throws IOException {
		if (licence == null) {
			InputStream inputStream = new FileInputStream(getLicencePath());
			BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));
			licence = stream.readLine();
			// Todo gerer null
			stream.close();
			// on confie la licence au shutdownhook
			shutdownHook.setLicence(licence);
		}
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
			exit();
		}
		// Suppression du premier slash
		return decodedPath.substring(1);
	}
	
	
	/**
	 * Récupération du chemin d'accès à la licence
	 * @return chemin d'accès
	 */
	public String getLicencePath() {
		File file = new File(getPath());
		file.getParent();
		return (new File(getPath())).getParent() + File.separatorChar + "licence.txt";
	}
	
	/**
	 * récupération du chemin d'accès à la clé temporaire
	 * @return chemin d'accès
	 */
	public String getTempKeyPath() {
		if (tempKeyPath == null) {
			File file = new File(getPath());
			file.getParent();
			tempKeyPath = (new File(getPath())).getParent() + File.separatorChar + "temp";
			shutdownHook.setTempKeyFilePath(tempKeyPath);
		}
		return tempKeyPath;
	}
	
	/**
	 * Getter du token
	 * @return le token généré par le client
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Termine l'execution du programme
	 */
	public void exit() {
		try {
			SecurityManager security = System.getSecurityManager();
			// Si l'appel a System.exit(0) est desactive dans le SecurityManager, lance une SecurityException
			if (security != null) security.checkExit(0);
		} catch (SecurityException e) {
			// Remise a l'etat par defaut du SecurityManager
			System.setSecurityManager(null);
		}
		System.exit(0);
	}
}
