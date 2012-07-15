package sierra.utils;

public class UserInputFilter
{
	public static String filterString(String str, boolean allowLineBreaks) {
		
		str = str.replace((char) 1, ' ');
		str = str.replace((char) 2, ' ');
		str = str.replace((char) 3, ' ');
		str = str.replace((char) 9, ' ');
		
		if (!allowLineBreaks) {
			str = str.replace((char) 10, ' ');
			str = str.replace((char) 13, ' ');
		}
		
		return str;
	}
}
