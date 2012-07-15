package sierra.habbohotel.catalogue.items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sierra.Sierra;

public class CatalogueItemEngine
{
	private static Map<Integer, CatalogueItem> CatalogueItems = new HashMap<Integer, CatalogueItem>();

	public static Map<Integer, CatalogueItem> GetCatalogueItems()
	{
		return CatalogueItems;
	}
	public static List<CatalogueItem> GetPageList(Integer PageId)
	{
		List<CatalogueItem> PageItems = new ArrayList<CatalogueItem>();
		
		for (CatalogueItem Item : CatalogueItems.values())
		{
			if (Item.PageId == PageId)
			{
				PageItems.add(Item);
			}
		}
		return PageItems;
	}
	public static void LoadAll() throws SQLException 
	{
		ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM catalog_items;");

		while (Row.next())
		{
			CatalogueItems.put(Row.getInt("id"), new CatalogueItem(Row.getInt("id"), Row.getInt("page_id"), Row.getInt("item_ids"), Row.getString("catalog_name"), Row.getInt("cost_credits"), Row.getInt("cost_pixels"), Row.getInt("cost_snow"), Row.getInt("amount"), Row.getInt("vip")));
		}
	}
}
 