package com.task.noteapp.helpers.di

import android.content.Context
import androidx.room.Room
import com.task.noteapp.helpers.api.ImageSearchService
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.task.noteapp.BuildConfig
import com.task.noteapp.R
import com.task.noteapp.imageselection.view.ImageSelectionAdapter
import com.task.noteapp.notes.view.NotesAdapter
import com.task.noteapp.helpers.Db
import com.task.noteapp.notes.viewmodel.NotesRepository
import com.task.noteapp.helpers.db.NoteDao
import com.task.noteapp.helpers.db.NoteDatabase
import com.task.noteapp.imageselection.viewmodel.ImageSelectionRepository
import com.task.noteapp.notes.viewmodel.NotesRepositoryInterface
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
    fun injectImageSelectionAdapter(glideRequestManager: RequestManager): ImageSelectionAdapter = ImageSelectionAdapter(glideRequestManager)

    @Singleton
    @Provides
    fun injectNotesAdapter(glideRequestManager: RequestManager): NotesAdapter = NotesAdapter(glideRequestManager)

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context): RequestManager {
        return Glide.with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.color.purple_200).error(android.R.color.holo_red_dark)
        )
    }
}