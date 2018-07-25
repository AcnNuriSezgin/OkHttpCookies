package nurisezgin.com.android.okhttpcookies

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by nuri on 24.07.2018
 */
class CookieUtilTest {

    @Test
    fun should_ToHostKeyCorrect() {
        val expected = "www.google.com_cookies"
        val hostName = "www.google.com"

        val actual = CookieUtil.toHostKey(hostName)

        assertThat(actual, `is`(equalTo(expected)))
    }

    @Test
    fun should_ToHostKeyWithNullCorrect() {
        val expected = "_cookies"

        val actual = CookieUtil.toHostKey(null)

        assertThat(actual, `is`(equalTo(expected)))
    }

    @Test
    fun should_ToCookieNameCorrect() {
        val expected = "sessionToken"
        val cookie = "sessionToken=abc123; Expires=Wed, 09 Jun 2021 10:18:14 GMT"

        val actual = CookieUtil.toCookieName(cookie)

        assertThat(actual, `is`(equalTo(expected)))
    }

    @Test
    fun should_ToCookieNamesSizeCorrect() {
        val expected = 3

        val cookies = getCookies(expected, null)
        val actual = CookieUtil.toCookieNames(cookies).size

        assertThat(actual, `is`(expected))
    }

    @Test
    fun should_ToCookieNamesWithNullCorrect() {
        val names = CookieUtil.toCookieNames(null)

        assertThat(names.isEmpty(), `is`(true))
    }

    @Test
    fun should_RemoveExpiredCookiesCorrect() {
        val expected = 0

        val cookies = getCookies(3, "cookie1")
        val oldCookies = HashSet<String>(getCookies(2, "cookie1"))

        CookieUtil.removeExpiredCookies(cookies.toMutableList(), oldCookies)

        val actual = oldCookies.size

        assertThat(actual, `is`(expected))
    }

    @Test
    fun should_RemoveDuplicatedCookiesCorrect() {
        val expected = 4

        val cookies = ArrayList<String>()
        cookies.add("cookie5")
        cookies.add("cookie4")
        cookies.add("cookie3")
        cookies.add("cookie3")
        cookies.add("cookie1")
        cookies.add("cookie4")

        val actual = CookieUtil.removeDuplicatedCookies(cookies).size

        assertThat(actual, `is`(expected))
    }

    private fun getCookies(num: Int, nameTag: String?): List<String> {
        val cookies = ArrayList<String>()

        for (i in 1..num) {
            val name = "name" + (nameTag ?: nameTag) + "$i"
            cookies.add("$name=value")
        }

        return cookies
    }
}