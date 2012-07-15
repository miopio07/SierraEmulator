package sierra.messages.handshake;

import java.sql.PreparedStatement;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbo.SetSessionInfo;
import sierra.habbohotel.UpdateOnline;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class Login implements MessageEvent {
	@Override
	public void Parse(Session Session, ClientMessage msg)
	{
		ServerMessage Message = new ServerMessage();

		/*
		 * Verify by IP
		 */
		if (Sierra.getDatabase().RowExists("SELECT ip FROM members WHERE ip = '" + Session.getIpAddress() + "' LIMIT 1"))
		{
			// Do shit
			//Login
			Message.Initialize(Outgoing.InitSystem);
			Session.Send(Message);

			// Generate Habbo Data instance
			Session.setNewHabbo();

			// Set info
			SetSessionInfo mSessionInfo;
			mSessionInfo = new SetSessionInfo();
			mSessionInfo.setDetails(Session, Session.getHabbo(), Sierra.getDatabase().ReadRow("SELECT * FROM members WHERE ip = '" + Session.getIpAddress() + "'"));


			Message.Initialize(Outgoing.FuseRights);
			Message.AppendInt32(2); // 0 = none / 1 = hc / 2 = vip
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			//Message.AppendInt32(1);
			//Message.AppendInt32(0);
			/*if(Session.getHabbo().Rank == 7)
				Message.AppendInt32(7);
			else
				Message.AppendInt32(8);//Session.getHabbo().Rank);*/
			Session.Send(Message);
			
			Message.Initialize(Outgoing.Pixels);
			Message.AppendInt32(1); // Count
			Message.AppendInt32(0);
			Message.AppendInt32(Session.getHabbo().Pixels); // Count of type (pixels)
			Session.Send(Message);

			try
			{
				/*
				 * Cache up the data.
				 */
				Session.setNewInventory();
				Session.setNewMessenger();

				/*
				 * Update yo status
				 */
				Session.getMessenger().UpdateStatus(true);

				/*
				 * Cache up ya rooms
				 */
				RoomEngine.GetOwnRooms(Session.getHabbo().Id, Session.getHabbo().Username);
				
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("UPDATE members SET online = ? WHERE id = ?");
				{
					Statement.setInt(1, 1);
					Statement.setInt(2, Session.getHabbo().Id);
					Statement.execute();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			Message.Initialize(Outgoing.Alert);
			Message.AppendString("Welcome to Sierra, " + Session.getHabbo().Username + "!");
			Message.AppendString("");
			Session.Send(Message);
			
			Message.Initialize(1742);
			Session.Send(Message);

			if (Session.getHabbo().hasStaffPermission("fuse_mod"))
			{
				Message.Initialize(Outgoing.ModTool);
				Message.AppendInt32(-1);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);
				Message.AppendBoolean(true);
				Message.AppendInt32(0);
				Message.AppendBoolean(true);
				Session.Send(Message);
			}

			/*
			 * Is authenticated
			 */
			Session._Authenticated = true;

			/*
			 * Update online
			 */
			UpdateOnline.SetCount();
		}
	}
}