package sierra.habbo.users;

import sierra.habbo.Session;

public class UserGenerator
{
	public static Session IdentityByUsername(String Username)
	{
		/*
		 *  Generate session
		 */
		Session requestSession = new Session(Username);
		
		/*
		 * Return generated session
		 */
		return requestSession;
	}
	public static Session IdentityById(int Id)
	{
		/*
		 *  Generate session
		 */
		Session requestSession = new Session(Id);
		
		/*
		 * Return generated session
		 */
		return requestSession;
	}
}
