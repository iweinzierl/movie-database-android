package iweinzierl.github.com.moviedatabase.rest.client;

import java.util.List;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface BackendClient {

    @POST("/api/movie")
    Movie save(@Body Movie movie);

    @GET("/api/movie")
    Call<List<Movie>> list();

    @GET("/api/movie/{id}")
    Call<Movie> get(@Path("id") String id);

    @DELETE("/api/movie/{id}")
    Call<Movie> delete(@Path("id") String id);
}
