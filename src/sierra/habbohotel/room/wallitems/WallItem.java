package sierra.habbohotel.room.wallitems;

import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.message.builder.ISerialize;
import sierra.message.builder.ServerMessage;

public class WallItem implements ISerialize
{

	public int Id;
	public int BaseId;
	public String Position;
	public String ExtraData;
	
	public WallItem(int Id, int BaseId, String Position, String ExtraData)
	{
		this.Id = Id;
		this.BaseId = BaseId;
		this.Position = Position;
		this.ExtraData = ExtraData;
	}
	public Furniture GetBaseItem()
	{
		return FurnitureEngine.GetById(BaseId);
	}
	public void SerializePacket(ServerMessage Message)
	{
        Message.AppendString(Id);
        Message.AppendInt32(GetBaseItem().SpriteId);
        Message.AppendString(Position);
        Message.AppendString(ExtraData);
        Message.AppendInt32(GetBaseItem().InteractionType.equals("default") ? 1 : 0);
	}
}
