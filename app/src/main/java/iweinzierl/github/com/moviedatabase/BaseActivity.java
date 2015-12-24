package iweinzierl.github.com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import iweinzierl.github.com.moviedatabase.navigation.NavigationAdapter;


public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;

    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.navigation_drawer);
        //recyclerView.setAdapter(new NavigationAdapter(getUsername()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    drawerLayout.closeDrawers();
                    startActivity(recyclerView.getChildLayoutPosition(child));
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            toolbar.setTitle(R.string.dashboard_title);
            //toolbar.setNavigationIcon(R.drawable.ic_menu_white_36dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    private void startActivity(int pos) {
        if (pos == 0) {
            return;
        }

        // XXX too hard coupling of logic in NavigationAdapter and here!
        int item = NavigationAdapter.navigationItems[pos - 1];

        switch (item) {
            case R.string.nav_dashboard:
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.string.nav_search_movie:
                startActivity(new Intent(this, SearchMovieActivity.class));
                break;
        }
    }

    protected abstract int getLayoutId();
}
