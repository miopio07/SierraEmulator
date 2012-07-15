package sierra.netty.connections;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import sierra.habbo.SessionManager;
import sierra.messages.MessageHandler;
import sierra.netty.codec.NetworkDecoder;
import sierra.netty.codec.NetworkEncoder;

public class Connection
{
	private NioServerSocketChannelFactory Factory;
	private ServerBootstrap Bootstrap;

	private MessageHandler Messages;
	private SessionManager Clients;

	private int Port;

	public Connection(int port)
	{

		this.Factory = new NioServerSocketChannelFactory
				(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()
						);

		this.Bootstrap = new ServerBootstrap(Factory);
		this.Messages = new MessageHandler();
		this.Messages.register();
		this.Clients = new SessionManager();

		this.Port = port;

		SetupSocket();
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = this.Bootstrap.getPipeline();

		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder());
		pipeline.addLast("handler", new ConnectionHandler());
	}

	public boolean StartSocket()
	{
		try
		{

			this.Bootstrap.bind(new InetSocketAddress(Port));

		}
		catch (ChannelException ex)
		{
			return false;
		}

		return true;
	}


	public MessageHandler getPacketMessages()
	{
		return this.Messages;
	}

	public SessionManager getActiveConnections()
	{
		return this.Clients;
	}
}
