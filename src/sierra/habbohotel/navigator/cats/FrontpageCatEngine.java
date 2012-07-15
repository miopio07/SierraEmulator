package sierra.habbohotel.navigator.cats;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;

public class FrontpageCatEngine
{
	private static List<FrontpageCat> Cats = new ArrayList<FrontpageCat>();
	
	public static void LoadAll()
	{
		ResultSet Result = Sierra.getDatabase().ReadTable("SELECT * FROM navigator_categories;");
		
		try
		{
			while (Result.next())
			{
				Cats.add(new FrontpageCat(Result.getInt("id"), Result.getInt("min_rank"), Result.getString("label"), Result.getString("picture")));
			}
		}
		catch (Exception e)
		{
			
		}
	}
	public static List<FrontpageCat> getCategories(int Rank)
	{
		List<FrontpageCat> sCats = new ArrayList<FrontpageCat>();
		
		for (FrontpageCat Cat : Cats)
		{
			if (Cat.MinRank == Rank || Rank > Cat.MinRank)
			{
				sCats.add(Cat);
			}
		}
		
		return sCats;
	}
}
