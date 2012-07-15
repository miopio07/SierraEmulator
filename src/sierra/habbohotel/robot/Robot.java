package sierra.habbohotel.robot;

import sierra.composers.Outgoing;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ServerMessage;

public class Robot
{
	public byte getRoomId() {
		return roomId;
	}

	public byte getUnitId() {
		return unitId;
	}

	public Room getBotRoom() {
		return botRoom;
	}
	
	public byte getCurrentX() {
		return currentX;
	}

	public void setCurrentX(byte currentX) {
		this.currentX = currentX;
	}

	public byte getCurrentY() {
		return currentY;
	}

	public void setCurrentY(byte currentY) {
		this.currentY = currentY;
	}
	
	private byte roomId;
	private byte currentX;
	private byte currentY;
	private byte unitId;
	private byte botRotation;
	private Room botRoom;
	
	public Robot(byte roomId, byte startX, byte startY, byte botRotation, byte unitId)
	{
		this.roomId = roomId;
		this.currentX = startX;
		this.currentY = startY;
		this.unitId = unitId;
		this.botRoom = RoomEngine.GetById(this.roomId);
	}

	public void SendStatus(String Status)
	{
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.RoomStatuses);
		Message.AppendInt32(1);
		Message.AppendInt32((int) unitId);
		Message.AppendInt32((int) currentX);
		Message.AppendInt32((int) currentY);
		Message.AppendString("0.0");
		Message.AppendInt32((int) botRotation);
		Message.AppendInt32((int) botRotation);
		Message.AppendString("/" + Status +"/");
		botRoom.Send(Message);
	}
	public void GenerateNode()
	{
		
	}
}
