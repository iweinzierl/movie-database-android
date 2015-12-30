package iweinzierl.github.com.moviedatabase.rest.client;

import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.rest.domain.LentMovieInfo;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.rest.domain.Statistics;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public interface BackendClient {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/api/movie")
    Call<Movie> saveMovie(@Body Movie movie);

    @Headers({"Accept: application/json"})
    @GET("/api/movie")
    Call<List<Movie>> listMovies();

    @Headers({"Accept: application/json"})
    @GET("/api/movie/{id}")
    Call<Movie> getMovie(@Path("id") String id);

    @Headers({"Accept: application/json"})
    @DELETE("/api/movie/{id}")
    Call<Movie> deleteMovie(@Path("id") String id);

    @Headers({"Accept: application/json"})
    @GET("/api/statistics")
    Call<Statistics> getStatistics();

    @Headers({"Accept: application/json"})
    @GET("/api/genre")
    Call<Set<String>> listGenres();

    @Headers({"Accept: application/json"})
    @GET("/api/lent/{id}")
    Call<LentMovieInfo> getLentMovieInfo(@Path("id") String movieId);

    @Headers({"Accept: application/json"})
    @POST("/api/lent")
    Call<LentMovieInfo> lendMovie(@Body LentMovieInfo lentMovieInfo);

    @Headers({"Accept: application/json"})
    @DELETE("/api/lent/{id}")
    Call<LentMovieInfo> movieReturned(@Path("id") String movieId);

    @Headers({"Accept: application/json"})
    @GET("/api/person")
    Call<List<String>> listPeople();
}
