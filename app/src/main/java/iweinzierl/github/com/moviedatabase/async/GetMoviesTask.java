package iweinzierl.github.com.moviedatabase.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.ClientFactory;
import iweinzierl.github.com.moviedatabase.rest.client.BackendClient;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import retrofit.Call;

public class GetMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(GetMoviesTask.class.getName(), "[async]");

    private final Context context;

    public GetMoviesTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        BackendClient backendClient = ClientFactory.createBackendClient(context);
        Call<List<Movie>> saveCall = backendClient.listMovies();

        try {
            return saveCall.execute().body();
        } catch (final IOException e) {
            LOG.error("Error while fetching movies", e);
            Looper mainLooper = Looper.getMainLooper();
            new Handler(mainLooper).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            context,
                            context.getString(R.string.task_get_movies_error_message, e.getMessage()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        return null;
    }
}
