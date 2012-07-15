package sierra;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SierraHasher
{
	public static String ComputeAllThree(String Input)
	{
		return EncodeB64(ComputeMD5(ComputeSHA1(Input) + ComputeSHA1(Input)));
	}
	public static String ComputeSHA1(String Input)
	{
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.update(Input.getBytes(), 0, Input.length());
		return new BigInteger(1, m.digest()).toString();
	}
	public static String ComputeMD5(String Input)
	{
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.update(Input.getBytes(), 0, Input.length());
		return new BigInteger(1, m.digest()).toString();
	}
	private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	private static int[]  toInt   = new int[128];

	static {
		for(int i=0; i< ALPHABET.length; i++){
			toInt[ALPHABET[i]]= i;
		}
	}
	public static String EncodeB64(String bufs)
	{
		byte[] buf = bufs.getBytes();
		int size = buf.length;
		char[] ar = new char[((size + 2) / 3) * 4];
		int a = 0;
		int i=0;
		while(i < size){
			byte b0 = buf[i++];
			byte b1 = (i < size) ? buf[i++] : 0;
			byte b2 = (i < size) ? buf[i++] : 0;

			int mask = 0x3F;
			ar[a++] = ALPHABET[(b0 >> 2) & mask];
			ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
			ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
			ar[a++] = ALPHABET[b2 & mask];
		}
		switch(size % 3){
		case 1: ar[--a]  = '=';
		case 2: ar[--a]  = '=';
		}
		return new String(ar);
	}
	public static byte[] DecodeBase64(String s){
		int delta = s.endsWith( "==" ) ? 2 : s.endsWith( "=" ) ? 1 : 0;
		byte[] buffer = new byte[s.length()*3/4 - delta];
		int mask = 0xFF;
		int index = 0;
		for(int i=0; i< s.length(); i+=4){
			int c0 = toInt[s.charAt( i )];
			int c1 = toInt[s.charAt( i + 1)];
			buffer[index++]= (byte)(((c0 << 2) | (c1 >> 4)) & mask);
			if(index >= buffer.length){
				return buffer;
			}
			int c2 = toInt[s.charAt( i + 2)];
			buffer[index++]= (byte)(((c1 << 4) | (c2 >> 2)) & mask);
			if(index >= buffer.length){
				return buffer;
			}
			int c3 = toInt[s.charAt( i + 3 )];
			buffer[index++]= (byte)(((c2 << 6) | c3) & mask);
		}
		return buffer;
	} 
}
