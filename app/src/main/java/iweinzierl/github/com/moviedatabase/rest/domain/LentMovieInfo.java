package iweinzierl.github.com.moviedatabase.rest.domain;

import org.joda.time.LocalDate;

public class LentMovieInfo {

    private String movieId;
    private String person;

    private LocalDate lentDate;

    public LentMovieInfo(String movieId, String person, LocalDate lentDate) {
        this.movieId = movieId;
        this.person = person;
        this.lentDate = lentDate;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public LocalDate getLentDate() {
        return lentDate;
    }

    public void setLentDate(LocalDate lentDate) {
        this.lentDate = lentDate;
    }
}
