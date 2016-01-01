package iweinzierl.github.com.moviedatabase.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.util.CollectionUtils;

public class MovieGenreFilter implements MovieListFilter {

    private final Set<String> genres;

    public MovieGenreFilter(Set<String> genres) {
        this.genres = genres;
    }

    @Override
    public boolean isActive() {
        return genres != null && !genres.isEmpty();
    }

    @Override
    public List<Movie> perform(List<Movie> movies) {
        if (genres == null || genres.isEmpty()) {
            return movies;
        }

        Collection<Movie> filteredMovies = Collections2.filter(movies, new Predicate<Movie>() {
            @Override
            public boolean apply(Movie input) {
                Set<String> intersection = CollectionUtils.extractIntersection(genres, input.getGenres());
                return !intersection.isEmpty();
            }
        });

        return new ArrayList<>(filteredMovies);
    }

    public Set<String> getGenres() {
        return genres;
    }
}
