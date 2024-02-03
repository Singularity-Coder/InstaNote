package com.singularitycoder.instanote.helpers.di

import android.content.Context
import androidx.room.Room
import com.singularitycoder.instanote.BuildConfig
import com.singularitycoder.instanote.helpers.api.ImageSearchService
import com.singularitycoder.instanote.imageselection.view.ImageSelectionAdapter
import com.singularitycoder.instanote.notes.view.NotesAdapter
import com.singularitycoder.instanote.helpers.Db
import com.singularitycoder.instanote.notes.viewmodel.NotesRepository
import com.singularitycoder.instanote.helpers.db.NoteDao
import com.singularitycoder.instanote.helpers.db.NoteDatabase
import com.singularitycoder.instanote.imageselection.viewmodel.ImageSelectionRepository
import com.singularitycoder.instanote.notes.viewmodel.NotesRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, Db.DB_NOTES).build()
    }

    @Singleton
    @Provides
    fun injectDao(db: NoteDatabase) = db.noteDao()

    @Singleton
    @Provides
    fun injectRetrofitApi(): ImageSearchService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.PIXABAY_BASE_URL).build().create(ImageSearchService::class.java)
    }

    @Singleton
    @Provides
    fun injectNotesRepo(dao: NoteDao) = NotesRepository(noteDao = dao) as NotesRepositoryInterface

    @Singleton
    @Provides
    fun injectImageSelectionRepo(service: ImageSearchService) = ImageSelectionRepository(imageSearchService = service)

    @Singleton
    @Provides
    fun injectImageSelectionAdapter(): ImageSelectionAdapter = ImageSelectionAdapter()

    @Singleton
    @Provides
    fun injectNotesAdapter(): NotesAdapter = NotesAdapter()
}