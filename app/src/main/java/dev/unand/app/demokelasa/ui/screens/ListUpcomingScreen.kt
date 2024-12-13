@file:OptIn(ExperimentalMaterial3Api::class)

package dev.unand.app.demokelasa.ui.screens

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import coil3.compose.rememberAsyncImagePainter
import dev.unand.app.demokelasa.UpcomingViewModel
import dev.unand.app.demokelasa.model.Movie
import dev.unand.app.demokelasa.model.local.AppDb
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListUpcomingScreen(viewModel: UpcomingViewModel, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Upcoming Movies")
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("ListUpcomingScreen", "FloatingActionButton clicked")
                showNotification(context)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        UpcomingMovieList(viewModel, modifier)
    }
}

@Composable
fun MovieCard(movie: Movie?, modifier: Modifier = Modifier) {
    val photoUrl = "https://image.tmdb.org/t/p/w500/${movie?.posterPath}"
    Column() {
        Image(
            painter = rememberAsyncImagePainter(photoUrl),
            contentDescription = null,
        )
        Text(text = movie?.title ?: "Unknown", modifier = modifier)
        Text(text = movie?.overview ?: "--", modifier = modifier)
    }
}

@Composable
fun UpcomingMovieList(viewModel: UpcomingViewModel, modifier: Modifier) {

    val upcomingsMovie = viewModel.upcomingsData.collectAsState().value

    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDb::class.java, "movies.db"
    ).allowMainThreadQueries()
        .build()

    val movieDao = db.movieDao()

    movieDao.deleteAllMovies()
    for (movie in upcomingsMovie.results ?: emptyList()) {
        if (movie != null) {
            movieDao.insertMovie(movie)
        }
    }

    val listMovie: List<Movie> = movieDao.getAllMovies()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(count = listMovie.size) { it ->
            MovieCard(listMovie[it], modifier = modifier)
        }
    }
}

fun showNotification(ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val mChannel =
            NotificationChannel("ChannelId", "New Movie", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager =
            getSystemService(ctx, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    Log.d("ListUpcomingScreen", "showNotification")

    var builder = NotificationCompat.Builder(ctx, "ChannelId")
        .setSmallIcon(R.drawable.btn_star)
        .setContentTitle("New Movie Coming Out")
        .setContentText("Check out the new movie that just released")
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    with(NotificationManagerCompat.from(ctx)) {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                android.Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // notificationId is a unique int for each notification that you must define.
        notify(Random.nextInt(), builder.build())
    }
}