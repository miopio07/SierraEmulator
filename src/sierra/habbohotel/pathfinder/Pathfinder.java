package sierra.habbohotel.pathfinder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import sierra.habbo.Session;
import sierra.habbohotel.room.flooritems.FloorItem;

import sierra.habbohotel.models.SquareState;

public class Pathfinder
{
	private Point[] AvaliablePoints;

	private int MapSizeX = 0;
	private int MapSizeY = 0;

	private SquareState[][] Squares;

	private Session User;
	public int GoX;
	public int GoY;
	public Pathfinder(Session Session)
	{
		AvaliablePoints = new Point[]
				{ 
				new Point(0, -1),
				new Point(0, 1),
				new Point(1, 0),
				new Point(-1, 0),
				new Point(1, -1),
				new Point(-1, 1),
				new Point(1, 1),
				new Point(-1, -1)
				};

		MapSizeX = Session.getRoomUser().getModel().MapSizeX;
		MapSizeY = Session.getRoomUser().getModel().MapSizeY;
		Squares = Session.getRoomUser().getModel().Squares;

		this.User = Session;
	}

	public List<Coord> PathCollection()
	{
		List<Coord> CoordSquares = new ArrayList<Coord>();

		int UserX = User.getRoomUser().X;
		int UserY = User.getRoomUser().Y;

		GoX = User.getRoomUser().GoalX;
		GoY = User.getRoomUser().GoalY;

		Coord LastCoord = new Coord(-200, -200);
		int Trys = 0;

		while(true)
		{
			Trys++;
			int StepsToWalk = 10000;
			for(Point MovePoint: AvaliablePoints)
			{
				int newX = MovePoint.x + UserX;
				int newY = MovePoint.y + UserY;

				if (newX >= 0 && newY >= 0 && MapSizeX > newX && MapSizeY > newY && Squares[newX][newY] == SquareState.WALKABLE && !User.getRoomUser().getCurrentRoom().CheckUserCoordinates(User, newX, newY) && !CheckFurniCoordinates(newX, newY))
				{
					Coord pCoord = new Coord(newX, newY);
					pCoord.PositionDistance = DistanceBetween(newX, newY, GoX, GoY);
					pCoord.ReversedPositionDistance = DistanceBetween(GoX, GoY, newX, newY);

					if(StepsToWalk > pCoord.PositionDistance)
					{
						StepsToWalk = pCoord.PositionDistance;
						LastCoord = pCoord;
					}
				}
			}
			if(Trys >= 200)
				return null;

			else if(LastCoord.X == -200 && LastCoord.Y == -200)
				return null;

			UserX = LastCoord.X;
			UserY = LastCoord.Y;
			CoordSquares.add(LastCoord);

			if(UserX == GoX && UserY == GoY)
				break;
		}
		return CoordSquares;
	}
	public Boolean CheckFurniCoordinates(int X, int Y)
	{
		for (FloorItem Item : User.getRoomUser().getCurrentRoom().getItemListByCoords(X, Y))
		{
			if (Item == null || (Item.GetBaseItem().InteractionType.equals("gate") && Item.ExtraData.equals("1")) || (Item.GetBaseItem().CanSit || Item.GetBaseItem().IsWalkable))
			{
				return false;
			}
			else if (Item.X == X && Item.Y == Y && !Item.GetBaseItem().CanSit)
			{
				return true;
			}
			else
			{
				List<AffectedTile> AffectedTiles = AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation);

				for (AffectedTile Tile : AffectedTiles)
				{
					if ((Item.GetBaseItem().InteractionType.equals("gate") && Item.ExtraData.equals("1"))  || (Item.GetBaseItem().CanSit || Item.GetBaseItem().IsWalkable))
					{	
						return false;
					}
					else if (X == Tile.X && Y == Tile.Y && !Item.GetBaseItem().CanSit)
					{
						return true;
					}
				}
			}
		}
		/*for (AffectedTile Tile : User.GetRoomUser().GetRoom().CheckAffectedCoordinates(X, Y))
		{
			FloorItem fItem = User.GetRoomUser().GetRoom().GrabFurniByCoords(X, Y);

			if (fItem != null)
				return fItem.GetBaseItem().IsWalkable ? false : true;
			else
			{
				if (Tile.X == X && Tile.Y == Y)
					return true;
				else
					return false;
			}

		}*/
		return false;
	}
	private int DistanceBetween(int currentX, int currentY, int goX, int goY)
	{
		return Math.abs(currentX - goX) + Math.abs(currentY - goY);
	}

}