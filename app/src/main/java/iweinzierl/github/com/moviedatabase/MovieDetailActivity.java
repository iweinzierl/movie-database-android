package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.base.Strings;

import iweinzierl.github.com.moviedatabase.async.DeleteMovieTask;
import iweinzierl.github.com.moviedatabase.async.GetMovieTask;
import iweinzierl.github.com.moviedatabase.fragment.MovieDetailFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieDetailActivity extends BaseActivity {

    public static final String EXTRA_MOVIE_ID = "moviedetailactivity.extra.movieid";

    protected MovieDetailFragment movieDetailFragment;

    protected MenuItem addMovieMenuItem;
    protected MenuItem removeMovieMenuItem;

    private Movie movie;

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
    protected void onStart() {
        super.onStart();
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_movie_detail, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        this.addMovieMenuItem = menu.findItem(R.id.add_to_collection);
        this.removeMovieMenuItem = menu.findItem(R.id.remove_from_collection);

        addMovieMenuItem.setVisible(false);
        removeMovieMenuItem.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_from_collection:
                removeMovieFromCollection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    protected String getMovieIdFromIntent() {
        Intent intent = getIntent();
        return intent.getStringExtra(EXTRA_MOVIE_ID);
    }

    protected Movie getMovie() {
        return movie;
    }

    protected void setMovie(Movie movie) {
        this.movie = movie;
        movieDetailFragment.setMovie(movie);

        setTitle(movie.getTitle());
        updateOptionsMenu();
    }

    protected void update() {
        startProgress(getString(R.string.moviedetail_progress_load_movie));

        new GetMovieTask(this) {
            @Override
            protected void onPostExecute(Movie movie) {
                setMovie(movie);
                stopProgress();
            }
        }.execute(getMovieIdFromIntent());
    }

    private void updateOptionsMenu() {
        Movie movie = getMovie();

        if (movie != null && !Strings.isNullOrEmpty(movie.getId())) {
            if (addMovieMenuItem != null) {
                addMovieMenuItem.setVisible(false);
            }

            if (removeMovieMenuItem != null) {
                removeMovieMenuItem.setVisible(true);
            }
        }
    }

    private void removeMovieFromCollection() {
        startProgress(getString(R.string.moviedetail_progress_remove_movie_from_collection));

        new DeleteMovieTask(this) {
            @Override
            protected void onPostExecute(final Movie movie) {
                super.onPostExecute(movie);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setMovie(movie);

                        stopProgress();
                        notifySuccessfullDeletion();
                    }
                });
            }
        }.execute(getMovie().getId());
        finish();
    }

    private void notifySuccessfullDeletion() {
        Toast.makeText(
                MovieDetailActivity.this,
                getString(R.string.moviedetail_delete_movie_successful, getMovie().getTitle()),
                Toast.LENGTH_SHORT)
                .show();
    }
}
