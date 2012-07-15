package sierra.messages.navigator;

import java.util.List;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.navigator.cats.FrontpageCat;
import sierra.habbohotel.navigator.cats.FrontpageCatEngine;
import sierra.message.builder.ClientMessage;
import sierra.message.builder.ServerMessage;
import sierra.messages.MessageEvent;

public class LookPublicRooms implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request)
	{
		/*
		 * Get user's categories
		 */
		List<FrontpageCat> Cats = FrontpageCatEngine.getCategories(Session.getHabbo().Rank);
		
		/*
		 * New server message
		 */
		ServerMessage Categorys = new ServerMessage();
		
		/*
		 * Start the header
		 */
		Categorys.Initialize(Outgoing.NavigatorShowPublics);
		Categorys.AppendInt32(Cats.size());
		
		/*
		 * Serialise
		 */
		
		for (FrontpageCat Cat : Cats)
		{
			Categorys.AppendBody(Cat);
	        
		}
		
		/*
		 * Packet suffix
		 */
		Categorys.AppendInt32(-1);
        
        Session.Send(Categorys);
	}

}
