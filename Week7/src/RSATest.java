
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class RSATest {

	RSAPrivateCrtKey privKey;
	RSAPublicKey pubKey;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Security.addProvider(new BouncyCastleProvider());
		byte[] m = { 01, 02, 03, 04, 05 };
		byte[] c = null;
		RSATest rsaTest = new RSATest();

		rsaTest.initRSA();
		System.out.println("RSA encryption");
		c = rsaTest.encryptTest(m);

		System.out.println("RSA decryption");
		if (c != null)
			m = rsaTest.decryptTest(c);

	}

	public void initRSA() throws Exception {

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

		keyGen.initialize(1024, random);
		KeyPair pair = keyGen.generateKeyPair();

		privKey = (RSAPrivateCrtKey) pair.getPrivate();
		pubKey = (RSAPublicKey) pair.getPublic();

	}

	public byte[] encryptTest(byte[] in) throws Exception {
		byte[] c = new byte[128];
		System.out.println("m = " + new String(Hex.encode(in)));

		Cipher cipher = Cipher.getInstance("RSA/OAEP/NOPADDING", "BC");

		cipher.init(Cipher.ENCRYPT_MODE, pubKey);

		c = cipher.doFinal(in);

		System.out.println("c = " + new String(Hex.encode(c)));

		return c;
	}

	public byte[] decryptTest(byte[] in) throws Exception {
		byte[] dm = new byte[128];
		System.out.println("c = " + new String(Hex.encode(in)));

		Cipher cipher = Cipher.getInstance("RSA/OAEP/NOPADDING", "BC");

		cipher.init(Cipher.DECRYPT_MODE, privKey);

		dm = cipher.doFinal(in);

		System.out.println("dm = " + new String(Hex.encode(dm)));

		return dm;
	}

}
