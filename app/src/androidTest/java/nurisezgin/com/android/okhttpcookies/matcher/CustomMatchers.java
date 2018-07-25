package nurisezgin.com.android.okhttpcookies.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashSet;

/**
 * Created by nuri on 25.07.2018
 */
public class CustomMatchers {

    public static Matcher<HashSet<String>> same(HashSet<String> dest) {
        return new TypeSafeMatcher<HashSet<String>>() {
            @Override
            protected boolean matchesSafely(HashSet<String> src) {
                boolean areEqual = src.size() == dest.size();

                for (String item : src) {
                    areEqual &= dest.contains(item);
                }

                return areEqual;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Set values are not equal each other");
            }
        };
    }

}
