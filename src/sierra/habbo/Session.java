package sierra.habbo;

import org.jboss.netty.channel.Channel;

import sierra.Sierra;
import sierra.sLogger;
import sierra.composers.Outgoing;
import sierra.habbo.users.Habbo;
import sierra.habbo.users.RoomUser;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;

public class Session
{
	public Boolean _Authenticated;
	public Boolean PingOK;
	
	private Channel _Channel;
	private Habbo _Habbo;
	private RoomUser _RoomUser;
	private SessionInventory _Inventory;
	private SessionMessenger _Messenger;

	public Session(Channel Channel)
	{
		this._Channel = Channel;
		this._Habbo = new Habbo();
		this._RoomUser = new RoomUser(this);
		this._Authenticated = false;
		this.PingOK = true;
	}
	public Session(String Username)
	{
		this._Habbo = SetSessionInfo.GenerateHabboData(Username);
	}
	public Session(int Id)
	{
		this._Habbo = SetSessionInfo.GenerateHabboData(Id);
	}
	public void setNewHabbo()
	{
		this._Habbo = new Habbo();
	}
	public void setNewInventory()
	{
		this._Inventory = new SessionInventory(this);
	}
	public void setNewMessenger()
	{
		this._Messenger = new SessionMessenger(this);
	}
	public void alertBanned(String Alert)
	{
		this.Send(new ServerMessage().Initialize(Outgoing.YouAreBanned)
				.AppendString(Alert));
	}
	public void alertUser(String Msg)
	{
		this.Send(new ServerMessage().Initialize(Outgoing.Alert)
				.AppendString(Msg)
				.AppendString(""));
	}
	public void AlertUserWithLink(String Msg, String Link)
	{
		this.Send(new ServerMessage().Initialize(Outgoing.Alert)
				.AppendString(Msg)
				.AppendString(Link));
	}
	public void dispose()
	{
		if (_RoomUser.InRoom)
		{
			this._RoomUser.getCurrentRoom().LeaveRoom(this);
		}

		RoomEngine.RemoveByOwnerId(this.getHabbo().Id);

		this._Habbo = null;
		this._Channel = null;
		this._RoomUser = null;
	}
	public void parseMessage(ClientMessage msg)
	{
		sLogger Logger = sLogger.getLogger(Session.class);
		Logger.date("(" + this.getHabbo().Username + ") ---> RECEIVED " + msg.GetHeader() + " / "+ msg.getBodyString());
		
		if (Sierra.getServer().getPacketMessages().contains(msg.GetHeader()))
		{
			Sierra.getServer().getPacketMessages().get(msg.GetHeader()).Parse(this, msg);
		}
		else
		{
			// Not coded yet ;3
		}
	}
	public Channel getChannel()
	{
		return _Channel;
	}
	public Habbo getHabbo()
	{
		return _Habbo;
	}
	public SessionInventory getInventory()
	{
		return _Inventory;
	}
	public SessionMessenger getMessenger()
	{
		return _Messenger;
	}
	public RoomUser getRoomUser()
	{
		return _RoomUser;
	}
	public String getIpAddress()
	{
		return _Channel.getRemoteAddress().toString().replace("/", "").split(":")[0];
	}
	public void Send(ServerMessage msg)
	{
		sLogger Logger = sLogger.getLogger(Session.class);
		Logger.date("(" + this.getHabbo().Username + ") ---> SENT " + msg.getBodyString());
		_Channel.write(msg);
	}
	public Habbo setHabbo(Habbo habbo)
	{
		return _Habbo = habbo;
	}
}
