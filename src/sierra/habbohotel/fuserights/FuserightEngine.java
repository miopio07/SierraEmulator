package sierra.habbohotel.fuserights;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sierra.Sierra;

public class FuserightEngine
{
	private static Map<Integer, List<String>> Fuserights = new HashMap<Integer, List<String>>();
	
	public static void LoadAll()
	{
		try
		{
			PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM `fuserights`");
			
			ResultSet Row = Statement.executeQuery();

			while (Row.next())
			{
				Integer Rank = Row.getInt("rank");
				
				if (Fuserights.containsKey(Rank) == false)
				{
					Fuserights.put(Rank, new ArrayList<String>());
					fillFuses(Rank);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void fillFuses(int Rank)
	{
		try
		{
			ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM fuserights WHERE rank = '" + Rank + "'");

			while (Row.next())
			{
				Fuserights.get(Rank).add(Row.getString("fuse"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<String> getFusesrightsByRank(int Rank)
	{
		List<String> LoadedFuses = new ArrayList<String>();
		
		for (Entry<Integer, List<String>> KeyValue : Fuserights.entrySet())
		{
			if (KeyValue.getKey().equals(Rank) || KeyValue.getKey() < Rank)
			{
				for (String fuse : KeyValue.getValue())
				{
					LoadedFuses.add(fuse);
				}
			}
		}
		return LoadedFuses;
	}
}
