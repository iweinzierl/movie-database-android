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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.adapter.SelectableGenreListAdapter;

public class SelectableGenreListFragment extends Fragment {

    public interface Callback {
        void onGenreSelectionChanged(Set<String> selectedGenres);
    }

    private SelectableGenreListAdapter genreListAdapter;

    private Callback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_list_selectable, container, false);

        genreListAdapter = new SelectableGenreListAdapter(getActivity());

        ListView genreList = UiUtils.getGeneric(ListView.class, view, R.id.genre_list);
        genreList.setAdapter(genreListAdapter);
        genreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                genreListAdapter.setChecked(i, !genreListAdapter.isChecked(i));
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

    public void setGenres(Set<String> genres) {
        genreListAdapter.setItems(new ArrayList<>(genres));
    }

    public Set<String> getSelectedGenres() {
        return genreListAdapter.getSelectedGenres();
    }

    public void setSelectedGenres(List<String> checkedGenres) {
        if (checkedGenres == null || checkedGenres.isEmpty()) {
            return;
        }

        for (String genre : checkedGenres) {
            int index = genreListAdapter.getIndexOf(genre);
            if (index >= 0) {
                genreListAdapter.setChecked(index, true);
            }
        }

        fireSelectionChanged();
    }

    private void fireSelectionChanged() {
        if (callback != null) {
            callback.onGenreSelectionChanged(genreListAdapter.getSelectedGenres());
        }
    }
}
