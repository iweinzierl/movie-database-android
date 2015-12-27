package iweinzierl.github.com.moviedatabase;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.base.Strings;

import iweinzierl.github.com.moviedatabase.async.GetSearchMovieTask;
import iweinzierl.github.com.moviedatabase.async.SaveMovieTask;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class SearchMovieDetailActivity extends MovieDetailActivity {

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        addMovieMenuItem.setVisible(true);
        removeMovieMenuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_collection:
                addMovieToCollection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void update() {
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
            }
        }.execute(getMovieIdFromIntent());
    }

    private void addMovieToCollection() {
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
