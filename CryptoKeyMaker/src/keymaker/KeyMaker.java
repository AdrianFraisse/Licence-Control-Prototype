package keymaker;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class KeyMaker {
	
	private static final String CRYPTO_ALGO = "RSA";
	private static final int KEY_SIZE_IN_BITS = 2048;

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(CRYPTO_ALGO);
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		kpg.initialize(KEY_SIZE_IN_BITS, random);
		KeyPair kp = kpg.genKeyPair();
		PublicKey publicKey = kp.getPublic();
		PrivateKey privateKey = kp.getPrivate();
		
		KeyFactory factory = KeyFactory.getInstance(CRYPTO_ALGO);
		
		RSAPublicKeySpec pub = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		RSAPrivateKeySpec priv = factory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
		
		System.out.println("public key modulus = " + pub.getModulus());
		System.out.println("public key exponent = " + pub.getPublicExponent());
		System.out.println();
		System.out.println("private key modulus = " + priv.getModulus());
		System.out.println("private key exponent = " + priv.getPrivateExponent());
	}

}
