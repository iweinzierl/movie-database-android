package iweinzierl.github.com.moviedatabase.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.iweinzierl.android.utils.UiUtils;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.rest.domain.MovieFormat;

public class MovieFormatSelectionDialog {

    public interface Callback {
        void onFormatSelected(String format);
    }

    private final Context context;
    private final Callback callback;

    private AlertDialog dialog;

    public MovieFormatSelectionDialog(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void show() {
        build();
        dialog.show();
    }

    private void build() {
        dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_movie_format_selection_title)
                .setView(buildContent())
                .create();
    }

    private View buildContent() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View content = inflater.inflate(R.layout.dialog_movie_format_selection, null);

        Button vhs = UiUtils.getGeneric(Button.class, content, R.id.vhs);
        Button dvd = UiUtils.getGeneric(Button.class, content, R.id.dvd);
        Button bluRay = UiUtils.getGeneric(Button.class, content, R.id.blu_ray);

        vhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onFormatSelected(MovieFormat.VHS.title());
                dialog.dismiss();
            }
        });

        dvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onFormatSelected(MovieFormat.DVD.title());
                dialog.dismiss();
            }
        });

        bluRay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onFormatSelected(MovieFormat.BLURAY.title());
                dialog.dismiss();
            }
        });

        return content;
    }
}
