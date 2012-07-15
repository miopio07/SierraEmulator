package sierra;

import java.util.Date;
import java.text.SimpleDateFormat;

public class sLogger
{
	private Class<?> _Class;
	
	public sLogger(Class<?> Class) {
		this._Class = Class;
	}
	
	public static sLogger getLogger(Class<?> Class) {
		sLogger Logger = new sLogger(Class);
		return Logger;
	}
	
	public void date(Object format) {
		System.out.println("(" + getDate() + ") " + format.toString());
	}
	
	public void line() {
		System.out.println("(" + getDate() + ")");
	}
	
	public void info(Object format) {
		System.out.println("(" + getDate() + ") [" + _Class.getName() + " / INFO] --> " + format.toString());
	}
	
	public void fatal(Object format) {
		System.out.println("(" + getDate() + ") [" + _Class.getName() + " / FATAL] --> " + format.toString());
	}
	
	public void error(Object format) {
		System.out.println("(" + getDate() + ") [" + _Class.getName() + " / ERROR] --> " + format.toString());
	}
	
	public void warning(Object format) {
		System.out.println("(" + getDate() + ") [" + _Class.getName() + " / WARNING] --> " + format.toString());
	}
	
    private String getDate() {
        return new SimpleDateFormat("yyyy/MM/dd | HH:mm:ss").format(new Date());
    }
}
