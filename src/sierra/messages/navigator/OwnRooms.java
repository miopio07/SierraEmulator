package sierra.messages.navigator;

import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class OwnRooms implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ConcurrentLinkedQueue<Room> Rooms = ListOwnRooms(Session);

		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.OwnRooms);
		Message.AppendInt32(5);
		Message.AppendString("");
		Message.AppendInt32(Rooms.size());
		{
			for (Room Room : Rooms)
			{
				Message.AppendInt32(Room.Id);
				Message.AppendBoolean(false);
				Message.AppendString(Room.Name);
				Message.AppendInt32(Room.OwnerId);
				Message.AppendString(Room.OwnerName);
				Message.AppendInt32(0);
				Message.AppendInt32(Room.SessionList.size());
				Message.AppendInt32(25);
				Message.AppendString(Room.Description);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
		        Message.AppendInt32(Room.Score);
		        Message.AppendInt32(Room.Category);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendString("");
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);

			}
			Message.AppendBoolean(false);
		}
		Session.Send(Message);

	}
	public ConcurrentLinkedQueue<Room> ListOwnRooms(Session Session)
	{
		ConcurrentLinkedQueue<Room> OwnRooms = new ConcurrentLinkedQueue<Room>();
		
		for (Room Room : RoomEngine.GetAllRooms())
		{
			if (Room.OwnerName.equals(Session.getHabbo().Username))
			{
				OwnRooms.add(Room);
			}
		}
		return OwnRooms;
	}
}
