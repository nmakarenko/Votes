package org.bissoft.yean.votes.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.bissoft.yean.votes.R;

import java.util.ArrayList;

/**
 * An adapter that is used in ListView with list of reporters.
 * @author      Nataliia Makarenko
 */
public class ReportersArrayAdapter extends BaseAdapter {
    /** List of reporters. */
    private ArrayList<Reporter> data;
    /** Current activity. */
    private Activity activity;

    /**
     * Constructs a new instance of {@code ReportersArrayAdapter}.
     *
     * @param activity current activity on which this adapter constructs the list of reporters.
     * @param reporters list of reporters.
     */
    public ReportersArrayAdapter(Activity activity, ArrayList<Reporter> reporters) {
        this.activity = activity;
        this.data = reporters;

    }

    /**
     * Gets number of elements in list.
     *
     * @return number of elements in list
     */
    @Override
    public int getCount() {
        return this.data.size();
    }

    /**
     * Gets item of list on the specified position.
     *
     * @param position position of the item.
     *
     * @return item of the list.
     */
    @Override
    public Object getItem(int position) {
        return this.data.get(position);
    }

    /**
     * Gets item's id of list on the specified position.
     *
     * @param position position of the item.
     *
     * @return item's id of the list.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position the position of the item within the adapter's data set of the item whose view we want.
     * @param convertView the old view to reuse, if possible.
     * @param parent the parent that this view will eventually be attached to.
     *
     * @return view of ListView on the specified position.
     */
    public View getView(final int position, View convertView, ViewGroup parent) {


        View rootView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.reporter_list_item, parent, false);
        } else {
            rootView = convertView;
        }

        try {
            TextView tvReporterName = (TextView) rootView.findViewById(R.id.tvReporterName);
            TextView tvReporterPost = (TextView) rootView.findViewById(R.id.tvReporterPost);

            tvReporterName.setText(data.get(position).getName());
            tvReporterPost.setText(data.get(position).getPost());
        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

}

