package sierra.utils;

public class BitConverter
{
	public static byte[] getBytes(short x)
	{
		return new byte[] { 
				(byte)(x >>> 8),
				(byte)x
		};
	}

	public static byte[] getBytes(int x)
	{
		return new byte[] {
				(byte)(x >>> 24),
				(byte)(x >>> 16),
				(byte)(x >>> 8),
				(byte)x
		};
	}
	public static short toInt16(byte[] bytes, int index) throws Exception
	{
		if(bytes.length != 8) 
			throw new Exception("The length of the byte array must be at least 8 bytes long.");
		return (short)(
				(0xff & bytes[index]) << 8   |
				(0xff & bytes[index + 1]) << 0
		); 
	}

	public static int toInt32(byte[] bytes, int index) throws Exception
	{
		if(bytes.length != 4) 
			throw new Exception("The length of the byte array must be at least 4 bytes long.");
		return (int)(
				(int)(0xff & bytes[index]) << 56  |
				(int)(0xff & bytes[index + 1]) << 48  |
				(int)(0xff & bytes[index + 2]) << 40  |
				(int)(0xff & bytes[index + 3]) << 32 
		);
	}
}
