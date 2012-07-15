package sierra.messages.room;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class InitRoom implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{	
		try
		{
			if (Session.getRoomUser().InRoom)
				Session.getRoomUser().getCurrentRoom().LeaveRoom(Session);

			int RoomId = Request.popInt();

			ServerMessage Message = new ServerMessage();

			if (RoomEngine.RoomExists(RoomId) == false)
			{
				int OwnerId = Sierra.getDatabase().ReadInt("ownerid", "SELECT ownerid FROM rooms WHERE id = '" + RoomId + "'");
				String OwnerName = Sierra.getDatabase().ReadString("username", "SELECT username FROM members WHERE id = '" + OwnerId + "'");
				RoomEngine.AddRoom(RoomId, OwnerId, OwnerName);
			}

			Room Room = RoomEngine.GetById(RoomId);
			
			Session.getRoomUser().InRoom = true;
			Session.getRoomUser().Room = Room;
			Session.getRoomUser().Room.SessionList.add(Session);
			Session.getRoomUser().X = Session.getRoomUser().getModel().DoorX;
			Session.getRoomUser().Y = Session.getRoomUser().getModel().DoorY;
			Session.getRoomUser().Height = Session.getRoomUser().getModel().DoorZ;
			Session.getRoomUser().BodyRotation = Session.getRoomUser().getModel().DoorRot;

			Message.Initialize(Outgoing.InitRoomProcess);
			Session.Send(Message);

			Message.Initialize(Outgoing.ModelAndId);
			Message.AppendString(Room.Model);
			Message.AppendInt32(Room.Id);
			Session.Send(Message);

			if (!Room.Wall.equals("0"))
			{
				Message.Initialize(Outgoing.Papers);
				Message.AppendString("wallpaper");
				Message.AppendString(Room.Wall);
				Session.Send(Message);
			}

			if (!Room.Floor.equals("0"))
			{
				Message.Initialize(Outgoing.Papers);
				Message.AppendString("floor");
				Message.AppendString(Room.Floor);
				Session.Send(Message);
			}

			Message.Initialize(Outgoing.Papers);
			Message.AppendString("landscape");
			Message.AppendString(Room.Landscape);
			Session.Send(Message);

			if (Room.getUserHasRights(Session.getHabbo().Id))
			{
				Message.Initialize(Outgoing.LoadRightsOnRoom);
				Message.AppendInt32(4);
				Session.Send(Message);
			}

			Message.Initialize(Outgoing.RoomEvents);
			Message.AppendString("-1");
			Session.Send(Message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
