package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.google.common.base.Strings;

import org.slf4j.Logger;

import java.util.Comparator;

import iweinzierl.github.com.moviedatabase.fragment.MovieComparatorChoiceFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieComparatorChoiceActivity extends AppCompatActivity {

    public static final int REQUEST_COMPARATOR = 200;

    public static final String EXTRA_SELECTED_COMPARATOR = "moviecomparatorchoice.extra.comparator";

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(MovieComparatorChoiceActivity.class.getName());

    private MovieComparatorChoiceFragment movieComparatorChoiceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_comparator_choice);

        movieComparatorChoiceFragment = new MovieComparatorChoiceFragment();

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbarTop);

        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComparator();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, movieComparatorChoiceFragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        applyInitialSelectedComparator();
    }

    private void submitComparator() {
        Class<? extends Comparator<Movie>> comparator = movieComparatorChoiceFragment.getSelectedComparator();

        Intent data = new Intent();
        data.putExtra(EXTRA_SELECTED_COMPARATOR, comparator.getName());

        setResult(RESULT_OK, data);
        finish();
    }

    @SuppressWarnings("unchecked")
    private void applyInitialSelectedComparator() {
        Intent intent = getIntent();
        String comparatorClassName = intent.getStringExtra(EXTRA_SELECTED_COMPARATOR);

        if (Strings.isNullOrEmpty(comparatorClassName)) {
            return;
        }

        try {
            Class<? extends Comparator<Movie>> comparatorClass = (Class<? extends Comparator<Movie>>) Class.forName(comparatorClassName);
            movieComparatorChoiceFragment.setSelectedComparator(comparatorClass);
        } catch (ClassNotFoundException e) {
            LOG.error("Comparator class not found: {}", comparatorClassName, e);
        }
    }
}
