package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.iweinzierl.android.logging.AndroidLoggerFactory;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.async.GetMoviesTask;
import iweinzierl.github.com.moviedatabase.fragment.MovieListFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.util.CollectionUtils;
import iweinzierl.github.com.moviedatabase.util.MovieTitleAscendingComparator;

public class MovieListActivity extends BaseActivity implements MovieListFragment.Callback {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(MovieListActivity.class.getName());

    private List<Movie> movies;

    private MovieListFragment movieListFragment;

    private Comparator<Movie> movieComparator;
    private List<String> genresFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieListFragment = new MovieListFragment();
        movieComparator = new MovieTitleAscendingComparator();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SelectableGenreListActivity.REQUEST_GENRES:
                    setGenresFilter(data.getStringArrayListExtra(SelectableGenreListActivity.EXTRA_SELECTED_GENRES));
                    break;
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_collection:
                showSortCollectionOptions();
                return true;
            case R.id.filter_collection:
                showFilterCollectionOptions();
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

    public void setGenresFilter(ArrayList<String> filter) {
        this.genresFilter = filter;
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

    private void showSortCollectionOptions() {
        Intent intent = new Intent(this, MovieComparatorChoiceActivity.class);
        intent.putExtra(MovieComparatorChoiceActivity.EXTRA_SELECTED_COMPARATOR, movieComparator.getClass().getName());

        startActivityForResult(intent, MovieComparatorChoiceActivity.REQUEST_COMPARATOR);
    }

    private void showFilterCollectionOptions() {
        Intent intent = new Intent(this, SelectableGenreListActivity.class);
        intent.putStringArrayListExtra(SelectableGenreListActivity.EXTRA_SELECTED_GENRES, (ArrayList<String>) genresFilter);

        startActivityForResult(intent, SelectableGenreListActivity.REQUEST_GENRES);
    }

    private void applyFilterAndComparator() {
        movieListFragment.setMovies(filterMovies(), movieComparator);
    }

    private List<Movie> filterMovies() {
        if (genresFilter == null || genresFilter.isEmpty()) {
            return movies;
        }

        Collection<Movie> filteredMovies = Collections2.filter(movies, new Predicate<Movie>() {
            @Override
            public boolean apply(Movie input) {
                Set<String> intersection = CollectionUtils.extractIntersection(genresFilter, input.getGenres());
                return !intersection.isEmpty();
            }
        });

        return new ArrayList<>(filteredMovies);
    }
}
