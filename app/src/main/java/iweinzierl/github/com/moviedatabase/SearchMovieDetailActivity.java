package iweinzierl.github.com.moviedatabase;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.base.Strings;

import iweinzierl.github.com.moviedatabase.async.GetSearchMovieTask;
import iweinzierl.github.com.moviedatabase.async.SaveMovieTask;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.util.MovieFormatSelectionDialog;

public class SearchMovieDetailActivity extends MovieDetailActivity {

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        addMovieMenuItem.setVisible(true);
        removeMovieMenuItem.setVisible(false);
        lendMovieMenuItem.setVisible(false);
        movieReturnedMenuItem.setVisible(false);

        updateOptionsMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_collection:
                startAddMovieToCollectionProcess();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void update() {
        startProgress(getString(R.string.moviedetail_progress_load_movie));

        new GetSearchMovieTask(this) {
            @Override
            protected void onPostExecute(final Movie movie) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movie.setId(null);
                        setMovie(movie);
                    }
                });

                stopProgress();
            }
        }.execute(getMovieIdFromIntent());
    }

    private void startAddMovieToCollectionProcess() {
        new MovieFormatSelectionDialog(this, new MovieFormatSelectionDialog.Callback() {
            @Override
            public void onFormatSelected(String format) {
                addMovieToCollection(format);
            }
        }).show();
    }

    private void addMovieToCollection(String formatInCollection) {
        startProgress(getString(R.string.moviedetail_progress_add_movie_to_collection));

        getMovie().setFormatInCollection(formatInCollection);

        new SaveMovieTask(this) {
            @Override
            protected void onPostExecute(final Movie movie) {
                if (movie != null && !Strings.isNullOrEmpty(movie.getId())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setMovie(movie);

                            notifySuccessfullPersistence();
                        }
                    });
                }

                stopProgress();
            }
        }.execute(getMovie());
    }

    private void notifySuccessfullPersistence() {
        Toast.makeText(
                SearchMovieDetailActivity.this,
                getString(R.string.moviedetail_save_movie_successful, getMovie().getTitle()),
                Toast.LENGTH_SHORT)
                .show();
    }
}
