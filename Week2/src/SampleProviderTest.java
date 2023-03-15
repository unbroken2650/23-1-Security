import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SampleProviderTest {
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		String providerName = "BC";
		if (Security.getProvider(providerName) == null) {
			System.out.println(providerName + " provider not installed");
		} else {
			System.out.println(providerName + " is installed.");
		}
	}
}