package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.base.Strings;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import iweinzierl.github.com.moviedatabase.async.DeleteLentMovieInfoTask;
import iweinzierl.github.com.moviedatabase.async.DeleteMovieTask;
import iweinzierl.github.com.moviedatabase.async.GetLentMovieInfoTask;
import iweinzierl.github.com.moviedatabase.async.GetMovieTask;
import iweinzierl.github.com.moviedatabase.async.GetPeopleTask;
import iweinzierl.github.com.moviedatabase.async.LendMovieTask;
import iweinzierl.github.com.moviedatabase.fragment.MovieDetailFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.LentMovieInfo;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.util.PeopleNameSelectionDialog;

public class MovieDetailActivity extends BaseActivity {

    public static final String EXTRA_MOVIE_ID = "moviedetailactivity.extra.movieid";

    protected MovieDetailFragment movieDetailFragment;

    protected MenuItem addMovieMenuItem;
    protected MenuItem removeMovieMenuItem;
    protected MenuItem lendMovieMenuItem;
    protected MenuItem movieReturnedMenuItem;

    private Movie movie;
    private LentMovieInfo lentMovieInfo;

    private AtomicInteger activeAsyncCalls = new AtomicInteger(0);

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
        this.lendMovieMenuItem = menu.findItem(R.id.lend_movie);
        this.movieReturnedMenuItem = menu.findItem(R.id.movie_returned);

        showAddMovieMenuItem(false);
        showRemoveMovieMenuItem(false);
        showLendMovieMenuItem(false);
        showReturnMovieMenuItem(false);

        updateOptionsMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_from_collection:
                removeMovieFromCollection();
                return true;
            case R.id.lend_movie:
                startLendMovieProcess();
                return true;
            case R.id.movie_returned:
                movieReturned();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    protected void startProgress(String message) {
        if (activeAsyncCalls.getAndIncrement() == 0) {
            super.startProgress(message);
        }
    }

    @Override
    protected void stopProgress() {
        if (activeAsyncCalls.getAndDecrement() <= 1) {
            super.stopProgress();
        }
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

    protected void setLentMovieInfo(LentMovieInfo lentMovieInfo) {
        this.lentMovieInfo = lentMovieInfo;
        movieDetailFragment.setLentMovieInfo(lentMovieInfo);

        updateOptionsMenu();
    }

    protected void update() {
        updateMovieDetails();
        updateLentMovieInfo();
    }

    protected void updateMovieDetails() {
        startProgress(getString(R.string.moviedetail_progress_load_movie));

        new GetMovieTask(this) {
            @Override
            protected void onPostExecute(Movie movie) {
                setMovie(movie);
                stopProgress();
            }
        }.execute(getMovieIdFromIntent());
    }

    protected void updateLentMovieInfo() {
        startProgress(getString(R.string.moviedetail_progress_load_movie));

        new GetLentMovieInfoTask(this) {
            @Override
            protected void onPostExecute(LentMovieInfo lentMovieInfo) {
                setLentMovieInfo(lentMovieInfo);
                stopProgress();
            }
        }.execute(getMovieIdFromIntent());
    }

    protected void updateOptionsMenu() {
        if (isMovieInCollection()) {
            showAddMovieMenuItem(false);
            showRemoveMovieMenuItem(true);

            if (isMovieLent()) {
                showLendMovieMenuItem(false);
                showReturnMovieMenuItem(true);
            } else {
                showLendMovieMenuItem(true);
                showReturnMovieMenuItem(false);
            }
        } else {
            showAddMovieMenuItem(true);
            showRemoveMovieMenuItem(false);
            showLendMovieMenuItem(false);
            showReturnMovieMenuItem(false);
        }
    }

    protected boolean isMovieInCollection() {
        return movie != null && !Strings.isNullOrEmpty(movie.getId());
    }

    protected boolean isMovieLent() {
        return isMovieInCollection()
                && lentMovieInfo != null
                && !Strings.isNullOrEmpty(lentMovieInfo.getPerson());
    }

    private void showAddMovieMenuItem(boolean show) {
        if (addMovieMenuItem != null) {
            addMovieMenuItem.setVisible(show);
        }
    }

    private void showRemoveMovieMenuItem(boolean show) {
        if (removeMovieMenuItem != null) {
            removeMovieMenuItem.setVisible(show);
        }
    }

    private void showLendMovieMenuItem(boolean show) {
        if (lendMovieMenuItem != null) {
            lendMovieMenuItem.setVisible(show);
        }
    }

    private void showReturnMovieMenuItem(boolean show) {
        if (movieReturnedMenuItem != null) {
            movieReturnedMenuItem.setVisible(show);
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

                        notifySuccessfulDeletion();
                    }
                });

                stopProgress();
            }
        }.execute(getMovie().getId());
        finish();
    }

    private void startLendMovieProcess() {
        startProgress(getString(R.string.moviedetail_progress_get_people));

        new GetPeopleTask(this) {
            @Override
            protected void onPostExecute(final List<String> people) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayPeopleNameSelectionDialog(people);
                    }
                });

                stopProgress();
            }
        }.execute();
    }

    private void displayPeopleNameSelectionDialog(List<String> people) {
        new PeopleNameSelectionDialog(this, new PeopleNameSelectionDialog.Callback() {
            @Override
            public void onSubmit(String person) {
                lendMovie(person);
            }

            @Override
            public void onCancel() {
                // nothing to do
            }
        }, people).show();
    }

    private void lendMovie(String person) {
        startProgress(getString(R.string.moviedetail_progress_lend_movie));

        new LendMovieTask(this) {
            @Override
            protected void onPostExecute(final LentMovieInfo lentMovieInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLentMovieInfo(lentMovieInfo);
                        notifySuccessfulLentMovie();
                    }
                });

                stopProgress();
            }
        }.execute(new LentMovieInfo(movie.getId(), person, LocalDate.now()));
    }

    private void movieReturned() {
        startProgress(getString(R.string.moviedetail_progress_remove_lent_movie_info));

        new DeleteLentMovieInfoTask(this) {
            @Override
            protected void onPostExecute(final LentMovieInfo lentMovieInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lentMovieInfo != null) {
                            setLentMovieInfo(null);
                            notifySuccessfulDeleteLentMovieInfo();
                        }
                    }
                });

                stopProgress();
            }
        }.execute(movie.getId());
    }

    private void notifySuccessfulDeletion() {
        Toast.makeText(
                MovieDetailActivity.this,
                getString(R.string.moviedetail_delete_movie_successful, getMovie().getTitle()),
                Toast.LENGTH_SHORT)
                .show();
    }

    private void notifySuccessfulLentMovie() {
        // TODO
    }

    private void notifySuccessfulDeleteLentMovieInfo() {
        // TODO
    }
}
