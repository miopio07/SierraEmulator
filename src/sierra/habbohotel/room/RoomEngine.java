package sierra.habbohotel.room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;

public class RoomEngine
{
	private static ConcurrentLinkedQueue<Room> Rooms = new ConcurrentLinkedQueue<Room>();

	public static ConcurrentLinkedQueue<Room> GetAllRooms()
	{
		return Rooms;
	}
	public static List<Room> GetByOwnerId(int Id)
	{
		List<Room> Rooms = new ArrayList<Room>();

		for (Room Room : GetAllRooms())
		{
			if (Room.OwnerId == Id)
			{
				Rooms.add(Room);
			}
		}
		return Rooms;
	}
	public static Room GetById(int Id)
	{
		for (Room Room : GetAllRooms())
		{
			if (Room.Id == Id)
			{
				return Room;
			}
		}
		return null;
	}
	public static void RemoveByOwnerId(int Id)
	{
		for (Room Room : Rooms)
		{
			if (Room.OwnerId == Id && Room.SessionList.size() == 0)
			{
				Rooms.remove(Room);
			}
		}
	}
	public static Boolean RoomExists(int Id)
	{
		if (GetById(Id) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public static void RemoveById(int Id)
	{
		for (Room Room : Rooms)
		{
			if (Room.Id == Id && Room.SessionList.size() == 0 && OwnerOffline(Room))
			{
				Rooms.remove(Room);
			}
		}
	}
	public static Boolean OwnerOffline(Room Room)
	{
		if (Sierra.getServer().getActiveConnections().UserByIdOnline(Room.OwnerId) == false)
			return true;
		else
			return false;
	}
	public static void GetOwnRooms(int OwnerId, String Owner) throws Exception
	{
		PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM `rooms` WHERE ownerid = ?");
		{
			Statement.setInt(1, OwnerId);
		}

		ResultSet Row = Statement.executeQuery();

		while (Row.next())
		{
			if (!RoomExists(Row.getInt("id")))
			{
				Rooms.add(new Room(Row.getInt("id"), OwnerId, Owner, Row.getString("name"), Row.getString("description"), Row.getString("model"), Row.getString("wallpaper"), Row.getString("floor"), Row.getString("outside")));
			}
		}
	}
	public static void AddRoom(int Id, int OwnerId, String Owner) throws SQLException 
	{
		if (!RoomExists(Id))
		{
			ResultSet Row = Sierra.getDatabase().ReadRow("SELECT * FROM rooms WHERE id = '" + Id + "'");
			{
				Rooms.add(new Room(Row.getInt("id"), OwnerId, Owner, Row.getString("name"), Row.getString("description"), Row.getString("model"), Row.getString("wallpaper"), Row.getString("floor"), Row.getString("outside")));
			}
		}
	}
}
