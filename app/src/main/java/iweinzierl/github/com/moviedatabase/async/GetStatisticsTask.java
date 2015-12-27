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
import iweinzierl.github.com.moviedatabase.rest.domain.Statistics;
import retrofit.Call;

public class GetStatisticsTask extends AsyncTask<Void, Void, Statistics> {

    private static final Logger LOG = AndroidLoggerFactory.getInstance()
            .getLogger(GetStatisticsTask.class.getName(), "[async]");

    private final Context context;

    public GetStatisticsTask(Context context) {
        this.context = context;
    }

    @Override
    protected Statistics doInBackground(Void... params) {
        BackendClient backendClient = ClientFactory.createBackendClient(context);
        Call<Statistics> statisticsCall = backendClient.getStatistics();

        try {
            return statisticsCall.execute().body();
        } catch (final IOException e) {
            LOG.error("Error while fetching statistics", e);
            Looper mainLooper = Looper.getMainLooper();
            new Handler(mainLooper).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            context,
                            context.getString(R.string.task_get_statistics_error_message, e.getMessage()),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        return null;
    }
}
