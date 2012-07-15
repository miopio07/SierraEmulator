package sierra.habbohotel.messenger.buddies;

import sierra.Sierra;
import sierra.habbo.users.Habbo;
import sierra.habbo.users.UserGenerator;

public class Buddy
{
	private int Id;
	private int CatId;
	private Habbo Entity;
	private Boolean Online = false;

	public Boolean getOnline() {
		return Online;
	}

	public Buddy(int UserId, int CatId)
	{
		/*
		 * Set the users id
		 */
		setId(UserId);

		/*
		 * Set the category id
		 */
		setCatId(CatId);

		/*
		 * Set the buddy online
		 */
		BuddyOnline();

		/*
		 * Attach an entity to the buddy
		 */
		AttachEntity();
	}

	/*
	 * Return if the buddy is online
	 */
	public void BuddyOnline()
	{
		Online = Sierra.getServer().getActiveConnections().UserByIdOnline(getId());
	}

	public void AttachEntity()
	{
		setEntity(UserGenerator.IdentityById(getId()).getHabbo());
	}

	public Habbo getHabbo()
	{
		return Entity;
	}

	public void setEntity(Habbo entity)
	{
		Entity = entity;
	}

	public int getId()
	{
		return Id;
	}
	public void setId(int id)
	{
		Id = id;
	}

	public int getCatId()
	{
		return CatId;
	}

	public void setCatId(int catId)
	{
		CatId = catId;
	}

}
