package org.bissoft.yean.votes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;


/**
 * A fragment representing a start page after user logged in.
 * @author      Nataliia Makarenko
 */
public class StartSessionFragment extends Fragment implements DialogTimeRegist.DialogTimeRegistListener {
    /** Whether or not the user is head. */
    boolean isHead;

    /** Button to start session. */
    Button btStartSession;
    /** TextView that deputy sees before the head starts the session. */
    TextView tvStartSession;

    TextView tvGreeting;

    ImageView ivCoat;

    /**
     * Constructs a new instance of {@code StartSessionFragment}.
     */
    public StartSessionFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater the LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     *
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start_session, container, false);

        btStartSession = (Button) rootView.findViewById(R.id.btStartSession);
        tvStartSession = (TextView) rootView.findViewById(R.id.tvStartSession);
        tvGreeting = (TextView) rootView.findViewById(R.id.tvGreeting);

        ivCoat = (ImageView) rootView.findViewById(R.id.ivCoat);

        if (((MainActivity) getActivity()).isConnected()) {
            new DownloadImageTask(ivCoat).execute(getArguments().getString("emblem_url", ""));
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        tvGreeting.setText("Вітаємо, шановний "
                        .concat(getArguments().getString("last_name", "")).concat(" ")
                        .concat(getArguments().getString("first_name", "")).concat(" ")
                        .concat(getArguments().getString("second_name", "")).concat("!")
        );

        isHead = getArguments().getBoolean("is_head", false);


        if (isHead) {
            tvStartSession.setVisibility(View.GONE);
        } else {
            //  btStartSession.setVisibility(View.GONE);
        }

        btStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isHead) {
                    Bundle args = new Bundle();
                    args.putBoolean("is_head", isHead);
                    args.putInt("total", Integer.parseInt(getArguments().getString("total")));
                    ((MainActivity) getActivity()).changeFragment(new RegistrationFragment(), args, "registr_fragm", "");
                    ((MainActivity) getActivity()).startSession();
                } else {
                    Bundle args = new Bundle();
                    args.putString("last_name", getArguments().getString("last_name"));
                    args.putString("first_name", getArguments().getString("first_name"));
                    args.putString("second_name", getArguments().getString("second_name"));
                    FragmentManager fragmentManager = getFragmentManager();
                    DialogTimeRegist editNameDialog = DialogTimeRegist.newInstance();
                    editNameDialog.setArguments(args);
                    editNameDialog.setCancelable(false);
                    editNameDialog.show(fragmentManager, "dialog_time_registr");
                }
            }
        });

        return rootView;
    }

    /**
     * Final instructions after exiting from dialog,
     * in which the deputy can register in current session.
     */
    @Override
    public void onFinishDialogTimeRegist(boolean registered) {
        if (registered) {
            Bundle args = new Bundle();
            args.putBoolean("is_head", isHead);
            ((MainActivity) getActivity()).changeFragment(new BillListFragment(), args, "bill_list_fragm", "");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) bmImage.setImageBitmap(result);
        }
    }
}

