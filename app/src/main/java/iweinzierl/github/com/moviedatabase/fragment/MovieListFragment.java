package iweinzierl.github.com.moviedatabase.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.adapter.MovieListAdapter;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class MovieListFragment extends Fragment {

    public interface Callback {
        void onMovieClicked(Movie movie);
    }

    private MovieListAdapter movieListAdapter;

    private Callback callback;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        movieListAdapter = new MovieListAdapter(getActivity(), Collections.EMPTY_LIST);

        ListView movieList = (ListView) view.findViewById(R.id.movie_list);
        movieList.setAdapter(movieListAdapter);
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (callback != null) {
                    Movie movie = movieListAdapter.getTypedItem(i);
                    callback.onMovieClicked(movie);
                }
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

    public void setMovies(List<Movie> movies) {
        movieListAdapter.setItems(movies);
    }
}
