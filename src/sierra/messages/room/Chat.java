package sierra.messages.room;

import sierra.Sierra;
import sierra.sLogger;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;
import sierra.messages.navigator.LeaveRoom;

public class Chat implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		if (Session.getRoomUser().InRoom)
		{
			String ToSay = Request.popFixedString();

			if (!ToSay.startsWith(":"))
			{
				ServerMessage Message = new ServerMessage();

				Message.Initialize(Outgoing.Talk);
				Message.AppendInt32(Session.getHabbo().Id);
				Message.AppendString(ToSay);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Session.getRoomUser().getCurrentRoom().Send(Message);

				sLogger.getLogger(Chat.class).info("[Room Id: " + Session.getRoomUser().getCurrentRoom().Id + "] [" + Session.getHabbo().Username + "] -> "+ ToSay);
			}
			else
			{
				GetCommands(Session, ToSay, ToSay.split(" "));
			}
		}
	}
	public void GetCommands(Session Session, String ToSay, String[] Arguments)
	{
		Boolean UsedCommand = false;
		ServerMessage Message = new ServerMessage();

		String Command = Arguments[0];

		if (Command.equals(":about"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_login"))
			{
				StringBuilder Builder = new StringBuilder();
				{
					Builder.append("Sierra Emulator\r\n");
					Builder.append("----\r\n");
					Builder.append("Created by Quackster\r\n");
					Builder.append("\r\n");
					Builder.append("This emulator is created in JAVA!\r\n");
				}

				Message.Initialize(Outgoing.Alert);
				Message.AppendString(Builder.toString());
				Message.AppendString("");
				Session.Send(Message);

				UsedCommand = true;
			}
		}
		if (Command.equals(":commands"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_login"))
			{
				StringBuilder Builder = new StringBuilder();
				{
					Builder.append("Sierra Emulator Commands\n");
					Builder.append("----\n");
					Builder.append(":commands\n");
					Builder.append(":about\n");
				}

				Message.Initialize(Outgoing.Alert);
				Message.AppendString(Builder.toString());
				Message.AppendString("");
				Session.Send(Message);

				UsedCommand = true;
			}
		}
		if (Command.equals(":alert") || Command.equals(":ha"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_mod"))
			{
				for (Session User : Sierra.getServer().getActiveConnections().getSessions().values())
				{
					Message.Initialize(Outgoing.Alert);
					Message.AppendString(ToSay.substring(Arguments[0].length() + 1) + "\n\n" 
							+ "Yours sincerely, " + Session.getHabbo().Username);
					Message.AppendString("");
					User.Send(Message);

					UsedCommand = true;
				}
			}
			else
			{
				this.NoPermissions(Session);
			}
		}
		if (Command.equals(":kickeveryone"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_mod"))
			{
				for (Session User : Sierra.getServer().getActiveConnections().getSessions().values())
				{
					if (User.getRoomUser().InRoom)
					{
						new LeaveRoom().Parse(User, null);

						UsedCommand = true;
					}
				}
			}
			else
			{
				this.NoPermissions(Session);
			}
		}
		if (Command.equals(":kickall"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_mod"))
			{
				for (Session User : Session.getRoomUser().getCurrentRoom().SessionList)
				{
					if (!User.getHabbo().hasStaffPermission("fuse_mod"))
					{
						new LeaveRoom().Parse(User, null);

						UsedCommand = true;
					}
				}
			}
			else
			{
				this.NoPermissions(Session);
			}
		}
		if (Command.equals(":disconnect") || Command.equals(":kick"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_mod"))
			{
				Session User = Sierra.getServer().getActiveConnections().GetUserByName(Arguments[1]);

				if (User != null)
				{
					new LeaveRoom().Parse(User, null);

					UsedCommand = true;
				}
			}
			else
			{
				this.NoPermissions(Session);
			}
		}
		else if (Command.equals(":troll"))
		{
			if (Session.getHabbo().hasStaffPermission("fuse_admin"))
			{
				Session User = Sierra.getServer().getActiveConnections().GetUserByName(Arguments[1]);

				if (User != null)
				{
					Message.Initialize(Outgoing.Talk);
					Message.AppendInt32(User.getHabbo().Id);
					Message.AppendString(ToSay.substring(Arguments[0].length() + Arguments[1].length() + 2));
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Session.getRoomUser().getCurrentRoom().Send(Message);

					UsedCommand = true;
				}
			}
			else
			{
				this.NoPermissions(Session);
			}
		}
		else
		{
			if (!UsedCommand)
			{
				int SmileState = 0;

				if (ToSay.contains(":)") || ToSay.contains("=)") || ToSay.contains(":D") || ToSay.contains("=D"))
					SmileState = 1;

				if (ToSay.contains(":@") || ToSay.contains(">:(") || ToSay.contains(">:@"))
					SmileState = 2;

				if (ToSay.contains(":o") || ToSay.contains("D:"))
					SmileState = 3; 

				if (ToSay.contains(":(") || ToSay.contains(":'(") || ToSay.contains("=(") || ToSay.contains("='("))
					SmileState = 4;

				Message.Initialize(Outgoing.Talk);
				Message.AppendInt32(Session.getHabbo().Id);
				Message.AppendString(ToSay);
				Message.AppendInt32(SmileState);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Session.getRoomUser().getCurrentRoom().Send(Message);
			}
		}
	}
	public void NoPermissions(Session Session)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.Alert);
		Message.AppendString("Access denied, please fuck off.");
		Message.AppendString("");
		Session.Send(Message);
	}
}
