package sierra.messages;

import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;

public interface MessageEvent
{
	void Parse(Session Session, ClientMessage Request);
}
