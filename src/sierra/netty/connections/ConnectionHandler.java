package sierra.netty.connections;

import java.sql.PreparedStatement;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import sierra.Sierra;
import sierra.habbo.Session;
import sierra.habbohotel.UpdateOnline;
import sierra.message.builder.ClientMessage;

public class ConnectionHandler extends SimpleChannelHandler {

	
	@Override
	public void channelOpen(ChannelHandlerContext ctnx, ChannelStateEvent e)
	{
		if (!Sierra.getServer().getActiveConnections().addSession(ctnx.getChannel()))
		{
			ctnx.getChannel().disconnect(); // failed to connect
		}
	
	} 

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		Session mSession = Sierra.getServer().getActiveConnections().GetUserByChannel(ctx.getChannel());
		if (mSession != null)
		{
			if (mSession.getHabbo() != null)
			{
				/* 
				 * You're now off line to everyone!
				 */
				mSession.getMessenger().UpdateStatus(false);
				mSession.getRoomUser().IsSit = false;

				try
				{
					PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("UPDATE members SET online = ? WHERE id = ?");
					{
						Statement.setInt(1, 0);
						Statement.setInt(2, mSession.getHabbo().Id);
						Statement.execute();
					}
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
				
				/*
				 * Kill off
				 */
				mSession.dispose();
				mSession._Authenticated = false;
			}
		}
		Sierra.getServer().getActiveConnections().removeSession(ctx.getChannel());

		/*
		 * Update online
		 */
		UpdateOnline.SetCount();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		ctx.getChannel().close();
	}
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		try
		{
			ClientMessage msg = (ClientMessage)e.getMessage();

			if (Sierra.getServer().getActiveConnections().hasSession(ctx.getChannel()))
			{
				Sierra.getServer().getActiveConnections().GetUserByChannel(ctx.getChannel()).parseMessage(msg);
			}
			else {
				System.out.print("error ohno");
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
