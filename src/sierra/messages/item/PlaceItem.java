package sierra.messages.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.inventory.Inventory;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class PlaceItem implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		try
		{
			if (!Session.getRoomUser().getCurrentRoom().getUserHasRights(Session.getHabbo().Id))
				return;

			ServerMessage Message = new ServerMessage();

			String PlaceData = Request.popFixedString();
			String[] Bits = PlaceData.split(" ");
			Integer ItemId = Integer.parseInt(Bits[0]);

			if (Bits[1].startsWith(":"))
			{			
				// Get item from inventory
				Inventory Item = Session.getInventory().GetWallInventoryItem(ItemId);

				// Send to client :)
				Message.Initialize(Outgoing.SendWallItem);
				Message.AppendString(Item.Id);
				Message.AppendInt32(Item.GetBaseItem().SpriteId);
				Message.AppendString(Bits[1] + " " + Bits[2] + " " + Bits[3]);
				Message.AppendString("");
				Message.AppendInt32(Item.GetBaseItem().InteractionType.equals("default") ? 1 : 0);
				Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
				Message.AppendString(Session.getRoomUser().getCurrentRoom().OwnerName);
				Session.Send(Message);

				// Add to MySQL 8-)
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("INSERT INTO room_wallitems (`id`, `roomid`, `baseid`, `position`, `extradata`) VALUES (?, ?, ?, ?, ?)");

				/*
				 * Set the item data
				 */
				Statement.setInt(1, Item.Id);
				Statement.setInt(2, Session.getRoomUser().getCurrentRoom().Id);
				Statement.setInt(3, Item.GetBaseItem().Id);
				Statement.setString(4, Bits[1] + " " + Bits[2] + " " + Bits[3]);
				Statement.setString(5, "0");
				Statement.execute();

				/*
				 * Remove from inventory using mysql queries
				 */
				Sierra.getDatabase().ExecuteQuery("DELETE FROM members_inventory WHERE owner = '" + Session.getHabbo().Id + "'").execute();

				/*
				 * Reload inventory
				 */
				Session.getInventory().RemoveWallItem(Item.Id);

				/*
				 * Reload inventory packet
				 */
				Message.Initialize(Outgoing.UpdateInventory);
				Session.Send(Message);

				/*
				 * Send new inventory
				 */
				new GetInventory().Parse(Session, Request);

				/*
				 * Add item to room cache
				 */
				Session.getRoomUser().getCurrentRoom().AddWallItem(Item.Id, Item.GetBaseItem().Id, Bits[1] + " " + Bits[2] + " " + Bits[3], "0");
			}
			else
			{
				Integer X = Integer.parseInt(Bits[1]);
				Integer Y = Integer.parseInt(Bits[2]);
				Integer Rot = Integer.parseInt(Bits[3]);

				/*
				 * Get item from inventory
				 */
				Inventory Item = Session.getInventory().GetFloorInventoryItem(ItemId);

				float Height = (float)Session.getRoomUser().getModel().getTileHeight(X, Y);

				for (FloorItem stackItem : Session.getRoomUser().getCurrentRoom().getItemListByCoords(X, Y))
				{
					if (Item.Id != stackItem.Id)
					{
						if (stackItem.GetBaseItem().CanStack)
						{
							Height += stackItem.GetBaseItem().StackHeight;
						}
					}
				}
				
				int Id = 0;

				/*
				 *  Add to MySQL 8-)
				 */
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("INSERT INTO room_flooritems (`roomid`, `baseid`, `x`, `y`, `rotation`, `height`, `extra`) VALUES (?, ?, ?, ?, ?, ?, ?)");
				
				/*
				 * Set data
				 */
				Statement.setInt(1, Session.getRoomUser().getCurrentRoom().Id);
				Statement.setInt(2, Item.GetBaseItem().Id);
				Statement.setInt(3, X);
				Statement.setInt(4, Y);
				Statement.setInt(5, Rot);
				Statement.setFloat(6, Height);
				Statement.setString(7, "0");
				Statement.executeUpdate();

				ResultSet Keys = Statement.getGeneratedKeys();

				while (Keys.next())
					Id = Keys.getInt(1);
				
				/*
				 * Send new packet
				 */

				Message.Initialize(Outgoing.SendFloorItem);
				Message.AppendInt32(Id);
				Message.AppendInt32(Item.GetBaseItem().SpriteId);
				Message.AppendInt32(X);
				Message.AppendInt32(Y);
				Message.AppendInt32(Rot);
				Message.AppendString(Float.toString(Height)); // TODO: Height
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendString("0"); // TODO: Triggers
				Message.AppendInt32(-1);
				Message.AppendInt32(Item.GetBaseItem().InteractionType.equals("default") ? 1 : 0);
				Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
				Message.AppendString(Session.getRoomUser().getCurrentRoom().OwnerName);
				Session.getRoomUser().getCurrentRoom().Send(Message);

				/*
				 * Remove from inventory using mysql queries
				 */
				Statement = Sierra.getDatabase().ExecuteQuery("DELETE FROM members_inventory WHERE id = ?");
				Statement.setInt(1, Item.Id);
				Statement.execute();
				/*
				 * Reload inventory
				 */
				Session.getInventory().RemoveFloorItem(Item.Id);

				/*
				 * Reload inventory packet
				 */
				Message.Initialize(Outgoing.UpdateInventory);
				Session.Send(Message);

				/*
				 * Send new inventory
				 */
				new GetInventory().Parse(Session, Request);

				/*
				 * Add item to room cache
				 */
				Session.getRoomUser().getCurrentRoom().AddFloorItem(Id, Item.GetBaseItem().Id, X, Y, Rot, Height, "0");

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}