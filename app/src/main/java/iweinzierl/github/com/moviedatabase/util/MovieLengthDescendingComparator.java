package iweinzierl.github.com.moviedatabase.util;

import java.util.Comparator;

import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieLengthDescendingComparator implements Comparator<Movie> {

    @Override
    public int compare(Movie one, Movie two) {
        int lengthOne = one.getLength();
        int lengthTwo = two.getLength();

        if (lengthOne < lengthTwo) {
            return 1;
        } else if (lengthOne == lengthTwo) {
            return 0;
        } else {
            return -1;
        }
    }
}
