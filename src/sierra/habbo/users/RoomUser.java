package sierra.habbo.users;

import java.util.List;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.models.Model;
import sierra.habbohotel.models.ModelEngine;
import sierra.habbohotel.pathfinder.Rotation;
import sierra.habbohotel.room.Room;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.habbohotel.room.threading.UserWalker;
import sierra.message.builder.ServerMessage;

public class RoomUser
{
	private Session mSession;
	public Thread WalkThread;
	public UserWalker UserWalker;
	public Room Room;
	public int X;
	public int Y;
	public int GoalX;
	public int GoalY;
	public int BodyRotation;
	public double Height;
	public Boolean InRoom;
	public Boolean SelectedSquare;
	public boolean IsSit;
	public Boolean NeedsWalkUpdate;

	public RoomUser(Session Session)
	{
		this.mSession = Session;
		this.Room = null;
		this.SelectedSquare = false;
		this.InRoom = false;
		this.IsSit = false;
		this.NeedsWalkUpdate = false;
		this.Height = 0;
	}

	public Boolean IsOwner() {
		if (getCurrentRoom().OwnerName.equals(mSession.getHabbo().Username))
			return true;
		else
			return false;
	}
	
	public void CalculateRotation() {
		this.BodyRotation = Rotation.Calculate(X, Y, GoalX, GoalY);
	}
	
	public void SendStatus(String Status) {
		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.RoomStatuses);
		Message.AppendInt32(1);
		Message.AppendInt32(mSession.getHabbo().Id);
		Message.AppendInt32(X);
		Message.AppendInt32(Y);
		Message.AppendString(Double.toString(Height));
		Message.AppendInt32(BodyRotation);
		Message.AppendInt32(BodyRotation);
		Message.AppendString("/" + Status +"/");

		if (mSession.getRoomUser().InRoom)
			getCurrentRoom().Send(Message);
	}
	
	public Room getCurrentRoom() {
		return Room;
	}
	
	public Model getModel() {
		return ModelEngine.GetInstanceByName(Room.Model);
	}

	public void UpdateUserStatus(Boolean BlankIfNoSuccess)
	{
		List<FloorItem> Items = mSession.getRoomUser().getCurrentRoom().getItemListByCoords(mSession.getRoomUser().X, mSession.getRoomUser().Y);

		if (Items.size() != 0)
		{
			for (FloorItem roomItem : Items)
			{
				if (roomItem.GetBaseItem().CanSit)
				{
					this.IsSit = true;
					this.BodyRotation = roomItem.Rotation;
					this.SendStatus("sit 1.3");
				}
				else
				{
					this.IsSit = false;
					
					if (BlankIfNoSuccess)
						this.SendStatus("");
				}
			}
		}
		else
		{
			this.IsSit = false;
			
			if (BlankIfNoSuccess)
				this.SendStatus("");
		}
	}

}
