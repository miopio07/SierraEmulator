package sierra.habbohotel.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;

public class ModelEngine
{
	private static Map<String, Model> Models = new HashMap<String, Model>();

	public static Map<String, Model> GetModelMap()
	{
		return Models;
	}
	public static Model GetInstanceByName(String Name)
	{
		return GetModelMap().get(Name);
	}
	public static void LoadAll() throws SQLException 
	{
		ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM room_models;");

		while (Row.next())
		{
			Models.put(Row.getString("id"), new Model(Row.getString("id"), Row.getString("heightmap"), Row.getInt("door_x"), Row.getInt("door_y"), Row.getInt("door_z"), Row.getInt("door_dir")));
		}
	}
}
