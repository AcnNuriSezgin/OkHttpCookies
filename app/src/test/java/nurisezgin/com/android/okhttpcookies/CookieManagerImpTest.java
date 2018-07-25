package nurisezgin.com.android.okhttpcookies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by nuri on 25.07.2018
 */
@RunWith(MockitoJUnitRunner.class)
public class CookieManagerImpTest {

    @Mock
    CookieStore mockCookieStore;

    private CookieManager cookieManager;

    @Before
    public void setUp_() {
        cookieManager = new CookieManagerImp(mockCookieStore);
    }

    @Test
    public void should_OnHandleCookiesCorrect() {
        final List<String> expected = new ArrayList<>();
        expected.add("cookie2=valueNew");
        expected.add("cookie3=valueOld");
        expected.add("cookie1=valueNew2");
        final String host = "www.google.com";

        List<String> cookies = new ArrayList<>();
        cookies.add("cookie1=valueNew");
        cookies.add("cookie2=valueNew");
        cookies.add("cookie1=valueNew2");

        HashSet<String> oldCookies = new HashSet<>();
        oldCookies.add("cookie1=valueOld");
        oldCookies.add("cookie2=valueOld");
        oldCookies.add("cookie3=valueOld");

        when(mockCookieStore.get(anyString()))
                .thenReturn(oldCookies);

        cookieManager.onHandleCookies(host, cookies);

        verify(mockCookieStore)
                .put(anyString(), argThat(new CookieMatcher(expected)));
    }

    @Test
    public void should_ProvideCookiesCorrect() {
        final String host = "host1.google.com";
        final String cookieValue = "value";

        HashSet<String> temporaryCookies = new HashSet<>();
        temporaryCookies.add(cookieValue);

        when(mockCookieStore.get(anyString()))
                .thenReturn(temporaryCookies);

        HashSet<String> cookies = cookieManager.provideCookies(host);

        String actual = cookies.iterator().next();

        assertThat(actual, is(cookieValue));
    }

    private static class CookieMatcher extends ArgumentMatcher<HashSet> {

        private final List<String> expected;

        public CookieMatcher(List<String> expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object argument) {
            HashSet<String> actual = (HashSet<String>) argument;
            boolean areEqual = actual.size() == expected.size();

            for (String act: actual) {
                areEqual = expected.contains(act);
            }

            return areEqual;
        }
    }

}
