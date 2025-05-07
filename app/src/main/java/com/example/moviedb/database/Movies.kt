package com.example.moviedb.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CachedMovieEntity::class], version = 1)
abstract class Movies : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
