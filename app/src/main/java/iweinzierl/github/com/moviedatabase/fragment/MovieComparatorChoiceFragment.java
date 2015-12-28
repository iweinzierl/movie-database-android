package iweinzierl.github.com.moviedatabase.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.iweinzierl.android.utils.UiUtils;

import java.util.Comparator;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.adapter.MovieComparatorListAdapter;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieComparatorChoiceFragment extends Fragment {

    private MovieComparatorListAdapter movieComparatorListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_comparator_selectable, container, false);

        movieComparatorListAdapter = new MovieComparatorListAdapter(getActivity());

        ListView comparatorList = UiUtils.getGeneric(ListView.class, view, R.id.comparator_list);
        comparatorList.setAdapter(movieComparatorListAdapter);
        comparatorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                movieComparatorListAdapter.setSeletedComparator(i);
            }
        });

        return view;
    }

    public void setSelectedComparator(Class<? extends Comparator<Movie>> selectedComparator) {
        int index = movieComparatorListAdapter.getIndexOf(selectedComparator);

        if (index >= 0) {
            movieComparatorListAdapter.setSeletedComparator(index);
        }
    }

    public Class<? extends Comparator<Movie>> getSelectedComparator() {
        return movieComparatorListAdapter.getSelectedComparator();
    }
}
