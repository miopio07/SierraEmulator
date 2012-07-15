package sierra.habbohotel.inventory;

import sierra.habbohotel.furniture.Furniture;

public class Inventory
{
	public int Id;
	public String Label;
	private Furniture BaseItem;
	
	public Inventory(int Id, Furniture BaseItem, String Label)
	{
		this.Id = Id;
		this.Label = Label;
		this.BaseItem = BaseItem;
	}
	public Furniture GetBaseItem()
	{
		return BaseItem;
	}
}
