package sierra.habbohotel.catalogue.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import sierra.Sierra;

public class CatalogueIndexEngine
{
	private static List<CatalogueIndex> CatalogueIndexs = new ArrayList<CatalogueIndex>();
	
	public static List<CatalogueIndex> GetMainCatalogueIndexList(int Rank)
	{
		 List<CatalogueIndex> Indexs = new ArrayList<CatalogueIndex>();
		 
		 for (CatalogueIndex Index : CatalogueIndexs)
		 {
			 if (Index.ParentId == -1 && (Index.Rank == Rank || Rank > Index.Rank))
			 {
				 Indexs.add(Index);
			 }
		 }
		 
		return Indexs;
	}
	public static List<CatalogueIndex> GetSubCatalogueIndexList(int Id, int Rank)
	{
		 List<CatalogueIndex> Indexs = new ArrayList<CatalogueIndex>();
		 
		 for (CatalogueIndex Index : CatalogueIndexs)
		 {
			 if (Index.ParentId == Id && Index.Rank <= Rank)
			 {
				 Indexs.add(Index);
			 }
		 }
		 
		return Indexs;
	}
	public static void LoadAll() throws SQLException 
	{
		ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM catalog_pages ORDER BY order_num ASC;");

		while (Row.next())
		{
			CatalogueIndexs.add(new CatalogueIndex(Row.getInt("id"), Row.getInt("parent_id"), Row.getString("caption"), Row.getInt("icon_color"), Row.getInt("icon_image"), Row.getInt("min_rank"), Row.getInt("club_only")));
		}
	}
}
