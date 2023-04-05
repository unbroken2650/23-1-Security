import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SimpleSymmetricExample {
	public static void main(String args[]) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		byte[] input = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
				0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 }; // 24Bytes

		byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c,
				0x0d, 0x0e, 0x0f, }; // 16 Bytes = 256 bits

		byte[] ivBytes = new byte[] { 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00, 0x07, 0x06, 0x05, 0x04, 0x03,
				0x02, 0x01, 0x00 }; // 16 Bytes

		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

		IvParameterSpec iv = new IvParameterSpec(ivBytes);

		System.out.println("input : " + Utils.toHexString(input));

		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		byte[] output = new byte[cipher.getOutputSize(input.length)];

		int processLen;

		// 24 bytes = 16 Bytes + 8 Bytes 중의 첫 16 Bytes(0~15)만
		processLen = cipher.update(input, 0, input.length, output, 0);
		System.out.println("update len : " + processLen);

		// processLen(16) 부터 doFinal이 마지막 블록을 추가해줌
		processLen = cipher.doFinal(output, processLen);
		System.out.println("doFinal len : " + processLen);

		System.out.println("output : " + Utils.toHexString(output));

		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		byte[] plainText = new byte[cipher.getOutputSize(output.length)];

		//암호화와 마찬가지로 16 Bytes만 
		int decryptedLen = cipher.update(output, 0, output.length, plainText, 0);

		decryptedLen += cipher.doFinal(plainText, decryptedLen);

		System.out.println(("plaintext :" + Utils.toHexString(plainText)));
	}
}