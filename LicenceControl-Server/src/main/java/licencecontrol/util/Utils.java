package licencecontrol.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public final class Utils {
	
	/**
	 * G�n�ration d'une cl� al�atoire s�curis�e en base 32
	 * @return token
	 */
	public static String generateTemporaryKey() {
		SecureRandom random = new SecureRandom();
		// Chaque caract�re en base 32 contient 5 digit, donc on prend une
		// Nombre al�atoire sur un nombre de bits divisible par 5
		return new BigInteger(130, random).toString(32);
	}
	
	public static Timestamp generateExpirationDate() {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
	    cal.add(Calendar.HOUR_OF_DAY, 1);
	    // Todo : lire en base la dur�e de validit� d'une cl� temporaire
		return new Timestamp(cal.getTime().getTime());
	}
}