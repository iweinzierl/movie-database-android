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
import iweinzierl.github.com.moviedatabase.rest.domain.LentMovieInfo;
import retrofit.Call;

public class DeleteLentMovieInfoTask extends AsyncTask<String, Void, LentMovieInfo> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(DeleteLentMovieInfoTask.class.getName(), "[async]");

    private final Context context;

    public DeleteLentMovieInfoTask(Context context) {
        this.context = context;
    }

    @Override
    protected LentMovieInfo doInBackground(String... movieId) {
        BackendClient backendClient = ClientFactory.createBackendClient(context);
        Call<LentMovieInfo> saveCall = backendClient.movieReturned(movieId[0]);

        try {
            return saveCall.execute().body();
        } catch (final IOException e) {
            LOG.error("Error while deleting lent movie info: '{}'", movieId[0], e);

            Looper mainLooper = Looper.getMainLooper();
            new Handler(mainLooper).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            context,
                            context.getString(R.string.task_lend_movie_delete_error_message, e.getMessage()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        return null;
    }
}
