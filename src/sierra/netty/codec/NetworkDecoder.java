package sierra.netty.codec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import sierra.message.builder.ClientMessage;
import sierra.utils.BitConverter;

public class NetworkDecoder extends FrameDecoder
{
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
	{
		try 
		{
			//Header and length
			if (buffer.readableBytes() < 6)
			{
				return null;
			}

			//New byte array for length
			byte[] Length = buffer.readBytes(4).array();

			if (Length[0] == 60)
			{
				buffer.discardReadBytes();

				String policy = "<?xml version=\"1.0\"?>\r\n"
					+ "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n"
					+ "<cross-domain-policy>\r\n"
					+ "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n"
					+ "</cross-domain-policy>\0";

				channel.write(policy);
			}
			else
			{
				//Retrieve length in integer form
				Integer newLength = BitConverter.toInt32(Length, 0);

				//New channel buffer to read the packet from given length
				ChannelBuffer msg = buffer.readBytes(newLength);

				//Turn to byte buffer for header
				Short id = ByteBuffer.wrap(msg.readBytes(2).array()).asShortBuffer().get();

				return new ClientMessage(id, msg);
			}		
		}
		catch (Exception e)
		{
		}
		return null;
	}
	public String getBodyString(ChannelBuffer body)
	{
		return new String(body.toString(Charset.defaultCharset()));
	}
}
