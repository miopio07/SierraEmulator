package sierra.habbohotel.catalouge.pages;

import java.util.List;

import sierra.habbohotel.catalogue.items.CatalogueItem;
import sierra.habbohotel.catalogue.items.CatalogueItemEngine;
import sierra.message.builder.ServerMessage;

public class CataloguePage {

	public String Layout;
	public String Headline;
	public String Teaser;
	public String Special;
	public String Text1;
	public String Text2;
	public String Details;
	public String Teaser2;

	public CataloguePage(String Layout, String Headline, String Teaser, String Special, String Text1, String Text2, String Details, String Teaser2)
	{
		this.Layout = Layout;
		this.Headline = Headline;
		this.Teaser = Teaser;
		this.Special = Special;
		this.Text1 = Text1;
		this.Text2 = Text2;
		this.Details = Details;
		this.Teaser2 = Teaser2;
	}
	public void SerializeItems(int Id, ServerMessage Message)
	{
		List<CatalogueItem> PageItems = CatalogueItemEngine.GetPageList(Id);

		Message.AppendInt32(PageItems.size()); // Count Items

		for (CatalogueItem Item : PageItems)
		{
			Message.AppendInt32(Item.Id);
			Message.AppendString(Item.GetBaseItem().PublicName);
			Message.AppendInt32(Item.Credits);
			Message.AppendInt32(Item.Pixels);
			Message.AppendInt32(0);
			Message.AppendBoolean(Item.GetBaseItem().AllowGift);
			Message.AppendInt32(1);
			Message.AppendString(Item.GetBaseItem().Type);
			Message.AppendInt32(Item.GetBaseItem().SpriteId);
			if (Item.Label.contains("wallpaper"))
			{
				Message.AppendString(Item.Label.split("_")[2]);
			}
			else if(Item.Label.contains("floor"))
			{
				Message.AppendString(Item.Label.split("_")[2]);
			}
			else if(Item.Label.contains("landscape"))
			{
				Message.AppendString(Item.Label.split("_")[2]);
			}
			else
			{
				Message.AppendString("");
			}
			Message.AppendInt32(Item.Amount);
			Message.AppendInt32(-1);
			Message.AppendInt32(Item.VIP ? 1 : 0);
		}
	}
}