package sierra.messages.item;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class MoveRotateItem implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * Check rights
		 */
		if (!Session.getRoomUser().getCurrentRoom().getUserHasRights(Session.getHabbo().Id))
			return;

		/*
		 * Grab received coordinates
		 */
		int Id = Request.popInt();
		int _X = Request.popInt();
		int _Y = Request.popInt();
		int _R = Request.popInt();

		/*
		 * Get floor item 
		 */

		FloorItem Item = Session.getRoomUser().getCurrentRoom().getFloorItem(Id);

		if (Item == null)
			return;

		/*
		 * Set new floor coords
		 */

		Item.X = _X;
		Item.Y = _Y;
		Item.Rotation = _R;

		float Height = (float)Session.getRoomUser().getModel().getTileHeight(_X, _Y);

		for (FloorItem stackItem : Session.getRoomUser().getCurrentRoom().getItemListByCoords(_X, _Y))
		{
			if (Item.Id != stackItem.Id)
			{
				if (stackItem.GetBaseItem().CanStack)
				{
					Height += stackItem.GetBaseItem().StackHeight;
				}
			}
		}

		Item.fHeight = Height;

		/*
		 * Build server packet
		 */
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.UpdateFloorItem);
		Message.AppendInt32(Item.Id);
		Message.AppendInt32(Item.GetBaseItem().SpriteId);
		Message.AppendInt32(Item.X);
		Message.AppendInt32(Item.Y);
		Message.AppendInt32(Item.Rotation);
		Message.AppendString(Float.toString(Height));
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendString(Item.ExtraData);
		Message.AppendInt32(-1);
		Message.AppendInt32(Item.GetBaseItem().InteractionType.equals("default") ? 1 : 0);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().OwnerId);
		Session.getRoomUser().getCurrentRoom().Send(Message);

		List<Session> AffectedUsers = new ArrayList<Session>();

		for (Session User : Session.getRoomUser().getCurrentRoom().SessionList)
		{
			if (Session.getRoomUser().getCurrentRoom().getItemListByCoords(User.getRoomUser().X, User.getRoomUser().Y).size() != 0)
				AffectedUsers.add(User);
		}

		for (Session AffectedUser : AffectedUsers)
		{
			if (Item.GetBaseItem().CanSit)
			{
				AffectedUser.getRoomUser().IsSit = true;
				AffectedUser.getRoomUser().BodyRotation = Item.Rotation;
				AffectedUser.getRoomUser().SendStatus("sit 1.0");
			}
			else
			{
				AffectedUser.getRoomUser().IsSit = false;
				AffectedUser.getRoomUser().SendStatus("");
			}
		}

		try
		{
			/*
			 * Update SQL
			 */

			PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("UPDATE room_flooritems SET x = ?, y = ?, rotation = ?, height = ? WHERE id = ?");
			{
				Statement.setInt(1, Item.X);
				Statement.setInt(2, Item.Y);
				Statement.setInt(3, Item.Rotation);
				Statement.setFloat(5, Height);
				Statement.setInt(4, Item.Id);
				Statement.execute();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}