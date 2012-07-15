package sierra.habbohotel;

import java.sql.PreparedStatement;

import sierra.Sierra;
import sierra.habbo.Session;

public class UpdateOnline
{
	public static void SetCount()
	{
		try
		{
			PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("UPDATE system SET online_count = ?");
			{
				Statement.setInt(1, AvaliableCount());
				Statement.execute();
			}
		}
		catch (Exception e)
		{
			//Logging.writeLine("Unhandled exception when updating the count of active clients: " + e.getMessage());
		}
	}
	public static int AvaliableCount()
	{
		int i = 0;
		
		for (Session User : Sierra.getServer().getActiveConnections().getSessions().values())
		{
			if (User != null)
			{
				if (User._Authenticated)
				{
					i++;
				}
			}
		}
		return i;
	}
}
