package com.coder.starwars.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.coder.starwars.util.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Connectivity interceptor for network failure case
 */
class ConnectivityInterceptor (context: Context
) : Interceptor{
    private val appContext = context

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline())
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}