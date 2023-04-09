import java.security.Security;
import java.security.MessageDigest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class pbkdf1 {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md = MessageDigest.getInstance("SHA1", "BC");


        String P = "password";
        byte[] S = new byte[] { 0x78, 0x57, (byte)0x8e, 0x5a, 0x5d, 0x63, (byte)0xcb, 0x06};
        int c = 1000;
        int dkLen = 16;
        
        byte[] PasswordSalt = new byte[Utils.toByteArray(P).length + S.length];

        System.arraycopy(Utils.toByteArray(P), 0, PasswordSalt, 0, Utils.toByteArray(P).length);
        System.arraycopy(S, 0, PasswordSalt, Utils.toByteArray(P).length, S.length);

        System.out.println("Input : " + P + S);
        System.out.println("Input : " + Utils.toHexString(PasswordSalt));

        md.update(PasswordSalt);

        for(int i = 0; i < c-1; i++) {
            byte[] T = md.digest();
            md.update(T);
        }

        byte[] output = md.digest();
        byte[] result = new byte[16];

        System.arraycopy(output, 0, result, 0, dkLen);

        System.out.println("result :\n" + Utils.toHexString(result));
        System.out.println("estimated result :\n" + "DC:19:84:7E:05:C6:4D:2F:AF:10:EB:FB:4A:3D:2A:20");
    }
}