package iweinzierl.github.com.moviedatabase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.iweinzierl.android.utils.UiUtils;

import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.adapter.SelectableMovieFormatListAdapter;
import iweinzierl.github.com.moviedatabase.rest.domain.MovieFormat;

public class SelectableMovieFormatListFragment extends Fragment {

    public interface Callback {
        void onMovieFormatSelectionChanged(Set<MovieFormat> selectedFormats);
    }

    private SelectableMovieFormatListAdapter formatListAdapter;

    private Callback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movieformat_list_selectable, container, false);

        formatListAdapter = new SelectableMovieFormatListAdapter(getActivity());

        ListView genreList = UiUtils.getGeneric(ListView.class, view, R.id.movieformat_list);
        genreList.setAdapter(formatListAdapter);
        genreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                formatListAdapter.setChecked(i, !formatListAdapter.isChecked(i));
                fireSelectionChanged();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();

        if (activity instanceof Callback) {
            callback = (Callback) activity;
        }
    }

    public Set<MovieFormat> getSelectedFormats() {
        return formatListAdapter.getSelectedFormats();
    }

    public void setSelectedFormats(List<MovieFormat> checkedFormats) {
        if (checkedFormats == null || checkedFormats.isEmpty()) {
            return;
        }

        for (MovieFormat format : checkedFormats) {
            int index = formatListAdapter.getIndexOf(format);
            if (index >= 0) {
                formatListAdapter.setChecked(index, true);
            }
        }

        fireSelectionChanged();
    }

    private void fireSelectionChanged() {
        if (callback != null) {
            callback.onMovieFormatSelectionChanged(formatListAdapter.getSelectedFormats());
        }
    }
}
