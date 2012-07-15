package sierra.messages;

import java.util.HashMap;
import java.util.Map;

import sierra.composers.Incoming;
import sierra.messages.catalogue.LoadCatalogueIndex;
import sierra.messages.catalogue.LoadCataloguePage;
import sierra.messages.console.GetFriends;
import sierra.messages.console.SendMessage;
import sierra.messages.handshake.*;
import sierra.messages.item.AddPapers;
import sierra.messages.item.ChangeItemState;
import sierra.messages.item.GetInventory;
import sierra.messages.item.MoveRotateItem;
import sierra.messages.item.PickUpItem;
import sierra.messages.item.PlaceItem;
import sierra.messages.item.RequestBuyItem;
import sierra.messages.navigator.AllRooms;
import sierra.messages.navigator.CanCreateRoom;
import sierra.messages.navigator.CreateRoom;
import sierra.messages.navigator.LeaveRoom;
import sierra.messages.navigator.LookPublicRooms;
import sierra.messages.navigator.OwnRooms;
import sierra.messages.navigator.SearchRooms;
import sierra.messages.room.ChangeLooks;
import sierra.messages.room.Chat;
import sierra.messages.room.GetRoomData;
import sierra.messages.room.InitRoom;
import sierra.messages.room.LoadHeightmap;
import sierra.messages.room.Shout;
import sierra.messages.room.ShowSign;
import sierra.messages.room.Walk;
import sierra.messages.snowstorm.InitSnowStorm;
import sierra.messages.user.LoadBadges;
import sierra.messages.user.LoadClub;
import sierra.messages.user.LoadMyCredits;
import sierra.messages.user.LoadMyData;
import sierra.messages.user.LoadUserProfile;
import sierra.messages.user.PingSession;

public class MessageHandler
{
	private Map<Short, MessageEvent> messages;
	
	public MessageHandler()
	{
		this.messages = new HashMap<Short, MessageEvent>();
	}
	public boolean contains(int id)
	{
		return this.messages.containsKey((short)id);
	}
	
	public MessageEvent get(Short id)
	{
		if (this.messages.containsKey(id))
		{
			return this.messages.get(id);
		}
		else {
			return null;
		}
	}
	
	public void register()
	{
		// Login
		this.messages.put(Incoming.ReadRelease, new Login());
		
		// Users
		this.messages.put(Incoming.Ping, new PingSession());
		this.messages.put(Incoming.GetProfile, new LoadUserProfile());
		this.messages.put(Incoming.MyData, new LoadMyData());
		this.messages.put(Incoming.GetCredits, new LoadMyCredits());
		this.messages.put(Incoming.LoadBadgesInventary, new LoadBadges());
		this.messages.put(Incoming.LoadClub, new LoadClub());
		
		// Catalogue
		this.messages.put(Incoming.GetCataIndex, new LoadCatalogueIndex());
		this.messages.put(Incoming.GetCataPage, new LoadCataloguePage());
		
		// Messenger
		this.messages.put(Incoming.GetFriends, new GetFriends());
		this.messages.put(Incoming.TalkOnChat, new SendMessage());
		
		// SnowStorm
		this.messages.put(Incoming.InitSnowStorm, new InitSnowStorm());
		
		// Navigator
		this.messages.put(Incoming.LookOnAllRooms, new AllRooms());
		this.messages.put(Incoming.LookOnAllRooms, new AllRooms());
		this.messages.put(Incoming.OwnRooms, new OwnRooms());
		this.messages.put(Incoming.StartRoom, new InitRoom());
		this.messages.put(Incoming.LoadHeightmap, new LoadHeightmap());
		this.messages.put(Incoming.CanCreateRoom, new CanCreateRoom());
		this.messages.put(Incoming.CreateNewRoom, new CreateRoom());
		this.messages.put(Incoming.LeaveRoom, new LeaveRoom());
		this.messages.put(Incoming.PublicRooms, new LookPublicRooms());
		this.messages.put(Incoming.SearchRoom, new SearchRooms());
		
		// Room
		this.messages.put(Incoming.Talk, new Chat());
		this.messages.put(Incoming.Shout, new Shout());
		this.messages.put(Incoming.RequestWalk, new Walk());
		this.messages.put(Incoming.LoadRoomData, new GetRoomData());
		this.messages.put(Incoming.ChangeLooks, new ChangeLooks());
		this.messages.put(Incoming.Sign, new ShowSign());
		
		// Item Handling
		this.messages.put(Incoming.ReqBuyItem, new RequestBuyItem());
		this.messages.put(Incoming.LoadFloorInventory, new GetInventory());
		this.messages.put(Incoming.LoadFloorInventory, new GetInventory());
		this.messages.put(Incoming.PlaceItem, new PlaceItem());
		this.messages.put(Incoming.MoveOrRotateItem, new MoveRotateItem());
		this.messages.put(Incoming.PickUpItem, new PickUpItem());
		this.messages.put(Incoming.UpdatePapers, new AddPapers());
		this.messages.put(Incoming.OnClickOnItem, new ChangeItemState());
	}
}
