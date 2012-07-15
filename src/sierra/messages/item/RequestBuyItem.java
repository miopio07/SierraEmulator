package sierra.messages.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sierra.Sierra;
import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.catalogue.items.CatalogueItem;
import sierra.habbohotel.catalogue.items.CatalogueItemEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;
import sierra.messages.user.LoadMyCredits;

public class RequestBuyItem implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		Integer PageId = Request.popInt();
		Integer ItemId = Request.popInt();

		CatalogueItem CataItem = CatalogueItemEngine.GetCatalogueItems().get(ItemId);

		if (!CataItem.equals(null) && CataItem.PageId == PageId)
		{
			ServerMessage Message = new ServerMessage();

			// Subtract Users Coins - if you're staff, we won't bother
			if (!Session.getHabbo().hasStaffPermission("fuse_mod"))
			{
				if (CataItem.Credits != 0)
				{
					if (Session.getHabbo().Credits >= CataItem.Credits) // Has enough credits
					{
						Session.getHabbo().Credits -= CataItem.Credits;

						//Update credits
						new LoadMyCredits().Parse(Session, null);

						// MySQL update.
						Session.getHabbo().SaveData();
					}
					else
					{
						return;
					}
				}
				if (CataItem.Pixels != 0)
				{
					if (Session.getHabbo().Pixels >= CataItem.Pixels) // Has enough credits
					{
						Session.getHabbo().Pixels -= CataItem.Pixels;

						Message.Initialize(Outgoing.Pixels);
						Message.AppendInt32(1);
						Message.AppendInt32(0);
						Message.AppendInt32(Session.getHabbo().Pixels);
						Session.Send(Message);

						// MySQL update.
						Session.getHabbo().SaveData();
					}
					else
					{
						return;
					}
				}
			}
			if (CataItem.GetBaseItem().InteractionType.equals("pet"))
			{
				return; // Haven't coded pets yet.
			}
			else
			{
				Message.Initialize(Outgoing.BroughtItem);
				Message.AppendInt32(CataItem.Id);
				Message.AppendString(CataItem.GetBaseItem().PublicName);
				Message.AppendInt32(CataItem.Credits);
				Message.AppendInt32(CataItem.Pixels);
				Message.AppendInt32(0);
				Message.AppendInt32(0);
				Message.AppendBoolean(true);
				Message.AppendString(CataItem.GetBaseItem().Type);
				Message.AppendInt32(CataItem.GetBaseItem().SpriteId);
				Message.AppendString("");                
				Message.AppendInt32(1);
				Message.AppendInt32(0);
				Message.AppendInt32(0); 
				Session.Send(Message);

				Message.Initialize(Outgoing.AlertNewItems);
				Message.AppendInt32(1);
				int Type = 2;
				if(CataItem.GetBaseItem().Type.equals("s"))
				{
					if(CataItem.GetBaseItem().InteractionType.equals("pet"))
					{
						Type = 3;
					}
					else
					{
						Type = 1;
					}
				}
				Message.AppendInt32(Type);
				Message.AppendInt32(CataItem.Amount);

				for(int i = 0; i != CataItem.Amount; i++)
				{
					try
					{
						int id = 0;
						PreparedStatement Statement = Sierra.getDatabase().ExecuteQuery("INSERT INTO members_inventory (`owner`, `itemid`, `label`) VALUES (?, ?, ?);");
						{
							Statement.setInt(1, Session.getHabbo().Id);
							Statement.setInt(2, CataItem.ItemId);
							Statement.setString(3, CataItem.Label);
							Statement.executeUpdate();

							ResultSet Keys = Statement.getGeneratedKeys();

							if (Keys.next())
								id = Keys.getInt(1);
						}
						Session.getInventory().AddSingle(id, CataItem.Label, CataItem.ItemId);
						Message.AppendInt32(id);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				Session.Send(Message);

				Message.Initialize(Outgoing.UpdateInventory);
				Session.Send(Message);

				new GetInventory().Parse(Session, null);
			}

		}
		else
		{
			return;
		}
	}
}
