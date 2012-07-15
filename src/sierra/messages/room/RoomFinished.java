package sierra.messages.room;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.habbohotel.room.wallitems.WallItem;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class RoomFinished implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Message;

		Message = new ServerMessage();

		Message.Initialize(Outgoing.FloorItems);
		Message.AppendInt32(1);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
		Message.AppendString(Session.getRoomUser().getCurrentRoom().OwnerName);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().FloorItems.size());
		for (FloorItem Item : Session.getRoomUser().getCurrentRoom().FloorItems)
		{
			Message.AppendBody(Item);
			Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
		}
		Session.Send(Message);

		Message.Initialize(Outgoing.WallItems);
		Message.AppendInt32(1);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
		Message.AppendString(Session.getRoomUser().getCurrentRoom().OwnerName);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().WallItems.size());
		for (WallItem Item : Session.getRoomUser().getCurrentRoom().WallItems)
		{
			Message.AppendBody(Item);
			Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
		}
		Session.Send(Message);


		Message.Initialize(Outgoing.BeforeUsers);
		Message.AppendInt32(1);
		Message.AppendInt32(Session.getHabbo().Id);
		Message.AppendString(Session.getHabbo().Username);
		Message.AppendInt32(0);
		Session.Send(Message);

		Session.getRoomUser().getCurrentRoom().Send(Session.getRoomUser().getCurrentRoom().SerializeRoomUsers(Session));
		Session.getRoomUser().getCurrentRoom().Send(Session.getRoomUser().getCurrentRoom().SerializeRoomStatus(Session));

		Message.Initialize(Outgoing.VipWallsAndFloors);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendBoolean(false);
		Session.Send(Message);

		Message.Initialize(Outgoing.RoomPanel);
		Message.AppendBoolean(true);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().Id);
		Message.AppendBoolean(true);
		Session.Send(Message);

        Message.Initialize(Outgoing.RoomData);
        Message.AppendBoolean(true);
        Message.AppendInt32(Session.getRoomUser().getCurrentRoom().Id);
        Message.AppendBoolean(false);
        Message.AppendString(Session.getRoomUser().getCurrentRoom().Name);
        Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
        Message.AppendString(Session.getRoomUser().getCurrentRoom().OwnerName);
        Message.AppendInt32(0);
        Message.AppendInt32(Session.getRoomUser().getCurrentRoom().SessionList.size());
        Message.AppendInt32(25);
        Message.AppendString(Session.getRoomUser().getCurrentRoom().Description);
        Message.AppendInt32(0);
        Message.AppendInt32(1); // trading
        Message.AppendInt32(Session.getRoomUser().getCurrentRoom().Score);
        Message.AppendInt32(Session.getRoomUser().getCurrentRoom().Category);
        Message.AppendString("");
        Message.AppendInt32(0);
        Message.AppendInt32(0);
        Message.AppendInt32(0); // tags
        Message.AppendInt32(0);
        Message.AppendInt32(0);
        Message.AppendInt32(0);
        Message.AppendBoolean(true);
        Message.AppendBoolean(true);
        Message.AppendBoolean(false);
        Message.AppendBoolean(false);
        Message.AppendBoolean(false);
        Session.Send(Message);
	}

}
