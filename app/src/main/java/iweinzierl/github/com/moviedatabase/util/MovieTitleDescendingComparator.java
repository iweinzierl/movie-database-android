package iweinzierl.github.com.moviedatabase.util;

import java.util.Comparator;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieTitleDescendingComparator implements Comparator<Movie> {

    @Override
    public int compare(Movie one, Movie two) {
        return two.getTitle().compareTo(one.getTitle());
    }
}
