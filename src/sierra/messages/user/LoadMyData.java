package sierra.messages.user;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadMyData implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.UserInfo);
        Message.AppendInt32(Session.getHabbo().Id);
        Message.AppendString(Session.getHabbo().Username);
        Message.AppendString(Session.getHabbo().Figure);
        Message.AppendString(Session.getHabbo().Gender.toUpperCase());
        Message.AppendString(Session.getHabbo().Motto);
        Message.AppendString(""); // fbname, blablabla
        Message.AppendBoolean(false);
        Message.AppendInt32(3); // Respect
        Message.AppendInt32(3); // Respect to give
        Message.AppendInt32(3); // to give to pets
        Message.AppendBoolean(true);
		Session.Send(Message);
	}

}
