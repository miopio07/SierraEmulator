package sierra.habbohotel.catalogue.items;

import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;

public class CatalogueItem {

	public int Id;
	public int PageId;
	public int ItemId;
	public String Label;
	public int Credits;
	public int Pixels;
	public int Snow;
	public int Amount;
	public Boolean VIP;
	
	public CatalogueItem(int Id, int PageId, int ItemId, String Label, int Credits, int Pixels, int Snow, int Amount, int VIP)
	{
		this.Id = Id;
		this.PageId = PageId;
		this.ItemId = ItemId;
		this.Label = Label;
		this.Credits = Credits;
		this.Pixels = Pixels;
		this.Snow = Snow;
		this.Amount = Amount;
		this.VIP = VIP == 1 ? true : false;
	}
	public Furniture GetBaseItem()
	{
		return FurnitureEngine.GetById(ItemId);
	}
}
