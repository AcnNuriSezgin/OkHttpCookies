package nurisezgin.com.android.okhttpcookies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import nurisezgin.com.android.okhttpcookies.instestutils.InsTestUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static nurisezgin.com.android.okhttpcookies.instestutils.InsTestUtils.*;
import static nurisezgin.com.android.okhttpcookies.matcher.CustomMatchers.same;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by nuri on 25.07.2018
 */
@RunWith(AndroidJUnit4.class)
public class CookieInterceptorInstrumentedTest {

    private static final String PATH = "/";

    @Rule
    public MockWebServer mockWebServer = new MockWebServer();

    private CookieInterceptor cookieInterceptor;
    private Context ctx;

    @Before
    public void setUp_() {
        this.ctx = InstrumentationRegistry.getTargetContext();
        this.cookieInterceptor = new CookieInterceptorBuilder(ctx).build();
        clearSharedPref(ctx);
    }

    @After
    public void tearDown_() throws IOException {
        mockWebServer.shutdown();
        mockWebServer.close();
        clearSharedPref(ctx);
    }

    @Test
    public void should_SaveCookieCorrectCorrect() throws IOException {
        final String cookie = "cookieName1=cookieValue1";
        final HashSet<String> expected = newCookieSetWith(cookie);

        MockResponse mockResponse = newMockResponse(cookie);
        mockWebServer.enqueue(mockResponse);

        makeRequest();

        HashSet<String> actual = getStringSetFromSharedPref(ctx);

        assertThat(actual, is(same(expected)));
    }

    @Test
    public void should_RestoreCookieCorrect() throws IOException, InterruptedException {
        final String cookie = "cookieName1=cookieValue1";
        putStringSetToSharedPref(ctx, newCookieSetWith(cookie));

        final BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String cookie = request.getHeader(CookieInterceptor.getHEADER_COOKIE_TAG());
                queue.offer(cookie);

                return newMockResponse(cookie);
            }
        });

        makeRequest();

        String actualCookie = queue.poll(2, TimeUnit.SECONDS);

        assertThat(actualCookie, is(equalTo(cookie)));
    }

    private void makeRequest() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(cookieInterceptor)
                .build();

        Request request = new Request.Builder()
                .url(mockWebServer.url(PATH))
                .build();

        client.newCall(request)
                .execute();
    }

    private MockResponse newMockResponse(String... cookies) {
        MockResponse mockResponse = new MockResponse();

        for (String cookie : cookies) {
            mockResponse.addHeader(CookieInterceptor.getSET_COOKIE_TAG(), cookie)
                    .setResponseCode(200)
                    .setBody("body");
        }

        return mockResponse;
    }

    private HashSet<String> newCookieSetWith(String cookie) {
        HashSet<String> cookies = new HashSet<>();
        cookies.add(cookie);
        return cookies;
    }

}
