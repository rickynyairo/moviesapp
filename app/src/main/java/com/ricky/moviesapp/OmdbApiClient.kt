import com.ricky.moviesapp.Movie
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class MovieSearchResult(val Search: List<Movie>, val totalResults: Int)

interface OmdbService {
    @GET("/?apikey=64623f9c&")
    suspend fun searchMovies(@Query("s") query: String): MovieSearchResult
}

class OmdbApiClient {
    private val apiService: OmdbService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val omdbApi = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = omdbApi.create(OmdbService::class.java)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        var result = apiService.searchMovies(query)
        return result.Search
    }
}
