package iweinzierl.github.com.moviedatabase.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.github.iweinzierl.android.utils.UiUtils;

import org.slf4j.Logger;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.Statistics;

public class StatisticsFragment extends Fragment {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(StatisticsFragment.class.getName());

    private TextView numberOfMovies;
    private TextView numberOfGenres;
    private TextView numberOfLentMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        numberOfMovies = UiUtils.getGeneric(TextView.class, view, R.id.number_of_movies);
        numberOfGenres = UiUtils.getGeneric(TextView.class, view, R.id.number_of_genres);
        numberOfLentMovies = UiUtils.getGeneric(TextView.class, view, R.id.number_of_lent_movies);

        return view;
    }

    public void setStatistics(Statistics statistics) {
        if (statistics != null) {
            numberOfMovies.setText(String.valueOf(statistics.getNumberOfMovies()));
            numberOfGenres.setText(String.valueOf(statistics.getNumberOfGenres()));
            numberOfLentMovies.setText(String.valueOf(statistics.getNumberOfLentMovies()));
        } else {
            LOG.warn("Called setStatistics() with 'null'");
        }
    }
}
