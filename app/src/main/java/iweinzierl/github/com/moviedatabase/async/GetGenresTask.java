package iweinzierl.github.com.moviedatabase.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.ClientFactory;
import iweinzierl.github.com.moviedatabase.rest.client.BackendClient;
import retrofit.Call;

public class GetGenresTask extends AsyncTask<Void, Void, Set<String>> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(GetGenresTask.class.getName(), "[async]");

    private final Context context;

    public GetGenresTask(Context context) {
        this.context = context;
    }

    @Override
    protected Set<String> doInBackground(Void... params) {
        BackendClient backendClient = ClientFactory.createBackendClient(context);
        Call<Set<String>> saveCall = backendClient.listGenres();

        try {
            return saveCall.execute().body();
        } catch (final IOException e) {
            LOG.error("Error while fetching genres", e);
            Looper mainLooper = Looper.getMainLooper();
            new Handler(mainLooper).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            context,
                            context.getString(R.string.task_get_genres_error_message, e.getMessage()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        return null;
    }
}
