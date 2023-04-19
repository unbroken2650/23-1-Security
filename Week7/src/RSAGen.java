
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAGen {

	public static void main(String args[]) throws NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());

		PrivateKey privKey;
		PublicKey pubKey;

		KeyPairGenerator keyGen = null;
		SecureRandom random = null;

		try {
			keyGen = KeyPairGenerator.getInstance("RSA", "BC");
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		keyGen.initialize(3072, random);

		KeyPair pair = keyGen.generateKeyPair();
		privKey = pair.getPrivate();
		pubKey = pair.getPublic();

		RSAPublicKey rsaPubKey = (RSAPublicKey) pubKey;

		RSAPrivateCrtKey rsaCrtPrivKey = (RSAPrivateCrtKey) privKey;

		System.out.println("public key(e) = " + rsaPubKey.getPublicExponent());
		System.out.println("public key(n) = " + rsaPubKey.getModulus());
		System.out.println("private key(d) = " + rsaCrtPrivKey.getPrivateExponent());

	}

	// hexadecimal value: "0x" format
	public static String stringToHex0x(String s) {
		String result = "";

		for (int i = 0; i < s.length(); i++) {
			result += String.format("0x%02X ", (int) s.charAt(i));
		}

		return result;
	}
}
