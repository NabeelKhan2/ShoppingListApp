package com.example.shoppinglistapp.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglistapp.data.local.ShoppingItemDatabase
import com.example.shoppinglistapp.data.remote.Api
import com.example.shoppinglistapp.data.repository.DefaultShoppingRepo
import com.example.shoppinglistapp.data.repository.ShoppingRepo
import com.example.shoppinglistapp.utils.Constants.BASE_URL
import com.example.shoppinglistapp.utils.Constants.DATABASE_NAME
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ShoppingItemDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideYourDao(db: ShoppingItemDatabase) =
        db.shoppingDao() // The reason we can implement a Dao for the database

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()

    }


    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): Api {
        return retrofit.create(Api::class.java)
    }

    @Singleton
    @Provides
    fun getMyRepository(defaultShoppingRepo: DefaultShoppingRepo): ShoppingRepo =
        defaultShoppingRepo


}

