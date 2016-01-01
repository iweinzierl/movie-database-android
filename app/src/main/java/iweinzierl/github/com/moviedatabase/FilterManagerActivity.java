package iweinzierl.github.com.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.async.GetGenresTask;
import iweinzierl.github.com.moviedatabase.filter.GenreFilter;
import iweinzierl.github.com.moviedatabase.filter.MovieListFilter;
import iweinzierl.github.com.moviedatabase.filter.MovieListFilterManager;
import iweinzierl.github.com.moviedatabase.fragment.SelectableGenreListFragment;

public class FilterManagerActivity extends AppCompatActivity {

    private MovieListFilterManager filterManager;

    private SelectableGenreListFragment selectableGenreListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectable_genre_list);

        filterManager = MovieListFilterManager.getInstance();
        selectableGenreListFragment = new SelectableGenreListFragment();

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbarTop);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitGenres();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, selectableGenreListFragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        new GetGenresTask(this) {
            @Override
            protected void onPostExecute(Set<String> genres) {
                setGenres(genres);
                applyInitialCheckedGenres();
            }
        }.execute();
    }

    private void setGenres(Set<String> genres) {
        selectableGenreListFragment.setGenres(genres);
    }

    private void applyInitialCheckedGenres() {
        Collection<MovieListFilter> filters = filterManager.getFilters();
        for (MovieListFilter filter : filters) {
            if (filter instanceof GenreFilter) {
                GenreFilter genreFilter = (GenreFilter) filter;
                selectableGenreListFragment.setSelectedGenres(new ArrayList<>(genreFilter.getGenres()));
            }
        }
    }

    private void submitGenres() {
        Set<String> genres = selectableGenreListFragment.getSelectedGenres();

        GenreFilter genreFilter = new GenreFilter(genres);
        filterManager.addOrReplaceFilter(genreFilter);

        finish();
    }
}
