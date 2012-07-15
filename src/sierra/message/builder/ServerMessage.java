package sierra.message.builder;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;

public class ServerMessage
{
	private ChannelBuffer body;
	private int Id;
	private ChannelBufferOutputStream bodystream;
	//private String Username;

	public ServerMessage()
	{
	}
	public ServerMessage Initialize(int id)
	{
		this.Id = id;
		this.body = ChannelBuffers.dynamicBuffer();
		this.bodystream = new ChannelBufferOutputStream(body);

		try {
			this.bodystream.writeInt(0);
			this.bodystream.writeShort(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public ServerMessage AppendString(String obj)
	{
		try {
			bodystream.writeUTF(obj);
		} catch (IOException e) {
		}
		return this;
	}
	public ServerMessage AppendString(int obj)
	{
		try {
			bodystream.writeUTF("" + obj);
		} catch (IOException e) {
		}
		return this;
	}
	public void AppendInt32(Integer obj)
	{
		try {
			bodystream.writeInt(obj);
		} catch (IOException e) {
		}
	}
	public ServerMessage AppendShort(int obj)
	{
		try {
			bodystream.writeShort((short)obj);
		} catch (IOException e) {
		}
		return this;
	}
	public ServerMessage AppendBoolean(Boolean obj)
	{
		try {
			bodystream.writeBoolean(obj);
		} catch (IOException e) {
		}
		return this;
	}
	public void AppendBody(ISerialize Obj)
	{
		try {
			Obj.SerializePacket(this);
		} catch (Exception e) {
		}
	}
	public String getBodyString()
	{
		String str = new String(this.body.toString(Charset.defaultCharset()));

		String consoleText = str;

		for (int i = 0; i < 13; i++)
		{ 
			consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
		}

		return consoleText;
	}
	
	public int getHeader() {

		return Id;
	}
	
	public ChannelBuffer get() {

		body.setInt(0, body.writerIndex() - 4);

		//sLogger Logger = sLogger.getLogger(ServerMessage.class);

		//Logger.date("(" + this.Username + ") ---> SENT " + this.Id + " / "+ getBodyString());

		return this.body;
	}
}
