package iweinzierl.github.com.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.async.GetGenresTask;
import iweinzierl.github.com.moviedatabase.filter.MovieFilterManager;
import iweinzierl.github.com.moviedatabase.filter.MovieFormatFilter;
import iweinzierl.github.com.moviedatabase.filter.MovieGenreFilter;
import iweinzierl.github.com.moviedatabase.filter.MovieListFilter;
import iweinzierl.github.com.moviedatabase.fragment.SelectableGenreListFragment;
import iweinzierl.github.com.moviedatabase.fragment.SelectableMovieFormatListFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.MovieFormat;

public class MovieFilterManagerActivity extends AppCompatActivity implements SelectableGenreListFragment.Callback, SelectableMovieFormatListFragment.Callback {

    private MovieFilterManager filterManager;

    private SelectableGenreListFragment genreFilterFragment;
    private SelectableMovieFormatListFragment formatFilterFragment;

    private View genreFilterContainer;
    private View formatFilterContainer;

    private TextView selectedGenreFilterCount;
    private TextView selectedFormatFilterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_filter_manager);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbarTop);

        filterManager = MovieFilterManager.getInstance();

        genreFilterFragment = new SelectableGenreListFragment();
        formatFilterFragment = new SelectableMovieFormatListFragment();

        genreFilterContainer = findViewById(R.id.genre_filter_container);
        formatFilterContainer = findViewById(R.id.format_filter_container);
        selectedGenreFilterCount = (TextView) findViewById(R.id.genre_filter_count);
        selectedFormatFilterCount = (TextView) findViewById(R.id.format_filter_count);

        View genreSection = findViewById(R.id.genre_section_header);
        genreSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayGenreFilter();
            }
        });

        View formatSection = findViewById(R.id.format_section_header);
        formatSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFormatFilter();
            }
        });

        View submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.genre_fragment, genreFilterFragment)
                .replace(R.id.format_fragment, formatFilterFragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        new GetGenresTask(this) {
            @Override
            protected void onPostExecute(Set<String> genres) {
                setGenres(genres);
                applyInitialCheckedItems();
            }
        }.execute();
    }

    @Override
    public void onMovieFormatSelectionChanged(Set<MovieFormat> selectedFormats) {
        selectedFormatFilterCount.setText(
                getString(R.string.moviefiltermanager_selected_formats_count, selectedFormats.size())
        );
    }

    @Override
    public void onGenreSelectionChanged(Set<String> selectedGenres) {
        selectedGenreFilterCount.setText(
                getString(R.string.moviefiltermanager_selected_genres_count, selectedGenres.size())
        );
    }

    private void displayGenreFilter() {
        formatFilterContainer.setVisibility(View.GONE);
        genreFilterContainer.setVisibility(View.VISIBLE);
    }

    private void displayFormatFilter() {
        genreFilterContainer.setVisibility(View.GONE);
        formatFilterContainer.setVisibility(View.VISIBLE);
    }

    public void setGenres(Set<String> genres) {
        genreFilterFragment.setGenres(genres);
    }

    private void applyInitialCheckedItems() {
        for (MovieListFilter filter : filterManager.getFilters()) {
            if (filter instanceof MovieGenreFilter) {
                MovieGenreFilter genreFilter = (MovieGenreFilter) filter;
                Set<String> genres = genreFilter.getGenres();
                genreFilterFragment.setSelectedGenres(new ArrayList<>(genres));
            } else if (filter instanceof MovieFormatFilter) {
                MovieFormatFilter movieFormatFilter = (MovieFormatFilter) filter;
                Set<MovieFormat> formats = movieFormatFilter.getFormats();
                formatFilterFragment.setSelectedFormats(new ArrayList<>(formats));
            }
        }
    }

    private void submit() {
        configureGenreFilter();
        configureMovieFormatFilter();

        finish();
    }

    private void configureGenreFilter() {
        Set<String> selectedGenres = genreFilterFragment.getSelectedGenres();

        filterManager.addOrReplaceFilter(
                new MovieGenreFilter(selectedGenres)
        );
    }

    private void configureMovieFormatFilter() {
        Set<MovieFormat> selectedFormats = formatFilterFragment.getSelectedFormats();

        filterManager.addOrReplaceFilter(
                new MovieFormatFilter(selectedFormats)
        );
    }
}
