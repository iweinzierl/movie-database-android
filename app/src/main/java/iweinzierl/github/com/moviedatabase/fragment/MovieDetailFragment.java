package iweinzierl.github.com.moviedatabase.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iweinzierl.android.utils.UiUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.LocalDate;

import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.LentMovieInfo;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieDetailFragment extends Fragment {

    private ImageView cover;
    private TextView title;
    private TextView originalTitle;
    private TextView publishDate;
    private TextView length;
    private TextView genres;
    private TextView description;

    private TextView lentMovieTo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        cover = UiUtils.getGeneric(ImageView.class, view, R.id.cover);
        title = UiUtils.getGeneric(TextView.class, view, R.id.title);
        originalTitle = UiUtils.getGeneric(TextView.class, view, R.id.original_title);
        publishDate = UiUtils.getGeneric(TextView.class, view, R.id.publish_date);
        length = UiUtils.getGeneric(TextView.class, view, R.id.length);
        genres = UiUtils.getGeneric(TextView.class, view, R.id.genres);
        description = UiUtils.getGeneric(TextView.class, view, R.id.description);
        lentMovieTo = UiUtils.getGeneric(TextView.class, view, R.id.lent_movie_to);

        return view;
    }

    public void setMovie(Movie movie) {
        loadCover(cover, movie.getCoverUrl());
        title.setText(movie.getTitle());
        originalTitle.setText(movie.getOriginalTitle());
        publishDate.setText(formatDate(movie.getPublished()));
        length.setText(formatLength(movie.getLength()));
        genres.setText(formatGenres(movie.getGenres()));
        description.setText(movie.getDescription());
    }

    public void setLentMovieInfo(LentMovieInfo lentMovieInfo) {
        if (lentMovieInfo != null && !Strings.isNullOrEmpty(lentMovieInfo.getPerson())) {
            lentMovieTo.setText(getString(R.string.moviedetail_label_lent_movie_to, lentMovieInfo.getPerson()));
            lentMovieTo.setVisibility(View.VISIBLE);
        }
        else if (lentMovieInfo == null) {
            lentMovieTo.setVisibility(View.GONE);
        }
    }

    private void loadCover(ImageView coverView, String coverUri) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(coverUri, coverView);
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.toString("yyyy-MM-dd") : "";
    }

    private String formatLength(int length) {
        return String.format("%s min", length);
    }

    private String formatGenres(Set<String> genres) {
        return Joiner.on(", ").join(genres);
    }
}
