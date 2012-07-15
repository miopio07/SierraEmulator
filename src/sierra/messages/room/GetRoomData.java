package sierra.messages.room;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class GetRoomData implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * If the user isn't in the room we cancel the action
		 */
		if (!Session.getRoomUser().InRoom)
			return;
		
		/*
		 * If the user has doesn't have rights
		 */
		if (!Session.getRoomUser().getCurrentRoom().getUserHasRights(Session.getHabbo().Id))
			return;
		
		/*
		 * Get Room instance.
		 */
		Room Req = RoomEngine.GetById(Request.popInt());
		
		/*
		 * Send the room packet
		 */
		
        ServerMessage Message = new ServerMessage();
        
        Message.Initialize(Outgoing.SerializeRoomData);
        Message.AppendInt32(Req.Id);
        Message.AppendString(Req.Name);
        Message.AppendString(Req.Description);
        Message.AppendInt32(Req.State);
        Message.AppendInt32(Req.Category);
        Message.AppendInt32(25);
        Message.AppendInt32((Session.getRoomUser().getModel().MapSizeX >= 20 || Session.getRoomUser().getModel().MapSizeY >= 20) ? 50 : 25);
        Message.AppendInt32(0); // tags count
        Message.AppendInt32(0); // Room Blocking Disabled
        Message.AppendInt32(0); // allowed pets
        Message.AppendInt32(0); // pets can eat
        Message.AppendInt32(0); // users walkable
        Message.AppendInt32(0); // hide walls
        Message.AppendInt32(1); // Walls Type
        Message.AppendInt32(1); // Floors Type
        Session.Send(Message);
	}

}
