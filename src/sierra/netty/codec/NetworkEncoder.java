package sierra.netty.codec;


import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import sierra.message.builder.ServerMessage;


public class NetworkEncoder extends SimpleChannelHandler {
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			if (e.getMessage() instanceof String)
			{
				Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer(
						(String) e.getMessage(), Charset.forName("UTF-8")));
			}
			else
			{
				ServerMessage msg = (ServerMessage) e.getMessage();
				Channels.write(ctx, e.getFuture(), msg.get());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
