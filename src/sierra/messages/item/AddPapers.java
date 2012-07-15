package sierra.messages.item;

import java.sql.SQLException;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.inventory.Inventory;
import sierra.habbohotel.room.wallitems.DecorationType;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class AddPapers implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		try
		{
			/*
			 * If the user has right
			 */
			if (!Session.getRoomUser().getCurrentRoom().getUserHasRights(Session.getHabbo().Id))
				return;


			/*
			 * Get the retrieved id from wallpaper
			 */
			Inventory Item = Session.getInventory().GetWallInventoryItem(Request.popInt());

			DecorationType type = null;
			if (Item.Label.contains("wall"))
				type = DecorationType.WALL;
			if (Item.Label.contains("landscape"))
				type = DecorationType.LANDSCAPE;
			if (Item.Label.contains("floor"))
				type = DecorationType.FLOOR;


			/*
			 * Get extra data
			 */
			String ExtraData = Item.Label.split("_")[2];

			/*
			 * New server message instance
			 */
			ServerMessage Message = new ServerMessage();

			/*
			 * Parse it and update rooms as such
			 */
			if (type == DecorationType.WALL)
			{
				Session.getRoomUser().getCurrentRoom().Wall = ExtraData;

				Message.Initialize(Outgoing.Papers);
				Message.AppendString("wallpaper");
				Message.AppendString(Session.getRoomUser().getCurrentRoom().Wall);
				Session.Send(Message);

				Sierra.getDatabase().ExecuteQuery("UPDATE rooms SET wallpaper = '" + ExtraData + "' WHERE id = '" + Session.getRoomUser().getCurrentRoom().Id + "'").execute();
			}
			if (type == DecorationType.LANDSCAPE)
			{
				Session.getRoomUser().getCurrentRoom().Landscape = ExtraData;

				Message.Initialize(Outgoing.Papers);
				Message.AppendString("landscape");
				Message.AppendString(Session.getRoomUser().getCurrentRoom().Landscape);
				Session.Send(Message);

				Sierra.getDatabase().ExecuteQuery("UPDATE rooms SET outside = '" + ExtraData + "' WHERE id = '" + Session.getRoomUser().getCurrentRoom().Id + "'").execute();
			}
			if (type == DecorationType.FLOOR)
			{
				Session.getRoomUser().getCurrentRoom().Floor = ExtraData;

				Message.Initialize(Outgoing.Papers);
				Message.AppendString("floor");
				Message.AppendString(Session.getRoomUser().getCurrentRoom().Floor);
				Session.Send(Message);

				Sierra.getDatabase().ExecuteQuery("UPDATE rooms SET floor = '" + ExtraData + "' WHERE id = '" + Session.getRoomUser().getCurrentRoom().Id + "'").execute();
			}
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
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
