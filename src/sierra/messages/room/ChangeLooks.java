package sierra.messages.room;

import java.sql.PreparedStatement;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class ChangeLooks implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * grab ze data
		 */
		String Sex = Request.popFixedString();
		String Figure = Request.popFixedString();
		
		/*
		 * New server message instance
		 */
		ServerMessage Message = new ServerMessage();
		
		/*
		 * Build le packets
		 */
		Message.Initialize(Outgoing.UpdateInfo);
		Message.AppendInt32(Session.getHabbo().Id);
		Message.AppendString(Figure);
		Message.AppendString(Sex.toLowerCase());
		Message.AppendString(Session.getHabbo().Motto);
		Message.AppendInt32(0); //TODO: Achievement points
		Session.getRoomUser().getCurrentRoom().Send(Message);
		
		/*
		 * Update SQL
		 */
		try
		{
			PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("UPDATE members SET figure = ?, gender = ? WHERE username = ?");
			{
				Statement.setString(1, Figure);
				Statement.setString(2, Sex.toUpperCase());
				Statement.setString(3, Session.getHabbo().Username);
				Statement.execute();
				
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		/*
		 * Update Session
		 */
		Session.getHabbo().Figure = Figure;
		Session.getHabbo().Gender = Sex.toUpperCase();
	}

}
