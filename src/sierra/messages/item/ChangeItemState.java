package sierra.messages.item;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class ChangeItemState implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * Check rights
		 */
		if (!Session.getRoomUser().getCurrentRoom().getUserHasRights(Session.getHabbo().Id))
			return;

		int Id = Request.popInt();

		FloorItem Item = Session.getRoomUser().getCurrentRoom().getFloorItem(Id);

		if (Item == null)
			return;

		Item.ChangeState();
		Item.SaveState();
		
		/*
		 * Build server packet
		 */
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.UpdateFloorExtraData);
		Message.AppendString(Item.Id);
		Message.AppendInt32(0);
		Message.AppendString(Item.ExtraData);
		Session.getRoomUser().getCurrentRoom().Send(Message);
	}

}
