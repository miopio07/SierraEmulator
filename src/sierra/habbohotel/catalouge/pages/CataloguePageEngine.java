package sierra.habbohotel.catalouge.pages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import sierra.Sierra;

public class CataloguePageEngine {

	private static Map<Integer, CataloguePage> CataloguePages = new HashMap<Integer, CataloguePage>();

	public static Map<Integer, CataloguePage> GetCataloguePages()
	{
		return CataloguePages;
	}
	public static CataloguePage GetInstanceByName(Integer Id)
	{
		return GetCataloguePages().get(Id);
	}
	public static void LoadAll() throws SQLException 
	{
		ResultSet Row = Sierra.getDatabase().ReadTable("SELECT * FROM catalog_pages;");

		while (Row.next())
		{
			CataloguePages.put(Row.getInt("id"), new CataloguePage(Row.getString("page_layout"), Row.getString("page_headline"), Row.getString("page_teaser"), Row.getString("page_special"), Row.getString("page_text1"), Row.getString("page_text2"), Row.getString("page_text_details"), Row.getString("page_text_teaser")));
		}
	}

}
