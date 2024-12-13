package dev.unand.app.demokelasa.model.network

import dev.unand.app.demokelasa.model.UpcomingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey: String): Call<UpcomingResponse>

}