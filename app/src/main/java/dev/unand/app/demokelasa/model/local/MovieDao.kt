package dev.unand.app.demokelasa.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.unand.app.demokelasa.model.Movie

@Dao
interface MovieDao {

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Insert
    fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

}