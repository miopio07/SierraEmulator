package sierra.messages.user;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadClub implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Club = new ServerMessage();
		
		Club.Initialize(Outgoing.ClubData);
		 Club.AppendString("club_habbo");
        Club.AppendInt32(1); // DaysLeft
        Club.AppendInt32(1); // Months Left
        Club.AppendInt32(0); // Years left wtf?
        Club.AppendInt32(2); // VIP (1 = no / 2 = yes)
        Club.AppendBoolean(false);
        Club.AppendBoolean(true);
        Club.AppendInt32(0);
        Club.AppendInt32(1); // Days I have
        Club.AppendInt32(0);
		Session.Send(Club);
	}

}
