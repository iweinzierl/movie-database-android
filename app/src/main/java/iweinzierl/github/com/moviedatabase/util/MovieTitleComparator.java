package iweinzierl.github.com.moviedatabase.util;

import java.util.Comparator;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieTitleComparator implements Comparator<Movie> {

    @Override
    public int compare(Movie one, Movie two) {
        return one.getTitle().compareTo(two.getTitle());
    }
}
