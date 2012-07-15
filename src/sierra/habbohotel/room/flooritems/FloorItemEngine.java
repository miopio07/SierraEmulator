package sierra.habbohotel.room.flooritems;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;

public class FloorItemEngine 
{
	public static ConcurrentLinkedQueue<FloorItem> getFloorItems(int RoomId) throws Exception 
	{
		ConcurrentLinkedQueue<FloorItem> FloorItems = new ConcurrentLinkedQueue<FloorItem>();

		PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM `room_flooritems` WHERE roomid = ?");
		{
			Statement.setInt(1, RoomId);
		}

		ResultSet Row = Statement.executeQuery();

		while (Row.next())
		{
			FloorItems.add(new FloorItem(Row.getInt("id"), Row.getInt("baseid"),  Row.getInt("x"),  Row.getInt("y"), Row.getInt("rotation"), Row.getFloat("height"), Row.getString("extra")));
		}
		return FloorItems;
	}
}
