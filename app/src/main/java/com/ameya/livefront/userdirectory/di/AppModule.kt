package com.ameya.livefront.userdirectory.di

import android.app.Application
import androidx.room.Room
import com.ameya.livefront.userdirectory.data.local.UserDatabase
import com.ameya.livefront.userdirectory.data.remote.UserAPI
import com.ameya.livefront.userdirectory.data.remote.UserAPI.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserAPi(): UserAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(application: Application): UserDatabase {
        return Room.databaseBuilder(
            context = application,
            UserDatabase::class.java,
            "user.db"
        ).build()
    }
}