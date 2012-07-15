package sierra.habbo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.channel.Channel;

public class SessionManager implements Runnable {

	private Thread connectionThread;
	private ConcurrentMap<Integer, Session> Sessions;

	public SessionManager()
	{
		Sessions = new ConcurrentHashMap<Integer, Session>();

		connectionThread = new Thread(this);
		/*
		 * Start ping event to check if the user is still connected.
		 */
		connectionThread.setPriority(Thread.NORM_PRIORITY);
		connectionThread.start();
	}

	/*
	 * Add session if the same session doesn't already exist
	 */
	public boolean addSession(Channel channel)
	{
		return Sessions.putIfAbsent(channel.getId(), new Session(channel)) == null;
	}

	/*
	 * Remove the session.
	 */
	public void removeSession(Channel channel)
	{
		try {
			Sessions.remove(channel.getId());
		} catch (Exception e) {
		}
	}

	/*
	 * Grab user by channel id.
	 */
	public Session GetUserByChannel(Channel channel)
	{
		return Sessions.get(channel.getId());
	}

	/*
	 * Return a collection of sessions
	 */
	public ConcurrentMap<Integer, Session> getSessions()
	{
		return Sessions;
	}

	/*
	 * Return a true/false if the session exists in the collection
	 */
	public boolean hasSession(Channel channel)
	{
		return Sessions.containsKey(channel.getId());
	}


	public boolean UserByIdOnline(int id)
	{
		for (Session Session : Sessions.values())
			if (Session.getHabbo() != null)
				if (Session.getHabbo().Id == id)
					return true;
		return false;
	}
	public Session GetUserByName(String Name)
	{
		for (Session Session : Sessions.values())
			if (Session.getHabbo() != null)
				if (Session.getHabbo().Username.equals(Name))
					return Session;
		return null;
	}
	public Session GetUserById(int UserId)
	{
		for (Session Session : Sessions.values())
			if (Session.getHabbo() != null)
				if (Session.getHabbo().Id == UserId)
					return Session;
		return null;
	}

	/*
	 * Thread loop for checking if Sessions have disconnected.
	 */
	@Override
	public void run()
	{
		while (true)
		{
			for (Session Session : Sessions.values())
			{
				if (!Session.PingOK)
				{
					Session.dispose();
					removeSession(Session.getChannel());
				}
			}
		}
	}
}
