package iweinzierl.github.com.moviedatabase;

import android.os.Bundle;

import iweinzierl.github.com.moviedatabase.async.GetStatisticsTask;
import iweinzierl.github.com.moviedatabase.fragment.StatisticsFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Statistics;

public class DashboardActivity extends BaseActivity {

    private StatisticsFragment statisticsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statisticsFragment = new StatisticsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, statisticsFragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        new GetStatisticsTask(this) {
            @Override
            protected void onPostExecute(Statistics statistics) {
                setStatistics(statistics);
            }
        }.execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    private void setStatistics(Statistics statistics) {
        statisticsFragment.setStatistics(statistics);
    }
}
