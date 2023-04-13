import java.security.Security;
import java.security.MessageDigest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class pbkdf1 {

    public static void main(String[] args) throws Exception {
        //Provider 불러오기
        Security.addProvider(new BouncyCastleProvider());

        //Instance 불러오기
        MessageDigest md = MessageDigest.getInstance("SHA1", "BC");

        String P = "password";
        byte[] S = new byte[] { 0x78, 0x57, (byte)0x8e, 0x5a, 0x5d, 0x63, (byte)0xcb, 0x06};
        int c = 1000;
        int dkLen = 16;
        
        //password와 salt를 넣을 배열 생성
        byte[] PasswordSalt = new byte[Utils.toByteArray(P).length + S.length];

        // 위 배열에 password와 salt를 저장
        System.arraycopy(Utils.toByteArray(P), 0, PasswordSalt, 0, Utils.toByteArray(P).length);
        System.arraycopy(S, 0, PasswordSalt, Utils.toByteArray(P).length, S.length);

        System.out.println("Original Input : " + P);
        System.out.println("Input : " + Utils.toHexString(PasswordSalt));

        // 메시지에 업데이트
        md.update(PasswordSalt);

        // digest->update를 count(=1000)번 반복
        for (int i = 0; i < c - 1; i++) {
            byte[] T = md.digest();
            md.update(T);
        }
        
        // 결과를 출력하고 Test Vector와 비교
        byte[] output = md.digest();
        byte[] result = new byte[16];

        System.arraycopy(output, 0, result, 0, dkLen);

        System.out.println("result :\t\t" + Utils.toHexString(result));
        System.out.println("estimated result :\t" + "DC:19:84:7E:05:C6:4D:2F:AF:10:EB:FB:4A:3D:2A:20");
    }
}