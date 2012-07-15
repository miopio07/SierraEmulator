package sierra.habbohotel.navigator.cats;

import sierra.message.builder.ISerialize;
import sierra.message.builder.ServerMessage;

public class FrontpageCat implements ISerialize
{
	public int Id;
	public int MinRank;
	public String Label;
	public String Picture;
	
	public FrontpageCat(int Id, int MinRank, String Label, String Picture)
	{
		this.Id = Id;
		this.MinRank = MinRank;
		this.Label = Label;
		this.Picture = Picture;
	}
	public void SerializePacket(ServerMessage Message)
	{
        Message.AppendInt32(Id);
        Message.AppendString(Label);
        Message.AppendString("");
        Message.AppendInt32(1); 
        Message.AppendString("");
        Message.AppendString(Picture);
        Message.AppendInt32(0);
        Message.AppendInt32(0);
        Message.AppendInt32(4);
        Message.AppendBoolean(false);
	}

}
