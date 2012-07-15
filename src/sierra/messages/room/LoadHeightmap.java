package sierra.messages.room;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.Room;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadHeightmap implements MessageEvent
{

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		Room Room = Session.getRoomUser().getCurrentRoom();
		ServerMessage Message = new ServerMessage();

		if (Room.equals(null))
		{
			Session.alertUser("Unknown Room.");
			Session.getChannel().close();
		}
		else
		{
			Message.Initialize(Outgoing.Heightmap1);
			Message.AppendString(Session.getRoomUser().getModel().Heightmap());
            Session.Send(Message);
            
			Message.Initialize(Outgoing.Heightmap2);
			Message.AppendString(Session.getRoomUser().getModel().RelativeHeightmap());
            Session.Send(Message);
            
            new RoomFinished().Parse(Session, Request);
		}

	}

}
