package sierra.habbohotel.room;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.pathfinder.AffectedTile;
import sierra.habbohotel.room.flooritems.*;
import sierra.habbohotel.room.wallitems.*;
import sierra.message.builder.ServerMessage;

public class Room {

	public int Id;
	public int OwnerId;
	public String OwnerName;
	public String Name;
	public String Description;
	public String Model;
	public int State;
	public int Score;
	public int Category;
	public String Wall;
	public String Floor;
	public Boolean Pets;
	public Boolean PetsCanEat;
	public String Landscape;
	public ConcurrentLinkedQueue<Session> SessionList;
	public ConcurrentLinkedQueue<Integer> RightsList;
	public ConcurrentLinkedQueue<WallItem> WallItems;
	public ConcurrentLinkedQueue<FloorItem> FloorItems;

	public Room(int Id, int OwnerId, String OwnerName, String Name, String Description, String Model, String Wall, String Floor, String Landscape)
	{
		// Set variables
		this.Id = Id;
		this.OwnerId = OwnerId;
		this.OwnerName = OwnerName;
		this.Name = Name;
		this.Description = Description;
		this.Model = Model;
		this.SessionList = new ConcurrentLinkedQueue<Session>();
		this.RightsList = new ConcurrentLinkedQueue<Integer>();
		this.Wall = Wall;
		this.Floor = Floor;
		this.Landscape = Landscape;
		this.Score = 0;
		this.Category = 0;
		this.LoadUserRights();

		try
		{
			this.WallItems = WallItemEngine.GetRoomItems(Id);
			this.FloorItems = FloorItemEngine.getFloorItems(Id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//new Thread(new Walker(this)).start();
		
	}
	/*
	 * Load room users with rights.
	 */
	public void LoadUserRights() 
	{
		ResultSet RightsRow = Sierra.getDatabase().ReadTable("SELECT * FROM room_rights WHERE roomid = '" + this.Id + "';");

		try
		{
			while (RightsRow.next())
			{
				this.RightsList.add(RightsRow.getInt("userid"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/*
	 * Send to all users who are in that room
	 */
	public void Send(ServerMessage Response)
	{
		for (Session Client : SessionList)
		{
			Client.Send(Response);
		}
	}
	/*
	 * If you have rights
	 */
	public Boolean getUserHasRights(int _Id)
	{ 
		if (RightsList.contains(_Id) || OwnerId == _Id)
			return true;
		else if (Sierra.getServer().getActiveConnections().GetUserById(_Id).getHabbo().hasStaffPermission("fuse_mod"))
			return true;
		else
			return false;
	}
	/*
	 * Leave room method
	 */
	public void LeaveRoom(Session mSession)
	{        
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.LeaveRoom);
		Message.AppendString(mSession.getHabbo().Id);
		Send(Message);

		SessionList.remove(mSession);
		mSession.getRoomUser().InRoom = false;
		mSession.getRoomUser().Room = null;

		if (!OwnerName.equals(mSession.getHabbo().Username))
			RoomEngine.RemoveById(Id);
	}

	/*
	 * Check if a user is already in those coordinates.
	 */
	public Boolean CheckUserCoordinates(Session Id, int X, int Y)
	{
		for (Session User : SessionList)
		{
			if (!Id.equals(User))
			{
				if (User.getRoomUser().X == X && User.getRoomUser().Y == Y)
					return true;

				if (User.getRoomUser().GoalX == X && User.getRoomUser().GoalY == Y)
					return true;
			}
		}
		return false;
	}
	
	public Session GetUserByCoordinates(int X, int Y)
	{
		for (Session User : SessionList)
		{
			if (User.getRoomUser().X == X && User.getRoomUser().Y == Y)
				return User;

			if (User.getRoomUser().GoalX == X && User.getRoomUser().GoalY == Y)
				return User;
		}
		return null;
	}

	/*
	 * New wall item add
	 */

	public void AddWallItem(int _Id, int BaseId, String Position, String ExtraData)
	{
		WallItems.add(new WallItem(_Id, BaseId, Position, ExtraData));
	}
	/*
	 * New floor item add
	 */

	public void AddFloorItem(int _Id, int BaseId, int X, int Y, int Rot, Float Height, String ExtraData)
	{
		FloorItems.add(new FloorItem(_Id, BaseId, X, Y, Rot, Height, ExtraData));
	}

	/*
	 * Grab floor item
	 */

	public FloorItem getFloorItem(int Id)
	{
		for (FloorItem Item : FloorItems)
		{
			if (Item.Id == Id)
				return Item;
		}
		return null;
	}

    
    public List<FloorItem> getItemListByCoords(int X, int Y)
    {
        List<FloorItem> Result = new ArrayList<FloorItem>();
        
        for (FloorItem Item : FloorItems)
        {
            if(Item.X == X && Item.Y == Y)
            {
                Result.add(Item);
            }
            else
            {
                List<AffectedTile> AffectedTiles = AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation);
                
                for (AffectedTile Tile : AffectedTiles)
                {
                    if(X == Tile.X && Y == Tile.Y)
                    {
                    	if(!Result.contains(Item))
                    		Result.add(Item);
                    }
                }
            }
        }
        return Result;
    }
	
	/*
	 * User serialise
	 */

	public ServerMessage SerializeRoomUsers(Session Session)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.RoomUsers);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().SessionList.size());
		for (Session RoomPerson : Session.getRoomUser().getCurrentRoom().SessionList)
		{
			Message.AppendInt32(RoomPerson.getHabbo().Id);
			Message.AppendString(RoomPerson.getHabbo().Username);
			Message.AppendString(RoomPerson.getHabbo().Motto);
			Message.AppendString(RoomPerson.getHabbo().Figure);
			Message.AppendInt32(RoomPerson.getHabbo().Id);
			Message.AppendInt32(RoomPerson.getRoomUser().X);
			Message.AppendInt32(RoomPerson.getRoomUser().Y);
			Message.AppendString(Double.toString(RoomPerson.getRoomUser().Height));
			Message.AppendInt32(RoomPerson.getRoomUser().BodyRotation);
			Message.AppendInt32(1);
			Message.AppendString("m");
			Message.AppendInt32(-1);
			Message.AppendInt32(-1);
			Message.AppendInt32(0);
			Message.AppendInt32(1337);
		}
		return Message;
	}

	/*
	 * Status serialize
	 */
	public ServerMessage SerializeRoomStatus(Session Session)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.RoomStatuses);
		Message.AppendInt32(Session.getRoomUser().getCurrentRoom().SessionList.size());
		for (Session sSession : Session.getRoomUser().getCurrentRoom().SessionList)
		{
			Message.AppendInt32(sSession.getHabbo().Id);
			Message.AppendInt32(sSession.getRoomUser().X);
			Message.AppendInt32(sSession.getRoomUser().Y);
			Message.AppendString(Double.toString(sSession.getRoomUser().Height));
			Message.AppendInt32(sSession.getRoomUser().BodyRotation);
			Message.AppendInt32(sSession.getRoomUser().BodyRotation);
			
			String Status = "/flatctrl 4/";
			
			if (sSession.getRoomUser().IsSit)
				Status += "sit 1.0/";
			
			Message.AppendString(Status);
		}
		return Message;
	}
	
	public FloorItem grabFurniByCoords(int X, int Y)
	{
		for (FloorItem Item : FloorItems)
		{
			for (AffectedTile Tile : AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation))
			{
				if (Item.X == X && Item.Y == Y || Tile.X == X && Tile.Y == Y)
				{
					return Item;
				}
			}
		}
		return null;
	}
}
