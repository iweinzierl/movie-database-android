package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import iweinzierl.github.com.moviedatabase.async.GetGenresTask;
import iweinzierl.github.com.moviedatabase.fragment.SelectableGenreListFragment;

public class SelectableGenreListActivity extends AppCompatActivity {

    public static final int REQUEST_GENRES = 100;

    public static final String EXTRA_SELECTED_GENRES = "selectablegenrelist.extra.genres";

    private SelectableGenreListFragment selectableGenreListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectable_genre_list);

        selectableGenreListFragment = new SelectableGenreListFragment();

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbarTop);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitGenres();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, selectableGenreListFragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        new GetGenresTask(this) {
            @Override
            protected void onPostExecute(Set<String> genres) {
                setGenres(genres);
                applyInitialCheckedGenres();
            }
        }.execute();
    }

    private void setGenres(Set<String> genres) {
        selectableGenreListFragment.setGenres(genres);
    }

    private void applyInitialCheckedGenres() {
        Intent intent = getIntent();
        List<String> checkedGenres = intent.getStringArrayListExtra(EXTRA_SELECTED_GENRES);

        selectableGenreListFragment.setSelectedGenres(checkedGenres);
    }

    private void submitGenres() {
        Set<String> genres = selectableGenreListFragment.getSelectedGenres();

        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_SELECTED_GENRES, new ArrayList<>(genres));

        setResult(RESULT_OK, data);
        finish();
    }
}
