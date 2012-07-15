package sierra.habbohotel.catalogue.index;

public class CatalogueIndex
{	
	public int Id;
	public int ParentId;
	public String Caption;
	public int IconColor;
	public int IconImage;
	public int Rank;
	public int Club;

	public CatalogueIndex(int Id, int ParentId, String Caption, int Color, int Image, int Rank, int ClubOnly)
	{
		this.Id = Id;
		this.ParentId = ParentId;
		this.Caption = Caption;
		this.IconColor = Color;
		this.IconImage = Image;
		this.Rank = Rank;
		this.Club = ClubOnly;
		
	}
}
