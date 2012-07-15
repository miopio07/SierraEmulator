package sierra.messages.user;

import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.messages.MessageEvent;

public class PingSession implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		Session.PingOK = true;
	}

}
