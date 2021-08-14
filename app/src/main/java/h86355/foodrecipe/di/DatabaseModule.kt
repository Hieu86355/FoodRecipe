package h86355.foodrecipe.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import h86355.foodrecipe.data.local.RecipeDatabase
import h86355.foodrecipe.utils.Constants.Companion.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room
            .databaseBuilder(
                context,
                RecipeDatabase::class.java,
                DATABASE_NAME
            ).build()

    @Singleton
    @Provides
    fun prodiveDao(database: RecipeDatabase) = database.recipesDao()
}