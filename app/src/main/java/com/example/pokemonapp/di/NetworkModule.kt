package com.example.pokemonapp.di

import com.example.pokemonapp.core.network.NetworkResultCallAdapterFactory
import com.example.pokemonapp.data.api.PokemonAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  fun provideGson(): Gson {
    return GsonBuilder().create()
  }

  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()
  }

  @Provides
  fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  fun provideApiService(retrofit: Retrofit): PokemonAPI = retrofit.create(PokemonAPI::class.java)

  private const val BASE_URL = "https://pokeapi.co/api/v2/"
}
