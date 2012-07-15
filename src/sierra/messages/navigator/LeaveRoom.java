package sierra.messages.navigator;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LeaveRoom implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * If user is not in room we cancel the action
		 */
		if (!Session.getRoomUser().InRoom)
			return;
		else
		{
			/*
			 * User is no longer in room
			 */
			Session.getRoomUser().InRoom = false;
			Session.getRoomUser().IsSit = false;
			Session.getRoomUser().NeedsWalkUpdate = false;
			
			ServerMessage Message = new ServerMessage();

			/*
			 * Send begin to leave hotel room
			 */
			Message.Initialize(Outgoing.LeavingRoom);
			Message.AppendBoolean(false);
			//Session.Send(Message);

			/*
			 * Send user figure left
			 */
			Message.Initialize(Outgoing.LeaveRoom);
			Message.AppendString(Session.getHabbo().Id);
			Session.getRoomUser().getCurrentRoom().Send(Message);

			/*
			 * Send to hotel view
			 */
			Message.Initialize(Outgoing.HotelView);
			Session.Send(Message);

			/*
			 * Remove session from collection
			 */
			Session.getRoomUser().getCurrentRoom().SessionList.remove(Session);
			
			if (!Session.getRoomUser().getCurrentRoom().OwnerName.equals(Session.getHabbo().Username))
				RoomEngine.RemoveById(Session.getRoomUser().getCurrentRoom().Id);
			
			Session.getRoomUser().Room = null;
		}
	}

}
