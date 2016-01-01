package iweinzierl.github.com.moviedatabase.filter;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.rest.domain.MovieFormat;

public class MovieFormatFilter implements MovieListFilter {

    private final Set<MovieFormat> formats;

    public MovieFormatFilter(Set<MovieFormat> formats) {
        this.formats = formats;
    }

    @Override
    public boolean isActive() {
        return formats != null && !formats.isEmpty();
    }

    @Override
    public List<Movie> perform(List<Movie> movies) {
        if (formats == null || formats.isEmpty()) {
            return movies;
        }

        return new ArrayList<>(Collections2.filter(movies, new Predicate<Movie>() {
            @Override
            public boolean apply(Movie input) {
                return formats.contains(getMovieFormatFromString(input.getFormatInCollection()));
            }
        }));
    }

    public Set<MovieFormat> getFormats() {
        return formats;
    }

    private MovieFormat getMovieFormatFromString(String format) {
        if (Strings.isNullOrEmpty(format)) {
            return null;
        } else {
            return MovieFormat.fromTitle(format);
        }
    }
}
