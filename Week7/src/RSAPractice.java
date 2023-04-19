
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAPractice {
	public static void main(String args[]) throws Exception {
		SecureRandom sr = new SecureRandom();
		sr.setSeed(System.currentTimeMillis());

		BigInteger p = BigInteger.probablePrime(512, sr);
		BigInteger q = BigInteger.probablePrime(512, sr);
		System.out.println("P= " + p.toString());
		System.out.println("Q= " + q.toString());

		BigInteger n = p.multiply(q);
		System.out.println("N= " + n.toString());

		BigInteger pn = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		System.out.println("PN= " + pn.toString());

		BigInteger e = BigInteger.valueOf(65537);
		System.out.println("e= " + e.toString());
		
		BigInteger d = e.modInverse(pn);
		
		BigInteger plaintext = new BigInteger("1234");
		BigInteger ciphertext= plaintext.modPow(e, n);
		
		System.out.println("plaintext= " + plaintext.toString());
		System.out.println("ciphertext= " + ciphertext.toString());
		
		BigInteger dm = ciphertext.modPow(d,n);
		System.out.println("d_plaintext= " + dm.toString());

	}
}
