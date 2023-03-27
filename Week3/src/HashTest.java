
import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class HashTest {

	public static void main(String args[]) throws Exception
	{
	    Security.addProvider(new BouncyCastleProvider());
	    
	    MessageDigest hash = MessageDigest.getInstance("SHA256", "BC");
	
		String student_no="12345678";
		String student_department="software";
		String student_name="hongildong";
		
<<<<<<< HEAD
		hash.update(Utils.toByteArray(student_no));
		hash.update(Utils.toByteArray(student_department));
		hash.update(Utils.toByteArray(student_name));
		
		
		byte[] out = hash.digest();
		
		
		System.out.println("Hash: "+ Utils.toHexString(out));
=======

//		hash.update(Utils.toByteArray(student_no));
//		hash.update(Utils.toByteArray(student_department));
//		hash.update(Utils.toByteArray(student_name));
		
		
		byte[] out = hash.digest();
	
		
		
		System.out.println("Hash: "+ Utils.toHexString(out));
		System.out.println(out);
>>>>>>> e6aea34854c4773224c86f41f8e295bb850f7e0a
	
	}
}
