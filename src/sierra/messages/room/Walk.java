package sierra.messages.room;

import sierra.habbo.Session;
import sierra.habbohotel.room.threading.UserWalker;
import sierra.message.builder.ClientMessage;
import sierra.messages.MessageEvent;

public class Walk implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		int X = Request.popInt();
		int Y = Request.popInt();

		if (Session.getRoomUser().SelectedSquare == true)
		{
			Session.getRoomUser().IsSit = false;
			Session.getRoomUser().UserWalker.Stop = true;
			Session.getRoomUser().SelectedSquare = false;
			Session.getRoomUser().SendStatus("");
		}

		/*
		 * Check if a user already exists in that spot
		 */
		if (Session.getRoomUser().getCurrentRoom().CheckUserCoordinates(Session, X, Y))
			return;

		Session.getRoomUser().GoalX = X;
		Session.getRoomUser().GoalY = Y;
		Session.getRoomUser().UserWalker = new UserWalker(Session);
		Session.getRoomUser().WalkThread = new Thread(Session.getRoomUser().UserWalker);
		Session.getRoomUser().WalkThread.start();
	}
}
