package nurisezgin.com.android.okhttpcookies

/**
 *  Created by nuri on 24.07.2018
 */
class CookieManagerImp(private val cookieStore: CookieStore): CookieManager {

    override fun onHandleCookies(host: String, cookies: List<String>) {
        val hostKey = CookieUtil.toHostKey(host)

        val cookies = CookieUtil.removeDuplicatedCookies(cookies.toMutableList())

        val oldCookies = this.cookieStore.get(hostKey)
        CookieUtil.removeExpiredCookies(cookies, oldCookies)

        val mergedCookies = HashSet<String>()
        mergedCookies.addAll(oldCookies)
        mergedCookies.addAll(cookies)

        cookieStore.put(hostKey, mergedCookies)
    }

    override fun provideCookies(host: String): HashSet<String> {
        val hostKey = CookieUtil.toHostKey(host)
        return cookieStore.get(hostKey)
    }

}