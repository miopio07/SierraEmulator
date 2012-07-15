package sierra.messages.console;

import java.util.List;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.messenger.buddies.Buddy;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class GetFriends implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage msg)
	{
		/*
		 * Get own categories (if any)
		 */
		//List<MessengerCat> OwnCats = MessengerCatEngine.GenerateCategories(Session.GetHabbo().Role);

		/*
		 * Get your friends
		 */
		List<Buddy> Buddies = Session.getMessenger().getBuddies();

		/*
		 * Build packet list.
		 */
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.LoadFriends);
		Message.AppendInt32(1100);
		Message.AppendInt32(300);
		Message.AppendInt32(800);
		Message.AppendInt32(1100);

		/*
		 * Append categories
		 */
		Message.AppendInt32(0);
		/*
		for (MessengerCat Cat : OwnCats)
		{
			Message.AppendInt32(Cat.Id);
			Message.AppendString(Cat.Label);
		}*

		/*
		 * Load friends
		 */

		Message.AppendInt32(Buddies.size());

		for (Buddy Friend : Buddies)
		{
			Message.AppendInt32(Friend.getHabbo().Id);
			Message.AppendString(Friend.getHabbo().Username);
			Message.AppendInt32(1);
			Message.AppendBoolean(Friend.getOnline());
			Message.AppendBoolean(Friend.getOnline() ? (Sierra.getServer().getActiveConnections().GetUserById(Friend.getHabbo().Id).getRoomUser() != null ? Sierra.getServer().getActiveConnections().GetUserById(Friend.getHabbo().Id).getRoomUser().InRoom : false) : false);
			Message.AppendString(Friend.getHabbo().Figure);
			Message.AppendInt32(0);
			Message.AppendString(Friend.getHabbo().Motto);
			Message.AppendString("14-08-2011 09:55:52");
			Message.AppendInt32(0);
		}
		Session.Send(Message);
	}
}
