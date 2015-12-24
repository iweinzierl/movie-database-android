package iweinzierl.github.com.moviedatabase;

import android.os.Bundle;

import java.util.List;

import iweinzierl.github.com.moviedatabase.async.SearchMovieTask;
import iweinzierl.github.com.moviedatabase.fragment.SearchMovieFragment;
import iweinzierl.github.com.moviedatabase.rest.domain.Movie;

public class SearchMovieActivity extends BaseActivity implements SearchMovieFragment.Callback {

    private SearchMovieFragment searchMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchMovieFragment = new SearchMovieFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, searchMovieFragment)
                .commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    public void onStartSearch(String search) {
        new SearchMovieTask(this) {
            @Override
            protected void onPostExecute(final List<Movie> movies) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchMovieFragment.setSearchResults(movies);
                    }
                });
            }
        }.execute(search);
    }
}
