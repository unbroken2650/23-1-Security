public class Utils {
	public static String toString(byte[] input) {
		return new String(input);
	}
	
	public static String toHexStringC(byte[] input) {
		String hexString = "";
		int length = input.length;
		
		for(int i = 0; i < length; i++) {
		
				hexString += String.format("%02X", input[i]);
			
		}
		
		return hexString;
	}
	
	public static String toHexString(byte[] input) {
		String hexString = "";
		int length = input.length;
		
		for(int i = 0; i < length; i++) {
			if(i != length - 1) {
				hexString += String.format("%02X:", input[i]);
			} else {
				hexString += String.format("%02X", input[i]);
			}
		}
		
		return hexString;
	}
	
	public static String toHexString(byte[] input, int size) {
		String hexString = "";
		int length = size;
		
		for(int i = 0; i < length; i++) {
			if(i != length - 1) {
				hexString += String.format("%02X:", input[i]);
			} else {
				hexString += String.format("%02X", input[i]);
			}
		}
		
		return hexString;
	}
	
	
	
	public static byte[] toByteArray(String string)
    {
        byte[] bytes = new byte[string.length()];
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++)
        {
            bytes[i] = (byte)chars[i];
        }

        return bytes;
    }

}
