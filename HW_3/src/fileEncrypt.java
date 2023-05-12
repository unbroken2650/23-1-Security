import java.security.Security;
import java.sql.ClientInfoStatus;
import java.util.Scanner;
import java.io.File;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;

import org.bouncycastle.jcajce.provider.symmetric.Blowfish;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class fileEncrypt {
	public static int BUF_SIZE = 4096;

	public static void main(String[] args) throws Exception {

		// Provider 불러오기
		Security.addProvider(new BouncyCastleProvider());

		// Instance 불러오기
		MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
		String P = "";
		Scanner sc = new Scanner(System.in);
		System.out.print("Type your password: ");
		P = sc.nextLine();

		byte[] S = new byte[] { 0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06 };
		int c = 1000;

		// password와 salt를 넣을 배열 생성
		byte[] PasswordSalt = new byte[Utils.toByteArray(P).length + S.length];

		// 배열에 password와 salt를 저장
		System.arraycopy(Utils.toByteArray(P), 0, PasswordSalt, 0, Utils.toByteArray(P).length);
		System.arraycopy(S, 0, PasswordSalt, Utils.toByteArray(P).length, S.length);

		// 메시지에 업데이트
		md.update(PasswordSalt);

		for (int i = 0; i < c - 1; i++) {
			byte[] T = md.digest();
			md.update(T);
		}

		// 결과를 키로 활용
		byte[] outputKey = md.digest();
		byte[] resultKey = new byte[16];

		System.arraycopy(outputKey, 0, resultKey, 0, 16);

		System.out.println("----------Encryption Start----------");

		byte[] input = new byte[BUF_SIZE];
		File file = new File("sample.jpg");
		long fileSize = file.length();
		FileInputStream fis = new FileInputStream("sample.jpg");
		FileOutputStream fos = new FileOutputStream("result.enc");

		// IV
		byte[] ivBytes = new byte[] { 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00, 0x07, 0x06, 0x05, 0x04, 0x03,
				0x02, 0x01, 0x00 }; // 16Bytes

		SecretKeySpec key = new SecretKeySpec(resultKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
		IvParameterSpec iv = new IvParameterSpec(ivBytes);

		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] output = new byte[cipher.getOutputSize(input.length)];
		int read = BUF_SIZE;
		fos.write(resultKey);
		fos.write(ivBytes);

		int encLength = 0;
		float encProgress = 0;
		double encStep = 0.05;
		while ((read = fis.read(input, 0, BUF_SIZE)) == BUF_SIZE) {
			encLength = encLength + read;
			encProgress = (float) encLength / (int) fileSize;
			if (encProgress > encStep) {
				System.out.printf("%.0f%%\n", encStep * 100);
				encStep = encStep + 0.05;
				if (encStep > 1) {
					break;
				}
			}
			fos.write(cipher.update(input, 0, read));
		}
		fos.write(cipher.doFinal(input, 0, read));

		fis.close();
		fos.close();

		fis = new FileInputStream("result.enc");
		fos = new FileOutputStream("sample_dec.jpg");

		byte[] compareKey = new byte[16];
		byte[] plainText = new byte[16];
		byte[] IV = new byte[16];

		// 패스워드 입력받고 각 해쉬값 비교하기

		fis.read(compareKey, 0, 16);
		sc = new Scanner(System.in);
		System.out.print("Type your password to decrypt: ");
		P = sc.nextLine();

		S = new byte[] { 0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06 };
		c = 1000;

		// password와 salt를 넣을 배열 생성
		PasswordSalt = new byte[Utils.toByteArray(P).length + S.length];

		// 배열에 password와 salt를 저장
		System.arraycopy(Utils.toByteArray(P), 0, PasswordSalt, 0, Utils.toByteArray(P).length);
		System.arraycopy(S, 0, PasswordSalt, Utils.toByteArray(P).length, S.length);

		// 메시지에 업데이트
		md.update(PasswordSalt);

		for (int i = 0; i < c - 1; i++) {
			byte[] T = md.digest();
			md.update(T);
		}

		// 결과를 키로 활용
		outputKey = md.digest();
		resultKey = new byte[16];
		System.arraycopy(outputKey, 0, resultKey, 0, 16);

		String resultText = Utils.toHexString(resultKey);
		String compareText = Utils.toHexString(compareKey);
		if (resultText.equals(compareText)) {
			System.out.println("Correct.");
			System.out.println("----------Decryption Start----------");
		}

		fis.read(IV, 0, 16);
		System.out.print("IV: ");
		System.out.println(Utils.toHexString(IV));
		iv = new IvParameterSpec(IV);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		/*
		 * byte[] plainText = new byte[20]; read = fis.read(plainText, 0, 20);
		 * System.out.println(Utils.toHexString(plainText));
		 */
		// read = fis.read(plainText, 0, 16);
		System.out.println(Utils.toHexString(plainText));
		plainText = new byte[BUF_SIZE];
		while ((read = fis.read(plainText, 0, BUF_SIZE)) == BUF_SIZE) {
			fos.write(cipher.update(plainText, 0, read));
		}
		fos.write(cipher.doFinal(plainText, 0, read));

	}

}