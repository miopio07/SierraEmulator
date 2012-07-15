package sierra.habbo.users;

import java.sql.SQLException;

import sierra.Sierra;
import sierra.habbohotel.fuserights.FuserightEngine;

public class Habbo
{	
	public int Id;
	public int Rank;
	public String Username;
	public String Gender;
	public String Figure;
	public String Motto;
	public int Credits;
	public int Pixels;
	
	public Habbo()
	{
		Id = 0;
		Rank = 0;
		Username = "";
		Gender = "";
		Figure = "";
		Motto = "";
		Credits = 0;
		Pixels = 0;
	}
	
	public Boolean IsAdmin(){
		return Rank == 7;
	}
	
	public Boolean IsModerator() {
		return Rank == 6;
	}
	
	public Boolean IsSuperHobba() {
		return Rank == 5;
	}
	
	public Boolean IsGoldHobba() {
		return Rank == 4;
	}
	
	public Boolean IsSilverHobba() {
		return Rank == 3;
	}
	
	public Boolean IsVIP() {
		return Rank == 2;
	}
	
	public Boolean IsNormal() {
		return Rank == 1;
	}
	
	public Boolean IsBanned() {
		return Rank == 0;
	}
	
	public Boolean hasStaffPermission(String Fuse) {
		
		return FuserightEngine.getFusesrightsByRank(Rank).contains(Fuse);
	}
	
	public void SaveData() {
		
		try {
			Sierra.getDatabase().ExecuteQuery("UPDATE members SET rank = '" + Rank + "', gender = '" + Gender + "', figure = '" + Figure + "', mission = '" + Motto + "', credits = '" + Credits + "' WHERE username = '" + Username + ";").execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
