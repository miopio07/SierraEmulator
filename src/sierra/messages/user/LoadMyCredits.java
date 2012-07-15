package sierra.messages.user;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadMyCredits implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Message = new ServerMessage();
		
		Message.Initialize(Outgoing.SendCredits);
		Message.AppendString("" + Session.getHabbo().Credits);
		Session.Send(Message);
	}

}
