package iweinzierl.github.com.moviedatabase.rest.client;

import java.util.List;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface SearchClient {

    @GET("/api/movie")
    Call<List<Movie>> search(@Query("title") String title);

    @GET("/api/movie/{id}")
    Call<Movie> get(@Path("id") String movieId);
}
