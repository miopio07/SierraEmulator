package sierra.messages.catalogue;

import java.util.List;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.catalogue.index.CatalogueIndex;
import sierra.habbohotel.catalogue.index.CatalogueIndexEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LoadCatalogueIndex implements MessageEvent
{
	@Override
	public void Parse(Session Session, ClientMessage msg)
	{
		List<CatalogueIndex> MainList = CatalogueIndexEngine.GetMainCatalogueIndexList(Session.getHabbo().Rank);

		ServerMessage Message = new ServerMessage();

		Message.Initialize(Outgoing.CataIndex);
		Message.AppendBoolean(true);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendInt32(-1);
		Message.AppendBoolean(false);
		Message.AppendBoolean(false);
		Message.AppendInt32(MainList.size());

		for (CatalogueIndex Page : MainList)
		{
			List<CatalogueIndex> SubList = CatalogueIndexEngine.GetSubCatalogueIndexList(Page.Id, Session.getHabbo().Rank);

			Message.AppendBoolean(true);
			Message.AppendInt32(Page.IconColor);
			Message.AppendInt32(Page.IconImage);
			Message.AppendInt32(Page.Id);
			Message.AppendString(Page.Caption);
			Message.AppendInt32(SubList.size());

			for (CatalogueIndex sPage : SubList)
			{
				Message.AppendBoolean(true);
				Message.AppendInt32(sPage.IconColor);
				Message.AppendInt32(sPage.IconImage);
				Message.AppendInt32(sPage.Id);
				Message.AppendString(sPage.Caption);
				Message.AppendInt32(0);
			}
		}
		Message.AppendBoolean(false); // new items shit!
		Session.Send(Message);
	}

}
