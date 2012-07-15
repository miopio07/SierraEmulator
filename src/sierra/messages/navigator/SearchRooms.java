package sierra.messages.navigator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.room.RoomEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class SearchRooms implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		try
		{
			String Name = Request.popFixedString();
			if (Name.isEmpty())
				return ;

			ServerMessage Message = new ServerMessage();

			int UserId = 0;
			int Count = 0;
			ResultSet Row = null;

			if (Name.split(":")[0].equals("owner"))
			{
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM members WHERE username = ?");
				{
					Statement.setString(1, Name.split(":")[1]);
					Row = Statement.executeQuery();
				}
				
				while (Row.next())
				{
					UserId = Row.getInt(1);
					System.out.println(UserId);
				}

				Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM rooms WHERE ownerid = '" + UserId + "'");
				Row = Statement.executeQuery();

				Count = Sierra.getDatabase().getRowCount("SELECT * FROM rooms WHERE ownerid = '" + UserId + "'");
			}
			else
			{
				PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM rooms WHERE name LIKE ? ");
				{
					Statement.setString(1, Name + "%");
				}
				
				Row = Statement.executeQuery();
				
				PreparedStatement CountStatement = Sierra.getDatabase().ExecuteQuery("SELECT * FROM rooms WHERE name LIKE ? ");
				{
					CountStatement.setString(1, Name + "%");
				}
				
				ResultSet CountSet = CountStatement.executeQuery();
				
				while (CountSet.next())
					Count++;

			}

			Message.Initialize(Outgoing.OwnRooms);
			Message.AppendInt32(5);
			Message.AppendString("");
			Message.AppendInt32(Count);
			{
				while(Row.next())
				{
					Message.AppendInt32(Row.getInt("id"));
					Message.AppendBoolean(false);
					Message.AppendString(Row.getString("name"));
					Message.AppendInt32(Row.getInt("ownerid"));
					Message.AppendString(Sierra.getDatabase().ReadString("username", "SELECT username FROM members WHERE id = '" + Row.getInt("id") + "'"));
					Message.AppendInt32(0);
					Message.AppendInt32(RoomEngine.GetById(Row.getInt("ownerid")) != null ? RoomEngine.GetById(Row.getInt("id")).SessionList.size() : 0);
					Message.AppendInt32(25);
					Message.AppendString(Row.getString("description"));
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(1337);
					Message.AppendInt32(2);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendString("");
					Message.AppendBoolean(true);
					Message.AppendBoolean(true);

				}
				Message.AppendBoolean(false);
			}
			Session.Send(Message);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
}
