package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL ="https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<Movie>()
    // get a ref for RecyclerView
    private lateinit var rvMovie : RecyclerView

//    1. define a data model class as the data source
//    2. add the RecyclerView to the layout
//    3. create a custom row layout XML file to visualize the item
//    4. create adapter and ViewHolder to render the item
//    5. bind the adapter to the data source to populate the RecyclerView
//    6. bind a layout manager to the RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovie = findViewById(R.id.rvMovie)

        val movieAdapter = MovieAdapter(this, movies)
        // this refers to MainActivity, which is the example of context; movies is the data source
        // bind the movie adapter to the RecyclerView
        rvMovie.adapter = movieAdapter
        rvMovie.layoutManager = LinearLayoutManager(this)

        // create a new obj for http client
        val client = AsyncHttpClient()
        // 2nd para: respond handler (the type of it depends on the data being returned by API). Here is an anonymous class
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                try {
                    val movieJasonArray = json.jsonObject.getJSONArray("results")
                    // pass in a Movie function
                    movies.addAll(Movie.fromJsonArray(movieJasonArray))
                    movieAdapter.notifyDataSetChanged()
                    Log.i(TAG, "Movie list $movies")
                }catch (e: JSONException){
                    Log.e(TAG, "Encounter exception $e")
                }
            }

        })
    }
}