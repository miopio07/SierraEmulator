package sierra.messages.item;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.inventory.Inventory;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class GetInventory implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.SendInventory);
		Message.AppendString("S");
		Message.AppendInt32(1);
		Message.AppendInt32(1);
		Message.AppendInt32(Session.getInventory().GetFloorItems().size());
		for (Inventory Item : Session.getInventory().GetFloorItems())
		{
			Message.AppendInt32(Item.Id);
			Message.AppendString(Item.GetBaseItem().Type.toUpperCase());
			Message.AppendInt32(Item.Id);
			Message.AppendInt32(Item.GetBaseItem().SpriteId);
			Message.AppendInt32(1);
			Message.AppendString("");
			Message.AppendInt32(0);
			Message.AppendBoolean(Item.GetBaseItem().AllowRecycle);
			Message.AppendBoolean(Item.GetBaseItem().AllowTrade);
			Message.AppendBoolean(Item.GetBaseItem().AllowInventoryStack);
			Message.AppendBoolean(Item.GetBaseItem().AllowMarketplaceSell);
			Message.AppendInt32(-1);
			Message.AppendString("");
			Message.AppendInt32(0);
		}
		Session.Send(Message);

		Message.Initialize(Outgoing.SendInventory);
		Message.AppendString("I");
		Message.AppendInt32(1);
		Message.AppendInt32(1);
		Message.AppendInt32(Session.getInventory().GetWallItems().size());
		for (Inventory Item : Session.getInventory().GetWallItems())
		{
			Message.AppendInt32(Item.Id);
			Message.AppendString(Item.GetBaseItem().Type.toUpperCase());
			Message.AppendInt32(Item.Id);
			Message.AppendInt32(Item.GetBaseItem().SpriteId);

			if (Item.Label.contains("floor") || Item.Label.contains("a2"))
				 Message.AppendInt32(3);
			else if (Item.Label.contains("wall"))
				Message.AppendInt32(2);
			else if (Item.Label.contains("land"))
				Message.AppendInt32(4);
			else
				Message.AppendInt32(1);
			Message.AppendInt32(0);
			if ((Item.Label.contains("floor") || Item.Label.contains("wall")) && Item.Label.split("_")[2] != null)
			{
				Message.AppendString(Item.Label.split("_")[2]);
			}
			else
			{
				Message.AppendString("");
			}
			Message.AppendBoolean(Item.GetBaseItem().AllowRecycle);
			Message.AppendBoolean(Item.GetBaseItem().AllowTrade);
			Message.AppendBoolean(Item.GetBaseItem().AllowInventoryStack);
			Message.AppendBoolean(Item.GetBaseItem().AllowMarketplaceSell);
			Message.AppendInt32(-1);
		}
		Session.Send(Message);

	}
}