package sierra.messages.item;

import java.sql.PreparedStatement;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class PickUpItem implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		try
		{
			/*
			 * Remove crap
			 */
			Request.popInt();

			/*
			 * Get furni id
			 */
			int Id = Request.popInt();

			/*
			 * Get furniture instance
			 */

			FloorItem Item = Session.getRoomUser().getCurrentRoom().getFloorItem(Id);

			/*
			 * If floor item
			 */

			if (Item != null)
			{
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("INSERT INTO members_inventory (`owner`, `itemid`, `label`) VALUES (?, ?, ?);");
				{
					Statement.setInt(1, Session.getHabbo().Id);
					Statement.setInt(2, Item.GetBaseItem().Id);
					Statement.setString(3, Item.GetBaseItem().PublicName);
					Statement.execute();
				}
				
	        	ServerMessage Message = new ServerMessage();
	        	
	        	Message.Initialize(Outgoing.RemoveFloorItem);
	        	Message.AppendString(Item.Id);
	        	Message.AppendInt32(0);
	        	Message.AppendInt32(Session.getHabbo().Id);
	        	Session.getRoomUser().getCurrentRoom().Send(Message);
	        	
	        	Message.Initialize(Outgoing.RemoveItem);
	        	Message.AppendString(Item.Id);
	        	Session.getRoomUser().getCurrentRoom().Send(Message);
	        	
	        	/*
	        	 * Remove sql data from floor items.
	        	 */
	        	
	        	Sierra.getDatabase().ExecuteQuery("DELETE FROM room_flooritems WHERE id = '" + Item.Id + "'").execute();
	        	
	        	/*
	        	 * Remove cache data
	        	 */
	        	
	        	Session.getRoomUser().getCurrentRoom().FloorItems.remove(Item);
	        	
	        	/*
	        	 * Update inventory
	        	 */
	        	
				Message.Initialize(Outgoing.UpdateInventory);
				Session.Send(Message);

				Session.getInventory().AddSingle(Item.Id, Item.GetBaseItem().PublicName, Item.GetBaseItem().Id);
				
				new GetInventory().Parse(Session, null);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
