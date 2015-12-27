package iweinzierl.github.com.moviedatabase.rest.domain;

import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("numberOfMovies")
    private int numberOfMovies;

    @SerializedName("numberOfGenres")
    private int numberOfGenres;

    @SerializedName("numberOfLentMovies")
    private int numberOfLentMovies;

    public int getNumberOfMovies() {
        return numberOfMovies;
    }

    public void setNumberOfMovies(int numberOfMovies) {
        this.numberOfMovies = numberOfMovies;
    }

    public int getNumberOfGenres() {
        return numberOfGenres;
    }

    public void setNumberOfGenres(int numberOfGenres) {
        this.numberOfGenres = numberOfGenres;
    }

    public int getNumberOfLentMovies() {
        return numberOfLentMovies;
    }

    public void setNumberOfLentMovies(int numberOfLentMovies) {
        this.numberOfLentMovies = numberOfLentMovies;
    }
}
