package sierra.habbohotel.furniture;

public class Furniture {

	public int Id;
	public String PublicName;
	public String ItemName;
	public String Type;
	public int Width;
	public int Length;
	public Float StackHeight;
	public Boolean CanStack;
	public Boolean CanSit;
	public Boolean IsWalkable;
	public int SpriteId;
	public Boolean AllowRecycle;
	public Boolean AllowTrade;
	public Boolean AllowMarketplaceSell;
    public Boolean AllowGift;
    public Boolean AllowInventoryStack;
    public String InteractionType;
    public int InteractionModesCount;
    public int VendingIds;
    public Boolean IsArrow;
    
	public Furniture(int Id, String PublicName, String ItemName, String Type, int Width, int Length, Float StackHeight, int CanStack, int CanSit,  int IsWalkable, int SpriteId, int AllowRecycle, int AllowTrade, int AllowMarketplaceSell, int AllowGift, int AllowInventoryStack, String InteractionType, int InteractionModesCount, int VendingIds, int IsArrow)
	{
		this.Id = Id;
		this.PublicName = PublicName;
		this.ItemName = ItemName;
		this.Type = Type;
		this.Width = Width;
		this.Length = Length;
		this.StackHeight = StackHeight;
		this.CanStack = CanStack == 1 ? true : false;
		this.CanSit = CanSit == 1 ? true : false;
		this.IsWalkable = IsWalkable == 1 ? true : false;
		this.SpriteId = SpriteId;
		this.AllowRecycle = AllowRecycle == 1 ? true : false;
		this.AllowTrade = AllowTrade == 1 ? true : false;
		this.AllowMarketplaceSell = AllowMarketplaceSell == 1 ? true : false;
		this.AllowGift = AllowGift == 1 ? true : false;
		this.AllowInventoryStack = AllowInventoryStack == 1 ? true : false;
		this.InteractionType = InteractionType;
		this.InteractionModesCount = InteractionModesCount;
		this.VendingIds = VendingIds;
		this.IsArrow = IsArrow == 1 ? true : false;
	}

}
