package dev.unand.app.demokelasa

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.unand.app.demokelasa.model.UpcomingResponse
import dev.unand.app.demokelasa.model.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel: ViewModel() {

    private val _upcomingsData : MutableStateFlow<UpcomingResponse> = MutableStateFlow(UpcomingResponse())
    val upcomingsData : StateFlow<UpcomingResponse> = _upcomingsData

    init {
        getUpcomingMovies()
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch() {
            val call: Call<UpcomingResponse> = RetrofitInstance.tmdbService.getUpcomingMovies("apikeydisini")

            call.enqueue(object : Callback<UpcomingResponse> {

                override fun onResponse(call: Call<UpcomingResponse>, response: Response<UpcomingResponse>) {
                    if (response.isSuccessful) {
                        _upcomingsData.value = response.body() ?: UpcomingResponse()
                    }
                }

                override fun onFailure(call: Call<UpcomingResponse>, t: Throwable) {
                    Log.d("UpcomingViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }

}