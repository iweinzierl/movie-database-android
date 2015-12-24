package iweinzierl.github.com.moviedatabase.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.ClientFactory;
import iweinzierl.github.com.moviedatabase.rest.client.SearchClient;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import retrofit.Call;

public class GetSearchMovieTask extends AsyncTask<String, Void, Movie> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(
            GetSearchMovieTask.class.getName(), "[async]");

    private final Context context;

    public GetSearchMovieTask(Context context) {
        this.context = context;
    }

    @Override
    protected Movie doInBackground(String... movieId) {
        SearchClient searchClient = ClientFactory.createSearchClient(context);
        Call<Movie> searchCall = searchClient.get(movieId[0]);

        try {
            return searchCall.execute().body();
        } catch (IOException e) {
            LOG.error("Error while search for movie: {}", movieId[0], e);
            Toast.makeText(
                    context,
                    context.getString(R.string.task_get_search_movie_error_message, e.getMessage()),
                    Toast.LENGTH_SHORT)
                    .show();
        }

        return null;
    }
}
