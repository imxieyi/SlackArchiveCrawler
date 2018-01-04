package matcher;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionMatcher {

    public ArrayList<String> match(String text) {
        ArrayList<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<=<@)([A-Z0-9]{9})(?=>)");
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new MentionMatcher().match("<@123456789><@123><@123456D89><@1234567d9><@123456c789>"));
    }

}
