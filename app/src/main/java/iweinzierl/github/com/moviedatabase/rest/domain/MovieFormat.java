package iweinzierl.github.com.moviedatabase.rest.domain;

public enum MovieFormat {

    VHS("VHS"),
    DVD("DVD"),
    BLURAY("Blu-Ray");

    private String title;

    MovieFormat(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public static MovieFormat fromTitle(String title) {
        for (MovieFormat format : values()) {
            if (format.title().equals(title)) {
                return format;
            }
        }

        return null;
    }
}
