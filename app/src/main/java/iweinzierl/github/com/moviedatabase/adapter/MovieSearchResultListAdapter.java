package iweinzierl.github.com.moviedatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iweinzierl.android.utils.UiUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.Comparator;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieSearchResultListAdapter extends BaseListAdapter<Movie> {

    private class ViewHolder {
        private ImageView cover;
        private TextView title;
        private TextView description;
    }

    private static class MovieComparator implements Comparator<Movie> {

        @Override
        public int compare(Movie movie, Movie t1) {
            return movie.getTitle().compareTo(t1.getTitle());
        }
    }

    public MovieSearchResultListAdapter(Context context) {
        super(context, Collections.EMPTY_LIST, new MovieComparator());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Movie movie = getTypedItem(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_movie_search_result, viewGroup, false);

            ImageView cover = UiUtils.getGeneric(ImageView.class, view, R.id.cover);
            TextView title = UiUtils.getGeneric(TextView.class, view, R.id.title);
            TextView description = UiUtils.getGeneric(TextView.class, view, R.id.description);

            ViewHolder holder = new ViewHolder();
            holder.cover = cover;
            holder.title = title;
            holder.description = description;

            loadImage(movie.getCover(), cover);
            title.setText(movie.getTitle());
            description.setText(createShortDescription(movie.getDescription()));

            view.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            loadImage(movie.getCover(), holder.cover);
            holder.title.setText(movie.getTitle());
            holder.description.setText(createShortDescription(movie.getDescription()));
        }

        return view;
    }

    private void loadImage(String imageUri, ImageView imageView) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUri, imageView);
    }

    private String createShortDescription(String description) {
        if (description == null || description.length() < 200) {
            return description;
        }

        int spacePosition = description.indexOf(" ", 195);
        return String.format("%s ...", description.substring(0, spacePosition));
    }
}
