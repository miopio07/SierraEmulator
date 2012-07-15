package sierra.habbo;

import java.sql.ResultSet;

//import sierra.Logging;
import sierra.Sierra;
import sierra.habbo.users.Habbo;

public class SetSessionInfo {

	private Habbo Info;
	public static Habbo GenerateHabboData(int UserId)
	{
		try
		{
			Habbo mHabbo = new Habbo();

			new SetSessionInfo().setDetails(null, mHabbo, Sierra.getDatabase().ReadRow("SELECT * FROM members WHERE id = '" + UserId + "'"));

			return mHabbo;
		}
		catch (Exception e)
		{
			//Logging.writeLine("Unhandled exception when loading profile");
			return null;
		}
	}
	public static Habbo GenerateHabboData(String Username)
	{
		try
		{
			Habbo mHabbo = new Habbo();

			new SetSessionInfo().setDetails(null, mHabbo, Sierra.getDatabase().ReadRow("SELECT * FROM members WHERE username = '" + Username + "'"));

			return mHabbo;
		}
		catch (Exception e)
		{
			//Logging.writeLine("Unhandled exception when loading profile");
			return null;
		}
	}
	public void setDetails(Session client, Habbo Info, ResultSet Row) {

		try {

			this.Info = Info;

			if (Row != null) {

				getInfo().Id = Row.getInt("id");
				getInfo().Rank = Row.getInt("rank");
				getInfo().Username = Row.getString("username");
				getInfo().Figure = Row.getString("figure");
				getInfo().Motto = Row.getString("mission");
				getInfo().Gender = Row.getString("gender");
				getInfo().Credits = Row.getInt("credits");
				getInfo().Pixels = Row.getInt("pixels");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Habbo getInfo() {
		return Info;
	}
}
