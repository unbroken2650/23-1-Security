
import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class HashTest_hw {

	public static void main(String args[]) throws Exception
	{
	    Security.addProvider(new BouncyCastleProvider());
	    
	    MessageDigest hash = MessageDigest.getInstance("SHA1");
	
		String student_no="20192650";
		// 0���� 100������ ������ ������ ��� 
		int random_num = (int)(Math.random()*100);
		byte[] out;
		
		// hash �Լ��� 1-2��° ����Ʈ�� 0�� �� ���� hash ���� ���
		while(true)
		{
			hash.update(Utils.toByteArray(student_no));
			hash.update(Utils.toByteArray(Integer.toString(random_num)));
			out = hash.digest();
			if(out[0]==0) {
				System.out.println("Answer #1: "+ Utils.toHexString(out)+ " With random number "+random_num);
				break;
			}
			else {
				random_num++;
				continue;
			}
		}
		// hash �Լ��� 1-4��° ����Ʈ�� 0�� �� ���� hash ���� ���
		while(true)
		{
			hash.update(Utils.toByteArray(student_no));
			hash.update(Utils.toByteArray(Integer.toString(random_num)));
			out = hash.digest();
			if(out[0]==0 && out[1] == 0) {
				System.out.println("Answer #2: "+ Utils.toHexString(out) + " With random number "+random_num);
				break;
			} 
			else {
				random_num++;
				continue;
			}
		}	
	}
}