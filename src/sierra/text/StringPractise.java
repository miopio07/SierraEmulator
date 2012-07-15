package sierra.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringPractise
{
	public static String getBetween(String haystack, String pre, String post) {
        Pattern pattern = Pattern.compile(pre+"(.+?)"+post);
        Matcher matcher = pattern.matcher(haystack);
        return (matcher.find() ? haystack.substring(matcher.start(1),matcher.end(1)) : "No match could be found.");
	}
}
