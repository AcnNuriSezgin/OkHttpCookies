package nurisezgin.com.android.okhttpcookies

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by nuri on 24.07.2018
 */
class CookieInterceptor(private val cookieManager: CookieManager) : Interceptor {

    companion object {
        @JvmStatic
        val HEADER_COOKIE_TAG = "Cookie"

        @JvmStatic
        val SET_COOKIE_TAG = "Set-Cookie"
    }

    override fun intercept(chain: Interceptor.Chain?): Response {
        if (chain != null) {
            val originalRequest = chain.request()
            val host = originalRequest.url().host()

            val customizedRequest = handleRequest(host, originalRequest)

            val response = chain.proceed(customizedRequest)
            handleResponse(host, response)

            return response
        }

        throw IllegalArgumentException()
    }

    private fun handleRequest(host: String, request: Request): Request {
        val requestBuilder = request.newBuilder()
        val cookies: HashSet<String> = cookieManager.provideCookies(host)
        for (cookie in cookies) {
            requestBuilder.addHeader(HEADER_COOKIE_TAG, cookie)
        }

        return requestBuilder.build()
    }

    private fun handleResponse(host: String, response: Response) {
        val cookies = response.headers(SET_COOKIE_TAG)
        if (cookies != null && !cookies.isEmpty()) {
            cookieManager.onHandleCookies(host, cookies)
        }

    }
}
