package org.bissoft.yean.votes;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A dialog fragment in which the deputy can sign in list of reporters.
 * @author      Nataliia Makarenko
 */
public class ReportDialogFragment  extends DialogFragment {
    /** Current activity. */
    Activity activity;
    /** Whether or not the deputy wishes to be in list of reporters. */
    boolean reporter;

    /**
     * An interface that represents a callback to be invoked
     * when the dialog fragment is cancelled.
     */
    public interface ReportDialogListener {
        void onFinishReportDialog(boolean inReporter);
    }

    /**
     * Called when a fragment is first attached to its context.
     * @param activity activity to which the fragment is attached.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.report_dialog, container, false);

        Button btOk = (Button) rootView.findViewById(R.id.btOk);
        Button btCancel = (Button) rootView.findViewById(R.id.btCancel);
        TextView tvMsg = (TextView) rootView.findViewById(R.id.tvMessage);
        LinearLayout llDialog = (LinearLayout) rootView.findViewById(R.id.llDialog);
        LinearLayout llBtnsDialog = (LinearLayout) rootView.findViewById(R.id.llBtnsDialog);

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.ll_round_all);

        RelativeLayout.LayoutParams layoutParamsrl = new RelativeLayout.LayoutParams(600, 250);
        llDialog.setLayoutParams(layoutParamsrl);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600, 100);
        //    layoutParams.setMargins(10, 0, 10, 0);
        llBtnsDialog.setLayoutParams(layoutParams);
        llBtnsDialog.setGravity(Gravity.BOTTOM);

        Bundle mArgs = getArguments();
        String msg = mArgs.getString("message");
        reporter = mArgs.getBoolean("reporter");

        tvMsg.setText(msg);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillInfoFragment fragment = (BillInfoFragment) getFragmentManager()
                        .findFragmentByTag("bill_info_fragm");
                fragment.onFinishReportDialog(!reporter);
                dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

   /*     getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //Clear the not focusable flag from the window
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                //Update the WindowManager with the new attributes (no nicer way I know of to do this)..
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(getDialog().getWindow().getDecorView(), getDialog().getWindow().getAttributes());
            }
        });
*/
        return rootView;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode, android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    //  dismiss();
                    //  return true;
                }
                // Otherwise, do nothing else
                else return false;
                return false;
            }
        });
    }
}

