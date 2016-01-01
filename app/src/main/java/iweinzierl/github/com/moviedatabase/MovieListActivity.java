package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;

import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

import iweinzierl.github.com.moviedatabase.async.GetMoviesTask;
import iweinzierl.github.com.moviedatabase.filter.MovieFilterManager;
import iweinzierl.github.com.moviedatabase.fragment.MovieListFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.util.MovieTitleAscendingComparator;

public class MovieListActivity extends BaseActivity implements MovieListFragment.Callback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(MovieListActivity.class.getName());

    private List<Movie> movies;

    private MovieListFragment movieListFragment;
    private MenuItem filterCollectionMenuItem;

    private Comparator<Movie> movieComparator;
    private MovieFilterManager filterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieListFragment = new MovieListFragment();
        movieComparator = new MovieTitleAscendingComparator();
        filterManager = MovieFilterManager.getInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, movieListFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startProgress(getString(R.string.movielist_progress_load_movies));

        new GetMoviesTask(this) {
            @Override
            protected void onPostExecute(List<Movie> movies) {
                super.onPostExecute(movies);
                setMovies(movies);
                stopProgress();
            }
        }.execute();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        displayFilterCollectionMenuItem(filterManager.isActive());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MovieComparatorChoiceActivity.REQUEST_COMPARATOR:
                    setMovieComparator(data.getStringExtra(MovieComparatorChoiceActivity.EXTRA_SELECTED_COMPARATOR));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_movie_list, menu);

        filterCollectionMenuItem = menu.findItem(R.id.filter_collection);
        displayFilterCollectionMenuItem(filterManager.isActive());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_collection:
                showSortCollectionOptions();
                return true;
            case R.id.filter_collection:
                showFilterManagerActivity();
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
    public void onMovieClicked(Movie movie) {
        if (movie != null) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());

            startActivity(intent);
        }
    }

    private void setMovies(List<Movie> movies) {
        this.movies = movies;
        applyFilterAndComparator();
    }

    @SuppressWarnings("unchecked")
    public void setMovieComparator(String comparatorClassName) {
        try {
            Class<?> comparatorClass = Class.forName(comparatorClassName);
            movieComparator = (Comparator<Movie>) comparatorClass.newInstance();

            applyFilterAndComparator();
        } catch (ClassNotFoundException e) {
            LOG.error("Comparator class not found: {}", comparatorClassName, e);
        } catch (InstantiationException e) {
            LOG.error("Unable to create instance of comparator class: {}", comparatorClassName, e);
        } catch (IllegalAccessException e) {
            LOG.error("Not allowed to create instance of comparator class: {}", comparatorClassName, e);
        }
    }

    private void displayFilterCollectionMenuItem(boolean active) {
        if (filterCollectionMenuItem != null) {
            if (active) {
                filterCollectionMenuItem.setIcon(getDrawable(R.drawable.ic_filter_list_white_24dp_enabled));
            } else {
                filterCollectionMenuItem.setIcon(getDrawable(R.drawable.ic_filter_list_white_24dp));
            }
        }
    }

    private void showSortCollectionOptions() {
        Intent intent = new Intent(this, MovieComparatorChoiceActivity.class);
        intent.putExtra(MovieComparatorChoiceActivity.EXTRA_SELECTED_COMPARATOR, movieComparator.getClass().getName());

        startActivityForResult(intent, MovieComparatorChoiceActivity.REQUEST_COMPARATOR);
    }

    private void showFilterManagerActivity() {
        Intent intent = new Intent(this, MovieFilterManagerActivity.class);
        startActivity(intent);
    }

    private void applyFilterAndComparator() {
        movieListFragment.setMovies(filterMovies(), movieComparator);
    }

    private List<Movie> filterMovies() {
        return filterManager.perform(movies);
    }
}
