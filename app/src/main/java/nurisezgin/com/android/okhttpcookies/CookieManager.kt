package nurisezgin.com.android.okhttpcookies

/**
 *  Created by nuri on 24.07.2018
 */
interface CookieManager {

    fun onHandleCookies(host: String, cookies: List<String>)

    fun provideCookies(host: String): HashSet<String>

}