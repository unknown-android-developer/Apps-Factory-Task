package com.test.appsfactorytask.core.di.module.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.test.appsfactorytask.core.api.webservice.WebService
import com.test.appsfactorytask.core.api.webservice.response.TracksResponse
import com.test.appsfactorytask.core.api.webservice.typeadapter.TracksResponseTypeAdapter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object WebServiceModule {

    private const val BASE_URL = "http://ws.audioscrobbler.com/2.0/"

    private const val API_KEY = "ffff60869e5a9f9cfdf30d5e0e50d1e2"

    private const val NAME_API_KEY = "api_key"

    private const val NAME_FORMAT = "format"

    private const val FORMAT_JSON = "json"

    @Singleton
    @Provides
    fun provideOkkHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor {
            val url = it
                .request()
                .url
                .newBuilder()
                .addQueryParameter(NAME_API_KEY, API_KEY)
                .addQueryParameter(NAME_FORMAT, FORMAT_JSON)
                .build()
            val request = it
                .request()
                .newBuilder()
                .url(url)
                .build()
            it.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY })
        .build()

    @Singleton
    @Provides
    fun provideWebService(okHttpClient: OkHttpClient, gson: Gson): WebService {
        val retrofit = Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideGson(typedAdapter: TypeAdapter<TracksResponse>): Gson = GsonBuilder()
        .apply {
            registerTypeAdapter(TracksResponse::class.java, typedAdapter)
        }.create()

    // API call "album.getinfo" returns track as an object when there is only one track instead of array
    // to avoid crashes it has to be parsed manually
    @Singleton
    @Provides
    fun provideTypeAdapter(): TypeAdapter<TracksResponse> = TracksResponseTypeAdapter()
}