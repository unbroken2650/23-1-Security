
import java.security.Security;
import java.security.Key;

import javax.crypto.Cipher;ale
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MacTest {

	public static void main(String args[]) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		String message = "mac message";

		byte key[] = new byte[] { 0x01, 0x02, 0x03, 0x01, 0x02, 0x03, 0x01, 0x02, 0x03, 0x01, 0x02, 0x03 };

		Mac mac = Mac.getInstance("HmacSHA1", "BC");

		Key keyHmac = new SecretKeySpec(key, "HmacSHA1");

		mac.init(keyHmac);

		byte[] out = mac.doFinal(Utils.toByteArray(message));

		System.out.println("MAC= " + Utils.toHexString(out));

	}
}
