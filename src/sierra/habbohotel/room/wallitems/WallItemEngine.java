package sierra.habbohotel.room.wallitems;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;
import sierra.habbohotel.room.flooritems.FloorItem;

public class WallItemEngine
{
	public static ConcurrentLinkedQueue<WallItem> GetRoomItems(int RoomId) throws Exception 
	{
		ConcurrentLinkedQueue<WallItem> WallItems = new ConcurrentLinkedQueue<WallItem>();

		PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM `room_wallitems` WHERE roomid = ?");
		{
			Statement.setInt(1, RoomId);
		}

		ResultSet Row = Statement.executeQuery();

		while (Row.next())
		{
			WallItems.add(new WallItem(Row.getInt("id"), Row.getInt("baseid"), Row.getString("position"), Row.getString("extradata")));
		}
		return WallItems;
	}
}
