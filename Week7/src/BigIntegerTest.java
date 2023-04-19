import java.math.BigInteger;
import java.security.SecureRandom;

public class BigIntegerTest {
	public static void main(String args[]) throws Exception {
		BigInteger b1 = new BigInteger("1234");
		BigInteger b2 = new BigInteger("FEA1", 16);

		// a big integer from a binary
		byte[] bin = new byte[] { 0x00, 0x13, 0x44, 0x23 };
		BigInteger b3 = new BigInteger(bin);

		// a big integer from a random value
		SecureRandom sr = new SecureRandom();
		sr.setSeed(System.currentTimeMillis());
		BigInteger b4 = new BigInteger(512, sr);
		BigInteger b5 = BigInteger.probablePrime(512, sr);

		// a big integer from valueof()
		BigInteger b6 = BigInteger.valueOf(3456);

		// a big integer from constant values
		BigInteger b7 = BigInteger.ONE;
		BigInteger b8 = BigInteger.TEN;
		BigInteger b9 = BigInteger.ZERO;

		// print
		System.out.println(b1.toString()); // decimal value
		System.out.println(b2.toString(16)); // hexadecimal value
		System.out.println(b3.toString(16)); // hexadecimal value
		System.out.println(b4.toString(16)); // hexadecimal value
		System.out.println(b5.toString(16)); // hexadecimal value
		System.out.println(b6.toString(16)); // hexadecimal value
		System.out.println(b7.toString(16)); // hexadecimal value
		System.out.println(b8.toString(16)); // hexadecimal value
		System.out.println(b9.toString(16)); // hexadecimal value

		// convert to binary
		byte[] bin2 = b3.toByteArray();
	}
}
