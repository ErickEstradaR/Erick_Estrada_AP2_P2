package com.example.erick_estrada_ap2_p2.di

import com.example.erick_estrada_ap2_p2.data.remote.GastosApiService
import com.example.erick_estrada_ap2_p2.data.repository.GastosRepositoryImpl
import com.example.erick_estrada_ap2_p2.domain.repository.GastosRepository
import com.example.erick_estrada_ap2_p2.domain.useCases.GastosUseCases
import com.example.erick_estrada_ap2_p2.domain.useCases.deleteGastoUseCase
import com.example.erick_estrada_ap2_p2.domain.useCases.getGastoUseCase
import com.example.erick_estrada_ap2_p2.domain.useCases.getGastosUseCase
import com.example.erick_estrada_ap2_p2.domain.useCases.saveGastoUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {
    const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideGastosApiService(moshi: Moshi): GastosApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GastosApiService::class.java)
    }

    @Provides
    fun provideGastoUseCases(repository: GastosRepository): GastosUseCases {
        return GastosUseCases(
            save = saveGastoUseCase(repository),
            delete = deleteGastoUseCase(repository),
            obtenerGastos =getGastosUseCase(repository),
            obtenerGastoPorId= getGastoUseCase(repository)
        )
    }


    @Module
    @InstallIn(SingletonComponent::class)

    abstract class RepositoryModule {

        @Binds
        @Singleton
        abstract fun bindGastoRepository(
            impl: GastosRepositoryImpl
        ): GastosRepository
}}