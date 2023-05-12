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
		byte[] encryptKey = PBKDF1(P);
		byte[] headerKey = SHA1(P);

		System.out.println("----------Encryption Start----------");

		String fileName = "sample.jpg";
		File file = new File(fileName);
		long fileSize = file.length();
		FileInputStream fis = new FileInputStream(fileName);
		FileOutputStream fos = new FileOutputStream("result.enc");

		// IV
		byte[] ivBytes = new byte[] { 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00, 0x07, 0x06, 0x05, 0x04, 0x03,
				0x02, 0x01, 0x00 }; // 16Bytes

		SecretKeySpec key = new SecretKeySpec(encryptKey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
		IvParameterSpec iv = new IvParameterSpec(ivBytes);

		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		
		fos.write(headerKey);
		fos.write(ivBytes);

		byte[] input = new byte[BUF_SIZE];
		int encLength = 0;
		float encProgress = 0;
		double encStep = 0.05;
		int read = BUF_SIZE;
		while ((read = fis.read(input, 0, BUF_SIZE)) == BUF_SIZE) {
			encLength = encLength + read;
			encProgress = (float) encLength / (int) fileSize;
			if (encProgress >= encStep) {
				System.out.printf("%.0f%%\t", encStep * 100);
				encStep = encStep + 0.05;
			}
			
			fos.write(cipher.update(input, 0, read));
		}
		System.out.printf("%.0f%%\n", encStep * 100);
		fos.write(cipher.doFinal(input, 0, read));

		fis.close();
		fos.close();
		System.out.println("--------Enecryption Completed--------");

		fis = new FileInputStream("result.enc");
		fos = new FileOutputStream("sample_dec.jpg");

		byte[] compareKey = new byte[20];

		// 패스워드 입력받고 각 해쉬값 비교하기

		fis.read(compareKey, 0, 20);

		sc = new Scanner(System.in);
		System.out.print("Type your password to decrypt: ");
		P = sc.nextLine();

		byte[] decryptTryKey = SHA1(P);

		String headerPW = Utils.toHexString(compareKey);
		String decryptPW = Utils.toHexString(decryptTryKey);
		if (headerPW.equals(decryptPW)) {
			System.out.println("Correct.");
			System.out.println("----------Decryption Start----------");
		} else {
			System.out.println("You are wrong.");
			return;
		}

		byte[] decryptKey = PBKDF1(P);
		SecretKeySpec deckey = new SecretKeySpec(decryptKey, "AES");

		byte[] IV = new byte[16];
		fis.read(IV, 0, 16);
		iv = new IvParameterSpec(IV);
		cipher.init(Cipher.DECRYPT_MODE, deckey, iv);

		byte[] plainText = new byte[BUF_SIZE];
		int decLength = 0;
		float decProgress = 0;
		double decStep = 0.05;
		
		File decFile = new File("result.enc");
		long decFileSize = decFile.length()-36;
		
		while ((read = fis.read(plainText, 0, BUF_SIZE)) == BUF_SIZE) {
			decLength = decLength + read;
			decProgress = (float) decLength / (int) decFileSize;
			if (decProgress >= decStep) {
				System.out.printf("%.0f%%\t", decStep * 100);
				decStep = decStep + 0.05;
			}
			fos.write(cipher.update(plainText, 0, read));
		}
		System.out.printf("%.0f%%\n", decStep * 100);
		fos.write(cipher.doFinal(plainText, 0, read));
		System.out.println("--------Decryption Completed--------");

	}

	public static byte[] SHA1(String input) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		MessageDigest hash = MessageDigest.getInstance("SHA1", "BC");

		hash.update(Utils.toByteArray(input));
		byte[] out = hash.digest();
		return out;
	}

	public static byte[] PBKDF1(String input) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		MessageDigest hash = MessageDigest.getInstance("SHA1", "BC");
		String P = input;
		byte[] S = new byte[] { 0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06 };
		int c = 1000;

		// password와 salt를 넣을 배열 생성
		byte[] PasswordSalt = new byte[Utils.toByteArray(P).length + S.length];

		// 배열에 password와 salt를 저장
		System.arraycopy(Utils.toByteArray(P), 0, PasswordSalt, 0, Utils.toByteArray(P).length);
		System.arraycopy(S, 0, PasswordSalt, Utils.toByteArray(P).length, S.length);

		// 메시지에 업데이트
		hash.update(PasswordSalt);
		for (int i = 0; i < c - 1; i++) {
			byte[] T = hash.digest();
			hash.update(T);
		}

		// 결과를 키로 활용
		byte[] outputKey = hash.digest();
		byte[] resultKey = new byte[16];
		System.arraycopy(outputKey, 0, resultKey, 0, 16);

		return resultKey;
	}

}