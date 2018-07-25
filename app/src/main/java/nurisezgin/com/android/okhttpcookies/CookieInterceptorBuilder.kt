package nurisezgin.com.android.okhttpcookies

import android.content.Context
import android.content.SharedPreferences

/**
 *  Created by nuri on 25.07.2018
 */
class CookieInterceptorBuilder(private val context: Context) {

    companion object {
        @JvmStatic
        val PREF_NAME = "cookieStore"
    }

    fun build(): CookieInterceptor {
        val store = object: CookieStore {
            override fun put(hostKey: String, cookies: HashSet<String>) {
                getSharedPreferences().edit().putStringSet(hostKey, cookies).apply()
            }

            override fun get(hostKey: String): HashSet<String> {
                return getSharedPreferences().getStringSet(hostKey, HashSet<String>()).toHashSet()
            }
        }

        val manager = CookieManagerImp(store)

        return CookieInterceptor(manager)
    }

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

}