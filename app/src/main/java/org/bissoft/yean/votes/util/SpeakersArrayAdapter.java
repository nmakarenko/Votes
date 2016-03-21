package org.bissoft.yean.votes.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bissoft.yean.votes.BillInfoDiscussFragment;
import org.bissoft.yean.votes.R;

import java.util.ArrayList;

/**
 * An adapter that is used in ListView with list of speakers.
 * @author      Nataliia Makarenko
 */
public class SpeakersArrayAdapter extends BaseAdapter implements View.OnClickListener{
    /** List of speakers. */
    public ArrayList<String> data;
    /** Current activity. */
    private Activity activity;
    /** Current fragment. */
    private BillInfoDiscussFragment fragment;

    /** Whether or not the kind of report was chosen. */
    public static boolean reportChosen;
    public static boolean speakerChosen;
    public int speakerPosition = -1;

    /**
     * Constructs a new instance of {@code SpeakersArrayAdapter}.
     *
     * @param activity current activity on which this adapter constructs the list of speakers.
     * @param data list of speakers.
     * @param fragment current fragment on which this adapter constructs the list of speakers.
     */
    public SpeakersArrayAdapter(Activity activity, ArrayList<String> data, BillInfoDiscussFragment fragment) {
        this.activity = activity;
        this.data = data;
        this.fragment = fragment;

    }

    /**
     * Gets number of elements in list.
     *
     * @return number of elements in list.
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
            rootView = inflater.inflate(R.layout.speaker_list_item, parent, false);
        } else {
            rootView = convertView;
        }

        LinearLayout llSpeakerName = (LinearLayout) rootView.findViewById(R.id.llSpeakerName);
        TextView tvSpeakerName = (TextView) rootView.findViewById(R.id.tvSpeakerName);

        final LinearLayout llSpeak = (LinearLayout) rootView.findViewById(R.id.llSpeak);
        final RelativeLayout rlSpeak = (RelativeLayout) rootView.findViewById(R.id.rlSpeak);
        final Button btStopSpeak = (Button) rootView.findViewById(R.id.btStopSpeak);
        Button btGiveFloor = (Button) rootView.findViewById(R.id.btGiveFloor);

        ImageView ivTimeSpeakingUp = (ImageView) rootView.findViewById(R.id.ivTimeSpeakingUp);
        ImageView ivTimeSpeakingDown = (ImageView) rootView.findViewById(R.id.ivTimeSpeakingDown);
        final EditText etTimeSpeaking = (EditText) rootView.findViewById(R.id.etTimeSpeaking);

        final TextView tvReport = (TextView) rootView.findViewById(R.id.tvReport);
        final TextView tvCoReport = (TextView) rootView.findViewById(R.id.tvCoReport);
        final TextView tvAppearance = (TextView) rootView.findViewById(R.id.tvAppearance);

        tvSpeakerName.setText(data.get(position));
        if (speakerChosen) {
            if (position == speakerPosition) {
                rlSpeak.setVisibility(View.VISIBLE);
            } else {
                rlSpeak.setVisibility(View.INVISIBLE);
            }
        } else {
            rlSpeak.setVisibility(View.INVISIBLE);
        }

        llSpeakerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakerChosen = true;
          //      if (position == 0) {
                speakerPosition = position;
                    rlSpeak.setVisibility(View.VISIBLE);
                    tvReport.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvCoReport.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvAppearance.setBackgroundColor(Color.parseColor("#ffffff"));
                notifyDataSetChanged();
          //      }
            }
        });

        btGiveFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportChosen) {
                    rlSpeak.setVisibility(View.VISIBLE);
                    llSpeak.setVisibility(View.INVISIBLE);
                    btStopSpeak.setVisibility(View.VISIBLE);

                    fragment.startSpeaking(Integer.parseInt(etTimeSpeaking.getText().toString()));
                    //   notifyDataSetChanged();
                }
            }
        });

        btStopSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSpeak.setVisibility(View.INVISIBLE);
                llSpeak.setVisibility(View.VISIBLE);
                btStopSpeak.setVisibility(View.INVISIBLE);

                fragment.stopSpeaking();

                data.remove(speakerPosition);
                notifyDataSetChanged();
            }
        });

        ivTimeSpeakingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(etTimeSpeaking.getText().toString()) < 58)
                    etTimeSpeaking.setText(String.valueOf(Integer.parseInt(etTimeSpeaking.getText().toString()) + 1));
            }
        });

        ivTimeSpeakingDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(etTimeSpeaking.getText().toString()) > 1)
                    etTimeSpeaking.setText(String.valueOf(Integer.parseInt(etTimeSpeaking.getText().toString()) - 1));
            }
        });

        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportChosen = true;
                tvReport.setBackgroundColor(Color.parseColor("#00ff00"));
                tvCoReport.setBackgroundColor(Color.parseColor("#ffffff"));
                tvAppearance.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        tvCoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportChosen = true;
                tvReport.setBackgroundColor(Color.parseColor("#ffffff"));
                tvCoReport.setBackgroundColor(Color.parseColor("#00ff00"));
                tvAppearance.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        tvAppearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportChosen = true;
                tvReport.setBackgroundColor(Color.parseColor("#ffffff"));
                tvCoReport.setBackgroundColor(Color.parseColor("#ffffff"));
                tvAppearance.setBackgroundColor(Color.parseColor("#00ff00"));
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSpeakerName:
                break;
            case R.id.btGiveFloor:
                break;
            case R.id.btStopSpeak:
                break;
            case R.id.ivTimeSpeakingUp:
                break;
            case R.id.ivTimeSpeakingDown:
                break;
            case R.id.tvReport:
                break;
            case R.id.tvCoReport:
                break;
            case R.id.tvAppearance:
                break;
        }
    }
}
