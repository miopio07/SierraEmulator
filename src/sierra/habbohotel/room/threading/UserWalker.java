package sierra.habbohotel.room.threading;

import java.util.List;

import sierra.habbo.Session;
import sierra.habbohotel.pathfinder.Coord;
import sierra.habbohotel.pathfinder.Pathfinder;
import sierra.habbohotel.pathfinder.Rotation;
import sierra.habbohotel.room.flooritems.FloorItem;
import sierra.messages.navigator.LeaveRoom;

public class UserWalker implements Runnable
{
	private Session mSession;
	public Boolean Stop;

	public UserWalker(Session mSession) {
		this.mSession = mSession;
		this.Stop = false;
	}

	@Override
	public void run()
	{
		try {
			/*
			 * Calculate the path
			 */
			Pathfinder Path = new Pathfinder(mSession);

			List<Coord> ListOfCoords = Path.PathCollection();

			for (Coord Following : ListOfCoords)
			{
				if (!Stop)
				{
					try
					{
						int X = Following.X;
						int Y = Following.Y;

						mSession.getRoomUser().BodyRotation = Rotation.Calculate(mSession.getRoomUser().X, mSession.getRoomUser().Y, X, Y);
						mSession.getRoomUser().Height = mSession.getRoomUser().getModel().getTileHeight(X, Y);
						mSession.getRoomUser().SendStatus("mv "+ X +","+ Y +"," + Double.toString(mSession.getRoomUser().Height));	
						mSession.getRoomUser().X = X;
						mSession.getRoomUser().Y = Y;
						mSession.getRoomUser().SelectedSquare = true;
						Thread.sleep(500);

						if (X == mSession.getRoomUser().getModel().DoorX && Y == mSession.getRoomUser().getModel().DoorY)
						{
							new LeaveRoom().Parse(mSession, null);
						}
					}
					catch (Exception e)
					{
						BlankTile();
					}
				}
				else
				{
					Thread.sleep(0);
				}
			}
			BlankTile();

			List<FloorItem> Items = mSession.getRoomUser().getCurrentRoom().getItemListByCoords(mSession.getRoomUser().X, mSession.getRoomUser().Y);

			for (FloorItem Item : Items)
			{
				if (Item.GetBaseItem().CanSit)
				{
					mSession.getRoomUser().IsSit = true;
					mSession.getRoomUser().BodyRotation = Item.Rotation;
					mSession.getRoomUser().SendStatus("sit 1.0");
				}
				else
				{
					BlankTile();
				}
			}
		} catch (Exception e) {
			/*mSession.getRoomUser().mUserWalk = new Thread(new UserWalker(mSession));
			mSession.getRoomUser().mUserWalk.start();*/
		}	
	}
	public void BlankTile()
	{
		mSession.getRoomUser().SelectedSquare = false;
		mSession.getRoomUser().IsSit = false;
		mSession.getRoomUser().SendStatus("");
	}
}
