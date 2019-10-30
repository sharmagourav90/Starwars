package com.coder.starwars.di.module

import android.app.Application
import com.coder.starwars.BuildConfig
import com.coder.starwars.util.Constants.BASE_URL
import com.coder.starwars.data.network.ConnectivityInterceptor
import com.coder.starwars.data.network.StarWarsApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module for providing network dependencies
 * API, Retrofit, Interceptor, Adapters
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesStarWarsApi(retrofit: Retrofit): StarWarsApi {
        return retrofit.create(StarWarsApi::class.java);
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesRxJava2CallAdapterFactory() : RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(connectivityInterceptor: ConnectivityInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        return client.build()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesConnectivityInterceptor(context : Application) : ConnectivityInterceptor {
        return ConnectivityInterceptor(context)
    }
}