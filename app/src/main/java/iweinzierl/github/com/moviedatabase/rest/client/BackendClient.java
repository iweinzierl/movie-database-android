package iweinzierl.github.com.moviedatabase.rest.client;

import java.util.List;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
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
    Call<Movie> save(@Body Movie movie);

    @Headers({"Accept: application/json"})
    @GET("/api/movie")
    Call<List<Movie>> list();

    @Headers({"Accept: application/json"})
    @GET("/api/movie/{id}")
    Call<Movie> get(@Path("id") String id);

    @Headers({"Accept: application/json"})
    @DELETE("/api/movie/{id}")
    Call<Movie> delete(@Path("id") String id);
}
