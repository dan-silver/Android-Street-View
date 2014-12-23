package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dan-silver on 12/14/14.
 */
public class FavoritedLocsFragment extends Fragment {
    GridView grid;

    public FavoritedLocsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Activity activity = getActivity();

        View v = inflater.inflate(R.layout.favorited_locations, container, false);
        grid = (GridView) v.findViewById(R.id.favorited_locations_grid);
        grid.setAdapter(new FavImageAdapter(getActivity().getApplicationContext()));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (activity instanceof MainActivity)
                    ((MainActivity) activity).switchToExploreWithSaved(position);
            }
        });
        grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        grid.setMultiChoiceModeListener(new MultiChoiceModeListener());
        return v;
    }

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.getMenuInflater().inflate(R.menu.multi_select, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_discard_favorites:
                    int len = grid.getCount();
                    SparseBooleanArray checked = grid.getCheckedItemPositions();
                    for (int i = 0; i < len; i++)
                        if (checked.get(i)) {
                            ((FavImageAdapter) grid.getAdapter()).removeItem(i);
                            grid.invalidateViews();
                        }
                    return true;
                default:
                    return true;
            }
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                              boolean checked) {
            int selectCount = grid.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle(selectCount + " items selected");
                    break;
            }
        }
    }
}