package nurisezgin.com.android.okhttpcookies

/**
 *  Created by nuri on 24.07.2018
 */
interface CookieStore {

    fun put(hostKey: String, cookies: HashSet<String>)

    fun get(hostKey: String): HashSet<String>

}