package nurisezgin.com.android.okhttpcookies;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by nuri on 25.07.2018
 */
@RunWith(MockitoJUnitRunner.class)
public class CookieInterceptorTest {

    private static final String PATH = "/";

    @Rule
    public MockWebServer mockWebServer = new MockWebServer();

    private CookieInterceptor cookieInterceptor;

    @Mock
    CookieStore cookieStore;

    @Before
    public void setUp_() {
        CookieManager cookieManager = new CookieManagerImp(cookieStore);
        cookieInterceptor = new CookieInterceptor(cookieManager);
    }

    @After
    public void tearDown_() throws IOException {
        mockWebServer.shutdown();
        mockWebServer.close();
    }

    @Test
    public void should_SaveCookieCorrect() throws IOException {
        final String cookie = "cookieName1=cookieValue1";
        final String cookie1 = "cookieName1=cookieValue2";

        MockResponse mockResponse = newMockResponse(cookie, cookie1);
        mockWebServer.enqueue(mockResponse);

        makeRequest();

        HashSet<String> expectedCookies = newCookieSetWith(cookie1);

        verify(cookieStore, times(1))
                .put(anyString(), eq(expectedCookies));
    }

    @Test
    public void should_RestoreCookieCorrect() throws IOException, InterruptedException {
        final String cookie = "cookieName2=cookieValue2";
        HashSet<String> temporaryCookie = newCookieSetWith(cookie);

        when(cookieStore.get(anyString()))
                .thenReturn(temporaryCookie);

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

    @Test
    public void should_ResponseWithoutCookieCorrect() throws IOException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        makeRequest();

        verify(cookieStore, never())
                .put(anyString(), any(HashSet.class));
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
