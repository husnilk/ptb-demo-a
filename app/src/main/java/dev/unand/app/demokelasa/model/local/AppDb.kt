package dev.unand.app.demokelasa.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.unand.app.demokelasa.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}