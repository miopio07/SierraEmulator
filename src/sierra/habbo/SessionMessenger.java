package sierra.habbo;

import java.util.List;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbohotel.messenger.buddies.Buddy;
import sierra.habbohotel.messenger.buddies.BuddyEngine;
import sierra.message.builder.ServerMessage;

public class SessionMessenger
{
	private Session Session;
	private List<Buddy> Buddies;

	public SessionMessenger(Session User)
	{
		Buddies = BuddyEngine.GenerateBuddyList(User.getHabbo().Id);
		Session = User;
	}

	public List<Buddy> getBuddies()
	{
		return Buddies;
	}

	public void UpdateStatus(Boolean Online)
	{

		for (Buddy Buddy : Session.getMessenger().getBuddies())
		{
			Buddy.BuddyOnline();
		}

		try {
			ServerMessage Message = new ServerMessage();

			Message.Initialize(Outgoing.UpdateFriendState);
			Message.AppendInt32(0);
			Message.AppendInt32(1);
			Message.AppendInt32(0);
			Message.AppendInt32(Session.getHabbo().Id);
			Message.AppendString(Session.getHabbo().Username);
			Message.AppendInt32(1);
			Message.AppendBoolean(Online);
			Message.AppendBoolean(Session.getRoomUser() != null ? Session.getRoomUser().InRoom : false);
			Message.AppendString(Session.getHabbo().Figure);
			Message.AppendInt32(0);
			Message.AppendString(Session.getHabbo().Motto);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Send(Session, Message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void Send(Session Session, ServerMessage Message)
	{
		for (Buddy Buddy : Session.getMessenger().getBuddies())
		{
			for (Session sSession : Sierra.getServer().getActiveConnections().getSessions().values())
			{
				if (Session.getHabbo() != null && Buddy != null)
				{
					if (Buddy.getId() == sSession.getHabbo().Id)
					{
						sSession.Send(Message);
					}
				}
			}
		}
	}
}
