package comun;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

	public static String generarHash(String password) {
		MessageDigest md = null;

		try {
			
			md = MessageDigest.getInstance("MD5"); // SHA-1 //SHA-512
			md.update(password.getBytes());
			byte[] mb = md.digest();

			// Pasar el hash a Hex
			StringBuilder result = new StringBuilder();

			for (byte temp : mb) {
				int decimal = (int) temp & 0xff; // bytes widen to int, need mask, prevent sign extension
				String hex = Integer.toHexString(decimal);
				result.append(hex);
			}
			return result.toString();

		} catch (NoSuchAlgorithmException e) {
			return e.toString();
		}
	}
}
