package iweinzierl.github.com.moviedatabase.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.iweinzierl.android.utils.UiUtils;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;
import iweinzierl.github.com.moviedatabase.util.MovieLengthAscendingComparator;
import iweinzierl.github.com.moviedatabase.util.MovieLengthDescendingComparator;
import iweinzierl.github.com.moviedatabase.util.MovieTitleAscendingComparator;
import iweinzierl.github.com.moviedatabase.util.MovieTitleDescendingComparator;

public class MovieComparatorListAdapter extends BaseListAdapter<Class<? extends Comparator<Movie>>> {

    @SuppressWarnings("unchecked")
    private static final List<Class<? extends Comparator<Movie>>> COMPARATORS = Lists.newArrayList(
            MovieLengthAscendingComparator.class,
            MovieLengthDescendingComparator.class,
            MovieTitleAscendingComparator.class,
            MovieTitleDescendingComparator.class
    );

    private static class ViewHolder {
        private AppCompatRadioButton radioButton;
    }

    public MovieComparatorListAdapter(Context context) {
        super(context, COMPARATORS);
    }

    private int indexOfSelectedComparator;

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Class<? extends Comparator<Movie>> comparator = getTypedItem(i);
        boolean selected = i == indexOfSelectedComparator;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_movie_comparator_choice, viewGroup, false);

            AppCompatRadioButton radioButton = UiUtils.getGeneric(AppCompatRadioButton.class, view, R.id.radio_button);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSeletedComparator(i);
                }
            });

            ViewHolder holder = new ViewHolder();
            holder.radioButton = radioButton;

            fill(holder, comparator, selected);
            view.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            fill(holder, comparator, selected);
        }

        return view;
    }

    public void setSeletedComparator(int index) {
        this.indexOfSelectedComparator = index;
        notifyDataSetChanged();
    }

    public Class<? extends Comparator<Movie>> getSelectedComparator() {
        return getTypedItem(indexOfSelectedComparator);
    }

    private void fill(ViewHolder holder, Class<? extends Comparator<Movie>> comparator, boolean selected) {
        if (comparator == MovieTitleAscendingComparator.class) {
            holder.radioButton.setText(R.string.moviecomparatorchoice_listitem_title_asc_comparator);
        } else if (comparator == MovieTitleDescendingComparator.class) {
            holder.radioButton.setText(R.string.moviecomparatorchoice_listitem_title_desc_comparator);
        } else if (comparator == MovieLengthAscendingComparator.class) {
            holder.radioButton.setText(R.string.moviecomparatorchoice_listitem_length_asc_comparator);
        } else if (comparator == MovieLengthDescendingComparator.class) {
            holder.radioButton.setText(R.string.moviecomparatorchoice_listitem_length_desc_comparator);
        }

        holder.radioButton.setChecked(selected);
    }
}
