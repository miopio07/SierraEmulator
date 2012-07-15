package sierra.habbo;

import java.sql.ResultSet;
import java.util.concurrent.ConcurrentLinkedQueue;

//import sierra.Logging;
import sierra.Sierra;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.habbohotel.inventory.Inventory;

public class SessionInventory
{
	private ConcurrentLinkedQueue<Inventory> WallItems;
	private ConcurrentLinkedQueue<Inventory> FloorItems;

	public SessionInventory(Session Session)
	{
		FloorItems = new ConcurrentLinkedQueue<Inventory>();
		WallItems = new ConcurrentLinkedQueue<Inventory>();
		try
		{
			ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM members_inventory WHERE owner = '" + Session.getHabbo().Id + "'");

			while (Row.next())
			{
				Furniture Furni = FurnitureEngine.GetById(Row.getInt("itemid"));

				if (Furni.Type.equals("s"))
				{
					FloorItems.add(new Inventory(Row.getInt("id"), Furni, Row.getString("label")));
				}
				else if (Furni.Type.equals("i"))
				{
					WallItems.add(new Inventory(Row.getInt("id"), Furni, Row.getString("label")));
				}
			}
		}
		catch (Exception e)
		{
			//Logging.writeLine("Unhandled exception when loading Inventory: " + e.getMessage());
		}
	}
	public void AddSingle(int Id, String Label, int ItemId)
	{
		Furniture Furni = FurnitureEngine.GetById(ItemId);

		if (Furni.Type.equals("s"))
		{
			FloorItems.add(new Inventory(Id, Furni, Label));
		}
		else if (Furni.Type.equals("i"))
		{
			WallItems.add(new Inventory(Id, Furni, Label));
		}
	}
	public Inventory RemoveWallItem(int Id)
	{
		for (Inventory Item : WallItems)
		{
			if (Item.Id == Id)
				WallItems.remove(Item);
		}
		return null;
	}
	public Inventory RemoveFloorItem(int Id)
	{
		for (Inventory Item : FloorItems)
		{
			if (Item.Id == Id)
				FloorItems.remove(Item);
		}
		return null;
	}
	public Inventory GetWallInventoryItem(int Id)
	{
		for (Inventory Item : WallItems)
		{
			if (Item.Id == Id)
				return Item;
		}
		return null;
	}
	public Inventory GetFloorInventoryItem(int Id)
	{
		for (Inventory Item : FloorItems)
		{
			if (Item.Id == Id)
				return Item;
		}
		return null;
	}
	public ConcurrentLinkedQueue<Inventory> GetWallItems()
	{
		return WallItems;
	}
	public ConcurrentLinkedQueue<Inventory> GetFloorItems()
	{
		return FloorItems;
	}
}
