package iweinzierl.github.com.moviedatabase.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.github.iweinzierl.android.utils.UiUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;

import iweinzierl.github.com.moviedatabase.R;
import iweinzierl.github.com.moviedatabase.adapter.BaseListAdapter;
import iweinzierl.github.com.moviedatabase.adapter.StringListAdapter;

public class PeopleNameSelectionDialog {

    public interface Callback {

        void onSubmit(String person);

        void onCancel();
    }

    private final Context context;
    private final Callback callback;
    private final List<String> peopleNames;

    private AlertDialog alertDialog;

    private EditText nameField;

    public PeopleNameSelectionDialog(Context context, Callback callback, List<String> peopleNames) {
        this.context = context;
        this.callback = callback;
        this.peopleNames = peopleNames;
    }

    public void show() {
        build();
        alertDialog.show();
    }

    private void build() {
        View content = buildContent();

        alertDialog = new AlertDialog.Builder(context)
                .setPositiveButton(R.string.dialog_people_name_selection_submit_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String personName = nameField.getText().toString();

                        if (!Strings.isNullOrEmpty(personName)) {
                            callback.onSubmit(personName);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_people_name_selection_cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onCancel();
                        dialogInterface.dismiss();
                    }
                })
                .setTitle(R.string.dialog_people_name_title)
                .setView(content)
                .create();
    }

    private View buildContent() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View content = layoutInflater.inflate(R.layout.dialog_people_name_selection, null);

        final ListView nameList = UiUtils.getGeneric(ListView.class, content, R.id.name_list);

        final BaseListAdapter.Filter<String> filter = new BaseListAdapter.Filter<String>() {
            @Override
            public List<String> performFilter(List<String> items, final Object filterObj) {
                return new ArrayList<>(Collections2.filter(items, new Predicate<String>() {
                    @Override
                    public boolean apply(String input) {
                        return filterObj == null || input != null
                                && input.contains((CharSequence) filterObj);
                    }
                }));
            }
        };

        final StringListAdapter adapter = new StringListAdapter(context, peopleNames);
        adapter.setFilter(filter);

        nameField = UiUtils.getGeneric(EditText.class, content, R.id.person_name);
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null) {
                    adapter.filter(null);
                } else {
                    adapter.filter(editable.toString());
                }
            }
        });

        nameList.setAdapter(adapter);
        nameList.setTextFilterEnabled(true);
        nameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String name = (String) adapterView.getAdapter().getItem(i);
                nameField.setText(name);
            }
        });

        return content;
    }
}
