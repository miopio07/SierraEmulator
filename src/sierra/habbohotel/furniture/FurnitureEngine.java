package sierra.habbohotel.furniture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;

public class FurnitureEngine
{
	private static Map<Integer, Furniture> Furnitures = new HashMap<Integer, Furniture>();

	public static Map<Integer, Furniture> GetFurnitureMap()
	{
		return Furnitures;
	}
	public static Furniture GetById(Integer ItemId)
	{
		return Furnitures.get(ItemId);
	}
	public static void LoadAll() throws SQLException 
	{
		ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM furniture;");

		while (Row.next())
		{
			Furnitures.put(Row.getInt("id"), new Furniture(Row.getInt("id"), Row.getString("public_name"), Row.getString("item_name"), Row.getString("type"),	Row.getInt("width"), Row.getInt("length"), Row.getFloat("stack_height"), Row.getInt("can_stack"),	Row.getInt("can_sit"), Row.getInt("is_walkable"), Row.getInt("sprite_id"), Row.getInt("allow_recycle"),	Row.getInt("allow_trade"), Row.getInt("allow_marketplace_sell"), Row.getInt("allow_gift"), Row.getInt("allow_inventory_stack"), Row.getString("interaction_type"),	Row.getInt("interaction_modes_count"), Row.getInt("vending_ids"), Row.getInt("is_arrow")));
		}
	}
}
