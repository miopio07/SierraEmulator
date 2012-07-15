package sierra.messages.room;

import sierra.habbo.Session;
import sierra.message.builder.ClientMessage;
import sierra.messages.MessageEvent;


public class ShowSign implements MessageEvent, Runnable
{
	private Session Signer;
	private ClientMessage Request;

	@Override
	public void Parse(Session Session, final ClientMessage Request)
	{
		if (!Session.getRoomUser().InRoom)
			return;

		this.Signer = Session;
		this.Request = Request;

		Thread Sign = new Thread(this);
		Sign.start();

	}

	@Override
	public void run()
	{
		try
		{
			Signer.getRoomUser().SendStatus("sign " + Request.popInt());
			{
				Thread.sleep(1500);
			}
			Signer.getRoomUser().SendStatus("");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

}
