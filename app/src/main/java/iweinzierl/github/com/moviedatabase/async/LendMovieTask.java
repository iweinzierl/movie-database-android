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

public class LendMovieTask extends AsyncTask<LentMovieInfo, Void, LentMovieInfo> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(LendMovieTask.class.getName(), "[async]");

    private final Context context;

    public LendMovieTask(Context context) {
        this.context = context;
    }

    @Override
    protected LentMovieInfo doInBackground(LentMovieInfo... lentMovieInfo) {
        BackendClient backendClient = ClientFactory.createBackendClient(context);
        Call<LentMovieInfo> saveCall = backendClient.lendMovie(lentMovieInfo[0]);

        try {
            return saveCall.execute().body();
        } catch (final IOException e) {
            LOG.error("Error while lending movie '{}' to '{}'",
                    lentMovieInfo[0].getMovieId(), lentMovieInfo[0].getPerson(), e);

            Looper mainLooper = Looper.getMainLooper();
            new Handler(mainLooper).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            context,
                            context.getString(R.string.task_lend_movie_error_message, e.getMessage()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        return null;
    }
}
