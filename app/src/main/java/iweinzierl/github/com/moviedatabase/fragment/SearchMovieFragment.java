package iweinzierl.github.com.moviedatabase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.iweinzierl.android.utils.UiUtils;
import com.google.common.base.Strings;

import java.util.List;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.adapter.MovieSearchResultListAdapter;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class SearchMovieFragment extends Fragment {

    public interface Callback {
        void onStartSearch(String search);

        void onMovieClicked(Movie movie);
    }

    private Callback callback;

    private EditText searchField;
    private MovieSearchResultListAdapter searchResultsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_movie, container, false);

        searchResultsAdapter = new MovieSearchResultListAdapter(getActivity());
        searchField = UiUtils.getGeneric(EditText.class, root, R.id.search_field);

        ListView searchResults = UiUtils.getGeneric(ListView.class, root, R.id.search_results);
        searchResults.setAdapter(searchResultsAdapter);
        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = searchResultsAdapter.getTypedItem(i);
                if (callback != null) {
                    callback.onMovieClicked(movie);
                }
            }
        });

        Button searchButton = UiUtils.getGeneric(Button.class, root, R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearch();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();

        if (activity instanceof Callback) {
            this.callback = (Callback) activity;
        }
    }

    public void setSearchResults(List<Movie> searchResults) {
        searchResultsAdapter.setItems(searchResults);
    }

    private void startSearch() {
        String search = searchField.getText().toString();

        if (!Strings.isNullOrEmpty(search) && callback != null) {
            callback.onStartSearch(search);
        }
    }
}
