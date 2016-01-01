package iweinzierl.github.com.moviedatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iweinzierl.android.utils.UiUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.MovieFormat;

public class SelectableMovieFormatListAdapter extends BaseListAdapter<MovieFormat> {

    private static class MovieFormatComparator implements Comparator<MovieFormat> {
        @Override
        public int compare(MovieFormat one, MovieFormat two) {
            return one.title().compareTo(two.title());
        }
    }

    private static class ViewHolder {
        private ImageView checkbox;
        private TextView title;
    }

    private boolean[] checked;

    @SuppressWarnings("unchecked")
    public SelectableMovieFormatListAdapter(Context context) {
        super(context, Arrays.asList(MovieFormat.values()), new MovieFormatComparator());
        checked = new boolean[items.size()];
        Arrays.fill(checked, false);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MovieFormat format = getTypedItem(i);
        boolean isChecked = isChecked(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_text_selectable, viewGroup, false);

            ImageView checkbox = UiUtils.getGeneric(ImageView.class, view, R.id.checkbox);
            TextView title = UiUtils.getGeneric(TextView.class, view, R.id.title);

            ViewHolder holder = new ViewHolder();
            holder.checkbox = checkbox;
            holder.title = title;

            fill(holder, format, isChecked);

            view.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            fill(holder, format, isChecked);
        }

        return view;
    }

    @Override
    public void setItems(List<MovieFormat> items) {
        super.setItems(items);
        checked = new boolean[items.size()];
        Arrays.fill(checked, false);
    }

    private void fill(ViewHolder holder, MovieFormat format, boolean checked) {
        holder.title.setText(format.title());
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

    public Set<MovieFormat> getSelectedFormats() {
        Set<MovieFormat> formats = new HashSet<>();

        for (int i = 0; i < getCount(); i++) {
            if (isChecked(i)) {
                formats.add(getTypedItem(i));
            }
        }

        return formats;
    }
}
