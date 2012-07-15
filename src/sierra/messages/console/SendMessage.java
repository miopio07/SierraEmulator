package sierra.messages.console;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbo.SessionManager;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;
import sierra.utils.UserInputFilter;

public class SendMessage implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * Get friend id
		 */
		int FriendId = Request.popInt();

		/*
		 * Get console message
		 */
		String ConsoleMessage = UserInputFilter.filterString(Request.popFixedString(), true);

		/*
		 * New server message instance
		 */
		ServerMessage Message = new ServerMessage();

		/*
		 * Build packet
		 */

		SessionManager Sessions = Sierra.getServer().getActiveConnections();

		if (Sessions.UserByIdOnline(FriendId))
		{
			Message.Initialize(Outgoing.TalkOnChat);
			Message.AppendInt32(Session.getHabbo().Id);
			Message.AppendString(ConsoleMessage);
			Sessions.GetUserById(FriendId).Send(Message);
		}
		else
		{
			Message.Initialize(Outgoing.TalkOnChat);
			Message.AppendInt32(FriendId);
			Message.AppendString("Your friend is offline.");
			Session.Send(Message);
		}
	}
	/*public void logMessage(int FriendId, String Message)
	{
		String Username = 
				Sierra.getServer().getActiveConnections().UserByIdOnline(FriendId) ? 
						Sierra.getServer().getActiveConnections().GetUserById(FriendId).getHabbo().Username :
							Sierra.getDatabase().ReadString("username", "SELECT username FROM members WHERE id = '" + FriendId + "'");

						//Logging.writeLine(Username);
	}*/
}
