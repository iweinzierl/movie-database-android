package iweinzierl.github.com.moviedatabase.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.ClientFactory;
import iweinzierl.github.com.moviedatabase.rest.client.BackendClient;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import retrofit.Call;

public class DeleteMovieTask extends AsyncTask<String, Void, Movie> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(DeleteMovieTask.class.getName(), "[async]");

    private final Context context;

    public DeleteMovieTask(Context context) {
        this.context = context;
    }

    @Override
    protected Movie doInBackground(String... movieId) {
        BackendClient backendClient = ClientFactory.createBackendClient(context);
        Call<Movie> deleteCall = backendClient.delete(movieId[0]);

        try {
            return deleteCall.execute().body();
        } catch (final IOException e) {
            LOG.error("Error while deleting movie: {}", movieId[0], e);
            Looper mainLooper = Looper.getMainLooper();
            new Handler(mainLooper).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            context,
                            context.getString(R.string.task_delete_movie_error_message, e.getMessage()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        return null;
    }
}
