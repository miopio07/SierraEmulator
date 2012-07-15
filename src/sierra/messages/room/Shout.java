package sierra.messages.room;

import sierra.sLogger;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class Shout implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		if (Session.getRoomUser().InRoom)
		{
			String ToSay = Request.popFixedString();

			int SmileState = 0;

			if (ToSay.contains(":)") || ToSay.contains("=)") || ToSay.contains(":D") || ToSay.contains("=D"))
				SmileState = 1;

			if (ToSay.contains(":@") || ToSay.contains(">:(") || ToSay.contains(">:@"))
				SmileState = 2;

			if (ToSay.contains(":o") || ToSay.contains("D:"))
				SmileState = 3; 

			if (ToSay.contains(":(") || ToSay.contains(":'(") || ToSay.contains("=(") || ToSay.contains("='("))
				SmileState = 4;
			
			ServerMessage Message = new ServerMessage();

			Message.Initialize(Outgoing.Shout);
			Message.AppendInt32(Session.getHabbo().Id);
			Message.AppendString(ToSay);
			Message.AppendInt32(SmileState);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Session.getRoomUser().getCurrentRoom().Send(Message);

			sLogger.getLogger(Chat.class).info("[Room Id: " + Session.getRoomUser().getCurrentRoom().Id + "] [" + Session.getHabbo().Username + "] -> "+ ToSay);
		}
	}

}
