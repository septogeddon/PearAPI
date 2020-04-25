package septogeddon.pear.utils;

import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {
	public static final String SHA_1 = "SHA-1", SHA_256 = "SHA-256", SHA_384 = "SHA-384", SHA_512 = "SHA-512";
	public static String encrypt(String passwordToHash) {
		String generatedPassword = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(UUID.nameUUIDFromBytes(passwordToHash.getBytes()).toString().replace("-", "").getBytes(),"AES"));
			generatedPassword = new String(cipher.doFinal(passwordToHash.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

}
