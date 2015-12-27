package iweinzierl.github.com.moviedatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iweinzierl.android.utils.UiUtils;
import com.google.common.base.Joiner;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieListAdapter extends BaseListAdapter<Movie> {

    private static class MovieComparator implements Comparator<Movie> {

        @Override
        public int compare(Movie one, Movie two) {
            return one.getTitle().compareTo(two.getTitle());
        }
    }

    private static class ViewHolder {
        private ImageView cover;
        private TextView title;
        private TextView genres;
        private TextView length;
    }

    public MovieListAdapter(Context context, List<Movie> items) {
        this(context, items, new MovieComparator());
    }

    public MovieListAdapter(Context context, List<Movie> items, Comparator<Movie> comparator) {
        super(context, items, comparator);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Movie movie = getTypedItem(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_movie, viewGroup, false);

            ImageView cover = UiUtils.getGeneric(ImageView.class, view, R.id.cover);
            TextView title = UiUtils.getGeneric(TextView.class, view, R.id.title);
            TextView genres = UiUtils.getGeneric(TextView.class, view, R.id.genres);
            TextView length = UiUtils.getGeneric(TextView.class, view, R.id.length);

            ViewHolder holder = new ViewHolder();
            holder.cover = cover;
            holder.title = title;
            holder.genres = genres;
            holder.length = length;

            fillView(movie, holder);
            view.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            fillView(movie, holder);
        }

        return view;
    }

    private void fillView(Movie movie, ViewHolder holder) {
        loadCover(holder.cover, movie.getCoverUrl());
        holder.title.setText(movie.getTitle());
        holder.genres.setText(makeGenresText(movie.getGenres()));
        holder.length.setText(makeLengthText(movie.getLength()));
    }

    private void loadCover(ImageView imageView, String imageUri) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUri, imageView);
    }

    private String makeGenresText(Set<String> genres) {
        return Joiner.on(", ").join(genres);
    }

    private String makeLengthText(int length) {
        return String.format("%s min", length);
    }
}
