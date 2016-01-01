package iweinzierl.github.com.moviedatabase.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieListFilterManager implements MovieListFilter {

    private static MovieListFilterManager INSTANCE;

    private Map<Class<MovieListFilter>, MovieListFilter> filters;

    private MovieListFilterManager() {
        filters = new HashMap<>();
    }

    public static MovieListFilterManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieListFilterManager();
        }

        return INSTANCE;
    }

    @Override
    public List<Movie> perform(List<Movie> movies) {
        if (filters.isEmpty()) {
            return movies;
        }

        return filterMovies(movies);
    }

    @SuppressWarnings("unchecked")
    public void addOrReplaceFilter(MovieListFilter filter) {
        filters.put((Class<MovieListFilter>) filter.getClass(), filter);
    }

    public Collection<MovieListFilter> getFilters() {
        return filters.values();
    }

    @Override
    public boolean isActive() {
        if (filters.isEmpty()) {
            return false;
        }

        for (MovieListFilter filter : filters.values()) {
            if (filter.isActive()) {
                return true;
            }
        }

        return false;
    }

    private List<Movie> filterMovies(List<Movie> movies) {
        List<Movie> results = new ArrayList<>(movies);

        for (MovieListFilter filter : filters.values()) {
            results = filter.perform(results);
        }

        return results;
    }
}
