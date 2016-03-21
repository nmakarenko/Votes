package org.bissoft.yean.votes.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bissoft.yean.votes.R;

import java.util.ArrayList;

/**
 * An adapter that is used in ListView with list of bills.
 * @author      Nataliia Makarenko
 */
public class BillsArrayAdapter extends BaseAdapter {
    /** List of bills. */
    private ArrayList<Bill> data;
    /** Current activity. */
    private Activity activity;

    /**
     * Constructs a new instance of {@code BillsArrayAdapter}.
     *
     * @param activity current activity on which this adapter constructs the list of bills.
     * @param bills list of bills.
     */
    public BillsArrayAdapter(Activity activity, ArrayList<Bill> bills) {
        this.activity = activity;
        this.data = bills;

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
            rootView = inflater.inflate(R.layout.bills_list_item, parent, false);
        } else {
            rootView = convertView;
        }

        try {
            LinearLayout llItemAccepted = (LinearLayout) rootView.findViewById(R.id.llItemAccepted);
            LinearLayout llItemVote = (LinearLayout) rootView.findViewById(R.id.llItemVote);

            TextView tvItemId = (TextView) rootView.findViewById(R.id.tvItemId);
            TextView tvItemTitle = (TextView) rootView.findViewById(R.id.tvItemTitle);
            TextView tvItemAccepted = (TextView) rootView.findViewById(R.id.tvItemAccepted);
            TextView tvItemVote = (TextView) rootView.findViewById(R.id.tvItemVote);

            TextView tvVotesFor = (TextView) rootView.findViewById(R.id.tvVotesFor);
            TextView tvVotesAgainst = (TextView) rootView.findViewById(R.id.tvVotesAgainst);
            TextView tvVotesRefrained = (TextView) rootView.findViewById(R.id.tvVotesRefrained);

            tvItemId.setText(String.valueOf(position + 1));
            tvItemTitle.setText(data.get(position).getTitle());

            if (data.get(position).getAccepted() == Bill.ACCEPTED) {
                llItemAccepted.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccept));
                tvItemAccepted.setText("Прийнято");
            } else if (data.get(position).getAccepted() == Bill.DECLINED) {
                llItemAccepted.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorDecline));
                tvItemAccepted.setText("Відхилено");
            } else {
                llItemAccepted.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorInQueue));
                tvItemAccepted.setText("На розгляді");
            }

            switch (data.get(position).getChoice()) {
                case Bill.VOTED_FOR:
                    tvItemVote.setTextColor(ContextCompat.getColor(activity, R.color.colorAccept));
                    tvItemVote.setText("Ви - ЗА");
                    break;
                case Bill.VOTED_AGAINST:
                    tvItemVote.setTextColor(Color.RED);
                    tvItemVote.setText("Ви - ПРОТИ");
                    break;
                case Bill.REFRAINED_FROM_VOTING:
                    tvItemVote.setTextColor(Color.GRAY);
                    tvItemVote.setText("Ви - УТРИМАЛИСЯ");
                    break;
                default:
                    tvItemVote.setText("");
                    break;
            }

            if (data.get(position).getVotesFor() > 0
                    || data.get(position).getVotesAgainst() > 0
                    || data.get(position).getVotesRefrained() > 0) {
                tvVotesFor.setText("За: " + data.get(position).getVotesFor());
                tvVotesAgainst.setText("Проти: " + data.get(position).getVotesAgainst());
                tvVotesRefrained.setText("Утрималося: " + data.get(position).getVotesRefrained());
            } else {
                tvVotesFor.setText("");
                tvVotesAgainst.setText("");
                tvVotesRefrained.setText("");
            }

        } catch (Exception e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

}
