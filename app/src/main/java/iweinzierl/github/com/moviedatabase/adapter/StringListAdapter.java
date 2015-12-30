package iweinzierl.github.com.moviedatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.android.utils.UiUtils;

import java.util.List;

import iweinzierl.github.com.moviedatabase.R;

public class StringListAdapter extends BaseListAdapter<String> {

    public StringListAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String item = (String) getItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listitem_text_item, viewGroup, false);

        TextView valueField = UiUtils.getGeneric(TextView.class, view, R.id.value);
        valueField.setText(item);

        return view;
    }
}
