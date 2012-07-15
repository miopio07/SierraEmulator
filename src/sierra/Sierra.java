package sierra;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import sierra.composers.Outgoing;
import sierra.habbo.Session;
import sierra.habbohotel.catalogue.index.CatalogueIndexEngine;
import sierra.habbohotel.catalogue.items.CatalogueItemEngine;
import sierra.habbohotel.catalouge.pages.CataloguePageEngine;
import sierra.habbohotel.furniture.FurnitureEngine;
import sierra.habbohotel.fuserights.FuserightEngine;
import sierra.habbohotel.messenger.categories.MessengerCatEngine;
import sierra.habbohotel.models.ModelEngine;
import sierra.habbohotel.navigator.cats.FrontpageCatEngine;
import sierra.message.builder.ServerMessage;
import sierra.netty.connections.Connection;
import sierra.storage.Storage;

public class Sierra
{
	private static Connection mConnection;
	private static Storage mMySQL;

	public static void main(String[] args)
	{
		sLogger Logger = sLogger.getLogger(Sierra.class);

		try
		{
			DataInputStream in = new DataInputStream(new FileInputStream(System.getProperty("user.dir").replaceAll("\\\\","/") + "\\startup_ascii.txt"));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine = "";

			//Logger.info(StringPractise.getBetween("cakelolhaha", "cake", "haha"));

			while ((strLine = br.readLine()) != null)
			{
				Logger.date(strLine);
			}

			Logger.line();


			Boolean IsWorking = true;
			InputStream is = null;
			Properties configFile = null;

			try
			{
				is = new Sierra().getClass().getResourceAsStream("/sierra.properties");  
				configFile = new Properties();  
				configFile.load(is);  

			}
			catch (Exception e)
			{
				Logger.info("Could not find the configuration file!");
				IsWorking = false;

			}

			if (IsWorking)
			{
				mMySQL = new Storage
						(
								configFile.getProperty("mysql.host") + ":3306",
								configFile.getProperty("mysql.user"), 
								configFile.getProperty("mysql.password"),
								configFile.getProperty("mysql.database")
								);

				Logger.info(configFile.getProperty("mysql.host"));
				Logger.info(configFile.getProperty("mysql.user"));
				Logger.info(configFile.getProperty("mysql.database"));
				Logger.info(configFile.getProperty("mysql.password"));
				
				if (!Storage.NoSQLException)
				{
					Logger.info("MySQL failed to connect, please check your settings!");
				}
				else
				{
					if (Sierra.createHotel())
					{
						Logger.info("MySQL connection successful.");

						Integer port = Integer.parseInt(configFile.getProperty("game.port"));

						mConnection = new Connection(port);
						if (mConnection.StartSocket())
						{	
							Logger.info("Server started on port " + port);

							/*
							 * Users online to zero
							 */
							Sierra.getDatabase().ExecuteQuery("UPDATE system SET online_count = 0").execute();
							Sierra.getDatabase().ExecuteQuery("UPDATE members SET online = 0").execute();

							while (true)
							{
								//  open up standard input
								BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

								String Input = null;

								//  read the username from the command-line; need to use try/catch with the
								//  readLine() method
								try
								{
									Input = input.readLine();

									String[] Arguments = Input.split(" ");
									
									if (Arguments[0].equals(":ha"))
									{
										for (Session User : Sierra.getServer().getActiveConnections().getSessions().values())
										{
											ServerMessage Message = new ServerMessage();
											
											Message.Initialize(Outgoing.Alert);
											Message.AppendString(Input.substring(Arguments[0].length() + 1) + "\n\n" 
													+ "Yours sincerely, Server");
											Message.AppendString("");
											User.Send(Message);
										}
									}
									else if (Arguments[0].equals(":troll"))
									{

										ServerMessage Message = new ServerMessage();

										Session User = Sierra.getServer().getActiveConnections().GetUserByName(Arguments[1]);

										if (User != null)
										{
											Message.Initialize(Outgoing.Talk);
											Message.AppendInt32(User.getHabbo().Id);
											Message.AppendString(Input.substring(Arguments[0].length() + Arguments[1].length() + 2));
											Message.AppendInt32(0);
											Message.AppendInt32(0);
											Message.AppendInt32(0);
											User.getRoomUser().getCurrentRoom().Send(Message);
										}
									}
								}
								catch (Exception ioe)
								{
									System.out.println("IO error trying to read your name!");
									System.exit(1);
								}
							}

						}
						else
						{ 
							Logger.date("Could not listen on port: " +  port);
						}
					}
					else
					{
						Logger.date("Could not create cache for hotel :(");
					}
				}
			}
		}
		catch (Exception e)
		{
			Logger.date("Unhandled exception when booting up Sierra: " + e.getMessage());
		}

	}
	public static Boolean createHotel()
	{
		sLogger Logger = sLogger.getLogger(Sierra.class);
		try
		{
			ModelEngine.LoadAll();
			CatalogueIndexEngine.LoadAll();
			CataloguePageEngine.LoadAll();
			FurnitureEngine.LoadAll();
			CatalogueItemEngine.LoadAll();
			MessengerCatEngine.LoadAll();
			FrontpageCatEngine.LoadAll();
			FuserightEngine.LoadAll();
			return true;
		}
		catch (Exception e)
		{
			Logger.date("Unhandled exception when creating cache: " + e.getMessage());
			return false;
		}
	}
	public static Connection getServer() {
		return mConnection;
	}

	public static Storage getDatabase() {
		return mMySQL;
	}
}
