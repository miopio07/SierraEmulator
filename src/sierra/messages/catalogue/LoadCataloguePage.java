package sierra.messages.catalogue;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.catalouge.pages.CataloguePage;
import sierra.habbohotel.catalouge.pages.CataloguePageEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadCataloguePage implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage msg)
	{
			int Id = msg.popInt();
			CataloguePage Page = CataloguePageEngine.GetInstanceByName(Id);

			if (Page != null)
			{
				if (Page.Layout.equals("frontpage"))
				{
					ServerMessage Message = new ServerMessage();

					Message.Initialize(Outgoing.CataPage);
					Message.AppendInt32(Id);
					Message.AppendString("frontpage3");
					Message.AppendInt32(3);
					Message.AppendString(Page.Headline);
					Message.AppendString(Page.Teaser);
					Message.AppendString("");
					Message.AppendInt32(11);
					Message.AppendString(Page.Special);
					Message.AppendString(Page.Text1);
					Message.AppendString("");
					Message.AppendString(Page.Text2);
					Message.AppendString(Page.Details);
					Message.AppendString(Page.Teaser2);
					Message.AppendString("Rares");
					Message.AppendString("#FEFEFE");
					Message.AppendString("#FEFEFE");
					Message.AppendString("Click here for more info..");
					Message.AppendString("magic.credits");
					Message.AppendInt32(0);
					Message.AppendInt32(-1);
					Session.Send(Message);
				}
				else if (Page.Layout.equals("default_3x3"))
				{
					ServerMessage Message = new ServerMessage();

					Message.Initialize(Outgoing.CataPage);
					Message.AppendInt32(Id);
					Message.AppendString(Page.Layout);
					Message.AppendInt32(3);
					Message.AppendString(Page.Headline);
					Message.AppendString(Page.Teaser);
					Message.AppendString(Page.Special);
					Message.AppendInt32(3);
					Message.AppendString(Page.Text1);
					Message.AppendString(Page.Details);
					Message.AppendString(Page.Teaser2);
					Page.SerializeItems(Id, Message);
					Message.AppendInt32(0);
					Session.Send(Message);
				}
				else if (Page.Layout.equals("spaces"))
				{
					ServerMessage Message = new ServerMessage();

					Message.Initialize(Outgoing.CataPage);
					Message.AppendInt32(Id);
					Message.AppendString("spaces_new");
					Message.AppendInt32(1);
					Message.AppendString(Page.Headline);
					Message.AppendInt32(1);
					Message.AppendString(Page.Text1);
					Page.SerializeItems(Id, Message);
					Message.AppendInt32(0);
					Session.Send(Message);
				}
				else if (Page.Layout.equals("trophies"))
				{
					ServerMessage Message = new ServerMessage();

					Message.Initialize(Outgoing.CataPage);
					Message.AppendInt32(Id);
					Message.AppendString("trophies");
					Message.AppendInt32(1);
					Message.AppendString(Page.Headline);
					Message.AppendInt32(2);
					Message.AppendString(Page.Text1);
					Message.AppendString(Page.Details);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(-1);
					Session.Send(Message);
				}
				else if (Page.Layout.equals("trophies"))
				{
					ServerMessage Message = new ServerMessage();

					Message.Initialize(Outgoing.CataPage);
					Message.AppendInt32(Id);
					Message.AppendString("trophies");
					Message.AppendInt32(1);
					Message.AppendString(Page.Headline);
					Message.AppendInt32(2);
					Message.AppendString(Page.Text1);
					Message.AppendString(Page.Details);
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(-1);
					Session.Send(Message);
				}
				else if (Page.Layout.equals("pets"))
				{
					ServerMessage Message = new ServerMessage();

					Message.Initialize(Outgoing.CataPage);
					Message.AppendInt32(Id);
					Message.AppendString("pets");
					Message.AppendInt32(2);
					Message.AppendString(Page.Headline);
					Message.AppendString(Page.Teaser);
					Message.AppendInt32(4);
					Message.AppendString(Page.Text1);
					Message.AppendString("Give a name:");
					Message.AppendString("Pick a color:");
					Message.AppendString("Pick a race:");
					Message.AppendInt32(0);
					Message.AppendInt32(0);
					Message.AppendInt32(-1);
					Session.Send(Message);
				}
			}
	}
}
