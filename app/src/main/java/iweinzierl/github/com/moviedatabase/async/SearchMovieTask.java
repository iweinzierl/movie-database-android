package iweinzierl.github.com.moviedatabase.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.ClientFactory;
import iweinzierl.github.com.moviedatabase.rest.client.SearchClient;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import retrofit.Call;

public class SearchMovieTask extends AsyncTask<String, Void, List<Movie>> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(SearchMovieTask.class.getName(), "[async]");

    private final Context context;

    public SearchMovieTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Movie> doInBackground(String... title) {
        SearchClient searchClient = ClientFactory.createSearchClient(context);
        Call<List<Movie>> searchCall = searchClient.search(title[0]);

        try {
            return searchCall.execute().body();
        } catch (IOException e) {
            LOG.error("Error while search for movie: {}", title[0], e);
            Toast.makeText(
                    context,
                    context.getString(R.string.task_search_movie_error_message, e.getMessage()),
                    Toast.LENGTH_SHORT)
                    .show();
        }

        return null;
    }
}
