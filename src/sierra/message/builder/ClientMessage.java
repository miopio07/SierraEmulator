package sierra.message.builder;


import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import sierra.utils.BitConverter;

public class ClientMessage implements Cloneable
{
	private Short Header;
	private ChannelBuffer Buffer;

	public ClientMessage(Short id, ChannelBuffer buffer)
	{
		this.Header = id;
		this.Buffer = (buffer == null || buffer.readableBytes() == 0) ? ChannelBuffers.EMPTY_BUFFER : buffer;
	}

	public ChannelBuffer readFixedValue() {
		
		//Turn to byte buffer for header
		ByteBuffer bb = ByteBuffer.wrap(Buffer.readBytes(2).array());

		//Create a short buffer from byte buffer
		ShortBuffer sb = bb.asShortBuffer();
		
		return Buffer.readBytes(sb.get());
	}

	public Integer popInt()
	{
		try
		{
			return BitConverter.toInt32(Buffer.readBytes(4).array(), 0);
		}
		catch (Exception e)
		{
		}
		return 0;
	}

	public int popFixedInt()
	{
		int i = 0;
		String s = popFixedString();

		i = Integer.parseInt(s);

		return i;
	}


	public String popFixedString()
	{
		return new String(readFixedValue().toString(Charset.defaultCharset()));
	}

	public Short GetHeader()
	{
		return this.Header;
	}

	public String getBodyString()
	{
		String str = new String(Buffer.toString(Charset.defaultCharset()));
		
		String consoleText = str;
		
		for (int i = 0; i < 13; i++) { 
			consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
		}
		
		return consoleText;
	}

	public int getCurrentLength()
	{
		return Buffer.readableBytes();
	}
}
