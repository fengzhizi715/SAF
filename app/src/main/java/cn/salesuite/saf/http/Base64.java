package cn.salesuite.saf.http;


/*
 * referenced from www.google.com
 */
public class Base64 {
	 
    public final static String BASE64CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
 
        "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/";
 
    public final static int SPLIT_LINES_AT = 76;
 
    public static byte[] zeroPad(int length, byte[] bytes) {
        byte[] padded = new byte[length]; // initialized to zero by JVM
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return padded;
    }
 
    public static String encode(String string) {
 
        String encoded = "";
        byte[] stringArray;
        try {
            stringArray = string.getBytes("UTF-8");  // use appropriate encoding string!
        } catch (Exception ignored) {
            stringArray = string.getBytes();  // use locale default rather than croak
        }
        // determine how many padding bytes to add to the output
        int paddingCount = (3 - (stringArray.length % 3)) % 3;
        // add any necessary padding to the input
        stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
        // process 3 bytes at a time, churning out 4 output bytes
        // worry about CRLF insertions later
        for (int i = 0; i < stringArray.length; i += 3) {
            int j = (stringArray[i] << 16) + (stringArray[i + 1] << 8) + 
                stringArray[i + 2];
            encoded = encoded + BASE64CODE.charAt((j >> 18) & 0x3f) +
                BASE64CODE.charAt((j >> 12) & 0x3f) +
                BASE64CODE.charAt((j >> 6) & 0x3f) +
                BASE64CODE.charAt(j & 0x3f);
        }
        // replace encoded padding nulls with "="
        String ret = splitLines(encoded.substring(0, encoded.length() -
            paddingCount) + "==".substring(0, paddingCount));
        return ret.trim();
 
    }
    public static String splitLines(String string) {
 
        String lines = "";
        for (int i = 0; i < string.length(); i += SPLIT_LINES_AT) {
 
            lines += string.substring(i, Math.min(string.length(), i + SPLIT_LINES_AT));
            lines += "\r\n";
 
        }
        return lines;
 
    }
 
}