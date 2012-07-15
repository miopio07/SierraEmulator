package sierra.habbohotel.messenger.buddies;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sierra.Sierra;

public class BuddyEngine
{
	public static List<Buddy> GenerateBuddyList(int UserId)
	{
		List<Buddy> Buddies = new ArrayList<Buddy>();
		
		try
		{
			ResultSet Row = Sierra.getDatabase().ExecuteQuery("SELECT * FROM messenger_buddies WHERE user_id = '" + UserId + "'").executeQuery();
			
			while (Row.next())
			{
				Buddies.add(new Buddy(Row.getInt("friend_id"), Row.getInt("cat_id")));
			}
		}
		catch (Exception e)
		{
		}
		
		return Buddies;
	}
}
