package sierra.habbohotel.room.flooritems;

import sierra.Sierra;
import sierra.habbohotel.furniture.Furniture;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.message.builder.ISerialize;
import sierra.message.builder.ServerMessage;

public class FloorItem implements ISerialize
{

	public int Id;
	public int BaseId;
	public int X;
	public int Y;
	public int Rotation;
	public Float fHeight;
	public String sHeight;
	public String ExtraData;
	
	public FloorItem(int Id, int BaseId, int X, int Y, int Rotation, Float Height, String ExtraData)
	{
		this.Id = Id;
		this.BaseId = BaseId;
		this.X = X;
		this.Y = Y;
		this.Rotation = Rotation;
		this.fHeight = Height;
		this.sHeight = "" + Height;
		this.ExtraData = ExtraData;
	}
	
	public Furniture GetBaseItem() {
		return FurnitureEngine.GetById(BaseId);
	}
	
	public void SerializePacket(ServerMessage Message) {
		Message.AppendInt32(Id);
        Message.AppendInt32(GetBaseItem().SpriteId);
        Message.AppendInt32(X);
        Message.AppendInt32(Y);
        Message.AppendInt32(Rotation);
        Message.AppendString(Float.toString(fHeight));
        Message.AppendInt32(0);
        Message.AppendInt32(0);
        Message.AppendString(ExtraData);
        Message.AppendInt32(-1);
        Message.AppendInt32(GetBaseItem().InteractionType.equals("default") ? 1 : 0);
	}
	
	/**
	 * @source: https://github.com/Shynoshy/SierraEmulator/
	 */
	public boolean ChangeState()
	{
		if ((this.GetBaseItem().InteractionType.equals("default") || this.GetBaseItem().InteractionType.equals("gate")) && (this.GetBaseItem().InteractionModesCount > 1))
		{
			if (ExtraData.isEmpty())
                ExtraData = "0";
			Integer Temp = Integer.parseInt(ExtraData) + 1;
			if (Temp >= this.GetBaseItem().InteractionModesCount)
				ExtraData = "0";
			else
				ExtraData = "1";
			return true;
		}
		else
			return false;
	}

	public Float GetNextHeight()
	{
		return this.fHeight + this.GetBaseItem().StackHeight;
	}

	/**
	 * @source: https://github.com/Shynoshy/SierraEmulator/
	 */
	public void SaveState()
	{
		try 
		{
			Sierra.getDatabase().ExecuteQuery("UPDATE room_flooritems SET extra = '" + ExtraData + "' WHERE id = '" + Id + "';").execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
