package sierra.messages.user;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadBadges implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		// TODO Auto-generated method stub
		ServerMessage Profile = new ServerMessage();
        Profile.Initialize(Outgoing.LoadBadges);
        Profile.AppendInt32(0);   
        Profile.AppendInt32(0);
        Session.Send(Profile);
	}

}
