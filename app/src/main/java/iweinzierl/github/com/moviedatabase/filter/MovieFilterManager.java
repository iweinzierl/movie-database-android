package iweinzierl.github.com.moviedatabase.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieFilterManager implements MovieListFilter {

    private static MovieFilterManager INSTANCE;

    private Map<Class<MovieListFilter>, MovieListFilter> filters;

    private MovieFilterManager() {
        filters = new HashMap<>();
    }

    public static MovieFilterManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieFilterManager();
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
