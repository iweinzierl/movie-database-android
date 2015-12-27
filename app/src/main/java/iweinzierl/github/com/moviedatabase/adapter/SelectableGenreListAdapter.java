package iweinzierl.github.com.moviedatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iweinzierl.android.utils.UiUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;

public class SelectableGenreListAdapter extends BaseListAdapter<String> {

    private static class GenreComparator implements Comparator<String> {
        @Override
        public int compare(String one, String two) {
            return one.compareTo(two);
        }
    }

    private static class ViewHolder {
        private ImageView checkbox;
        private TextView title;
    }

    private boolean[] checked;

    @SuppressWarnings("unchecked")
    public SelectableGenreListAdapter(Context context) {
        super(context, Collections.EMPTY_LIST, new GenreComparator());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String genre = getTypedItem(i);
        boolean isChecked = isChecked(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_genre_selectable, viewGroup, false);

            ImageView checkbox = UiUtils.getGeneric(ImageView.class, view, R.id.checkbox);
            TextView title = UiUtils.getGeneric(TextView.class, view, R.id.title);

            ViewHolder holder = new ViewHolder();
            holder.checkbox = checkbox;
            holder.title = title;

            fill(holder, genre, isChecked);

            view.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            fill(holder, genre, isChecked);
        }

        return view;
    }

    @Override
    public void setItems(List<String> items) {
        super.setItems(items);
        checked = new boolean[items.size()];
        Arrays.fill(checked, false);
    }

    private void fill(ViewHolder holder, String genre, boolean checked) {
        holder.title.setText(genre);
        if (checked) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isChecked(int index) {
        return checked[index];
    }

    public void setChecked(int index, boolean checked) {
        this.checked[index] = checked;
        notifyDataSetChanged();
    }

    public Set<String> getSelectedGenres() {
        Set<String> genres = new HashSet<>();

        for (int i = 0; i < getCount(); i++) {
            if (isChecked(i)) {
                genres.add(getTypedItem(i));
            }
        }

        return genres;
    }
}
