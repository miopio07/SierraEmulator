package sierra.messages.snowstorm;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class InitSnowStorm implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.SnowStorm);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendInt32(10);
		Session.Send(Message);
	}

}
