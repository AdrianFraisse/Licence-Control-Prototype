package licencecontrol.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class Crypto {
	
	private static final String CRYPTO_ALGO = "RSA";
	private static final BigInteger PUB_KEY_MODULUS = new BigInteger("20524028555919395017281257"
			+ "486316511230618282300349586903555410240554452964985622634891056811028641482551425"
			+ "991724453142294958944949701647979608219812170516526004912371886801369754457912122"
			+ "560344905427154182800043080790761778512348715020172125988281989014619642240107011"
			+ "511504059948259296872791817304367467426895092035671147095992883670017448730748151"
			+ "561954354768552631507968510761286952635627767550961570307183848419959809490916811"
			+ "779517856865408117297117686767170040125784979573198997562866693273281608872906157"
			+ "918044067836880017335988323102076667084246331279411905713497072245788275144458755"
			+ "958130110434053591673559");
	private static final BigInteger PUB_KEY_EXPONENT = new BigInteger("65537");

	public static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec spec = new RSAPublicKeySpec(PUB_KEY_MODULUS, PUB_KEY_EXPONENT);
		KeyFactory factory = KeyFactory.getInstance(CRYPTO_ALGO);
		return factory.generatePublic(spec);
	}
	
	public static String encryptData(String data, PublicKey key)
			throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		byte[] dataToEncrypt = data.getBytes();
		Cipher cipher = Cipher.getInstance(CRYPTO_ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedData = cipher.doFinal(dataToEncrypt);
		System.out.println("Encrypted Data: " + DatatypeConverter.printHexBinary(encryptedData));
		
		return DatatypeConverter.printHexBinary(encryptedData);
	}
	
	
	/**
	 * Génération d'un token aléatoire sécurisé en base 32.
	 * @return token
	 */
	public static String generateToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(32, random).toString();
	}
}
