package iweinzierl.github.com.moviedatabase.rest.domain;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

public class Movie {

    private String id;

    private String title;

    @SerializedName("originalTitle")
    private String originalTitle;

    @SerializedName("coverUrl")
    private String coverUrl;

    private String description;

    private Set<String> genres;
    private int length;

    private LocalDate published;

    @SerializedName("formatInCollection")
    private String formatInCollection;

    public Movie(Movie movie) {
        this.id = movie.getId();
        this.title = movie.title;
        this.originalTitle = movie.originalTitle;
        this.coverUrl = movie.coverUrl;
        this.description = movie.description;
        this.genres = new HashSet<>(movie.genres);
        this.length = movie.length;
        this.published = movie.published;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public String getFormatInCollection() {
        return formatInCollection;
    }

    public void setFormatInCollection(String formatInCollection) {
        this.formatInCollection = formatInCollection;
    }
}
