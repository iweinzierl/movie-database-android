package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;

import iweinzierl.github.com.moviedatabase.async.GetSearchMovieTask;
import iweinzierl.github.com.moviedatabase.fragment.MovieDetailFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieDetailActivity extends BaseActivity {

    public static final String EXTRA_MOVIE_ID = "moviedetailactivity.extra.movieid";

    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieDetailFragment = new MovieDetailFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, movieDetailFragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        new GetSearchMovieTask(this) {
            @Override
            protected void onPostExecute(final Movie movie) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieDetailFragment.setMovie(movie);
                    }
                });
            }
        }.execute(getMovieId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    private String getMovieId() {
        Intent intent = getIntent();
        return intent.getStringExtra(EXTRA_MOVIE_ID);
    }
}
