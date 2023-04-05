
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CipherTest {

	public static void main(String args[]) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		byte[] input = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
				0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 }; // 24Bytes

		byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c,
				0x0d, 0x0e, 0x0f, }; // 16Bytes

		byte[] ivBytes = new byte[] { 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00, 0x07, 0x06, 0x05, 0x04, 0x03,
				0x02, 0x01, 0x00 }; // 16Bytes

		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

		IvParameterSpec iv = new IvParameterSpec(ivBytes);

		System.out.println("input : " + Utils.toHexString(input));

		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		byte[] output = new byte[cipher.getOutputSize(input.length)];
		int processLen;

		// 24 bytes = 16 Bytes + 8 Bytes 중의 첫 16 Bytes(0~15)만
//		processLen = cipher.update(input, 0, input.length, output, 0);
//		processLen = cipher.doFinal(output, processLen);
		
		// multiple-part operation을 사용하면 input 포함 32 Bytes의 데이터를 한 번에 encrypt
		processLen = cipher.doFinal(input, 0, input.length, output, 0);
		System.out.println("Encrypted Len: " + processLen);

		System.out.println("output : " + Utils.toHexString(output));

		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		byte[] plainText = new byte[cipher.getOutputSize(output.length)];

//		int decryptedLen = cipher.update(output, 0, output.length, plainText, 0);
//		decryptedLen += cipher.doFinal(plainText, decryptedLen);
		
		// multiple-part operation을 사용하면 input 포함 32 Bytes의 데이터를 한 번에 decrypt
		int decryptedLen = cipher.doFinal(output, 0, output.length, plainText, 0);
		System.out.println("Decrypted Len: " + decryptedLen);

		System.out.println("plaintext :" + Utils.toHexString(plainText));

	}
}
