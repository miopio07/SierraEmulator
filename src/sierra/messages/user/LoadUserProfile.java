package sierra.messages.user;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbo.SetSessionInfo;
import sierra.habbo.users.Habbo;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadUserProfile implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		Integer Id = Request.popInt();

		Habbo User = SetSessionInfo.GenerateHabboData(Id);

		if (!User.equals(null))
		{
			ServerMessage Message = new ServerMessage();

			Message.Initialize(Outgoing.LoadProfile);
			Message.AppendInt32(User.Id);
			Message.AppendString(User.Username);
			Message.AppendString(User.Figure);
			Message.AppendString(User.Motto);
			Message.AppendString("1-1-1969");
			Message.AppendInt32(0);			// Achievement Score
			Message.AppendInt32(getShortFriendList(User.Id).size()); 		// Friend Count
			Message.AppendBoolean(getShortFriendList(User.Id).contains(Session.getHabbo().Id)); 	// Is friend
			Message.AppendBoolean(false); 	// Friend request sent
			Message.AppendBoolean(Sierra.getServer().getActiveConnections().UserByIdOnline(User.Id)); 	// Is online
			Message.AppendInt32(0); 		// ?
			Message.AppendInt32(0);			// ?
			Session.Send(Message);
		}
		else
		{
			return;
		}
	}
	public List<Integer> getShortFriendList(int UserId)
	{
		List<Integer> Friends = new ArrayList<Integer>();
		
		try
		{
			ResultSet Row = Sierra.getDatabase().ExecuteQuery("SELECT * FROM messenger_buddies WHERE user_id = '" + UserId + "'").executeQuery();
			
			while (Row.next())
			{
				Friends.add(Row.getInt("friend_id"));
			}
		}
		catch (Exception e)
		{
		}
		return Friends;
	}
}
