package sierra.messages.navigator;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class CanCreateRoom implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Message = new ServerMessage();
		
		Message.Initialize(Outgoing.CanCreateNewRoom);
		Message.AppendInt32(0);
		Message.AppendInt32(25);
		Session.Send(Message);
	}
}
