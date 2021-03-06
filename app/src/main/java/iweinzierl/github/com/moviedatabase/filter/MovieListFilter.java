package iweinzierl.github.com.moviedatabase.filter;

import java.util.List;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public interface MovieListFilter {

    boolean isActive();

    List<Movie> perform(List<Movie> movies);
}
