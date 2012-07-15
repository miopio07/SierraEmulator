package sierra.habbohotel.messenger.categories;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;

public class MessengerCatEngine
{
	private static List<MessengerCat> Categories = new ArrayList<MessengerCat>();
	
	public static void LoadAll()
	{
		try
		{
			ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM messenger_categories;");
			
			while (Row.next())
			{
				Categories.add(new MessengerCat(Row.getInt("id"), Row.getString("label"), Row.getInt("min_rank")));
			}
		}
		catch (Exception e)
		{
		}
	}
	public static List<MessengerCat> GenerateCategories(int MinRank)
	{
		List<MessengerCat> Own = new ArrayList<MessengerCat>();
		
		for (MessengerCat OwnCat : Categories)
		{
			if (OwnCat.MinRank == MinRank || OwnCat.MinRank < MinRank)
			{
				Own.add(OwnCat);
			}
		}
		return Own;
	}
	public static int getIdByCat(String Label)
	{
		for (MessengerCat OwnCat : Categories)
		{
			if (OwnCat.Label.toLowerCase().contains(Label.toLowerCase()))
				return OwnCat.Id;
		}
		return 0;
	}
}
