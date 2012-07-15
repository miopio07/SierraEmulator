package sierra.messages.navigator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class CreateRoom implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		String Name = Request.popFixedString();
		String Model = Request.popFixedString();
		
		Boolean IsValid = Name.length() + Model.length() != 0 ? true : false;
		Boolean CanCreateAnotherRoom = RoomEngine.GetByOwnerId(Session.getHabbo().Id).size() != 25 ? true : false;
		
		if (IsValid && CanCreateAnotherRoom)
		{
			try
			{
				int Id = 0;
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("INSERT INTO rooms (`name`, `ownerid`, `description`, `status`, `password`, `model`, `wallpaper`, `floor`, `outside`)" +
						"VALUES (?, ?, '', 'open', '', ?, '0', '0', '0.0')");
				
				Statement.setString(1, Name);
				Statement.setInt(2, Session.getHabbo().Id);
				Statement.setString(3, Model);
				Statement.execute();
				
				ResultSet Keys = Statement.getGeneratedKeys();
				
				if (Keys.next())
					Id = Keys.getInt(1);
				
				RoomEngine.AddRoom(Id, Session.getHabbo().Id, Session.getHabbo().Username);
				
				ServerMessage Message = new ServerMessage();
				
				Message.Initialize(Outgoing.CreateNewRoom);
				Message.AppendInt32(Id);
				Message.AppendString(Name);
				Session.Send(Message);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			return;
		}
	}
}
