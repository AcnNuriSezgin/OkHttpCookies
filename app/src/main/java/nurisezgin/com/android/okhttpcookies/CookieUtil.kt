package nurisezgin.com.android.okhttpcookies

import java.util.HashSet


/**
 * Created by nuri on 24.07.2018
 */
object CookieUtil {

    @JvmStatic val COOKIES_KEY = "_cookies"
    private const val COOKIE_NAME_SEPARATOR = "="

    fun toHostKey(host: String?):String {
        return (host ?: "") + COOKIES_KEY
    }

    fun toCookieName(cookie: String?):String {
        return (cookie ?: "").split(COOKIE_NAME_SEPARATOR.toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]
    }

    fun toCookieNames(cookies: List<String>?):List<String> {
        val names = ArrayList<String>()

        if (cookies == null) {
            return names
        }

        for (cookie in cookies) {
            if (cookie != null && cookie.contains(COOKIE_NAME_SEPARATOR)) {
                names.add(toCookieName(cookie))
            }
        }

        return names
    }

    fun removeExpiredCookies(cookies: MutableList<String>?, oldCookies: HashSet<String>?) {
        if (cookies == null || cookies.isEmpty()) {
            return
        }

        if (oldCookies == null || oldCookies.isEmpty()) {
            return
        }

        val newCookieNames = toCookieNames(cookies)
        val it = oldCookies.iterator()

        while (it.hasNext()) {
            val cookie = it.next()
            val name = toCookieName(cookie)

            if (newCookieNames.contains(name)) {
                it.remove()
            }
        }
    }

    fun removeDuplicatedCookies(cookies: List<String>): MutableList<String> {
        val cookieMap = HashMap<String, String>()
        for (cookie in cookies) {
            val name = toCookieName(cookie)
            cookieMap[name] = cookie
        }

        val result = ArrayList<String>()
        for (value in cookieMap.values) {
            result.add(value)
        }

        return result
    }
}

