package org.bissoft.yean.votes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class DialogTimeRegist extends DialogFragment implements View.OnClickListener {

    TextView tvTitleReg;
    TextView tvSessiaReg;
    TextView tvTimerReg;
    TextView tvResultReg;

    ProgressBar progressReg;

    Button buttonReg;

    LinearLayout linearProgress;

    Thread thread;
    Handler h;

    int numSessia = 45;
    int i;

    boolean redYes = false;

    public DialogTimeRegist() {
    }

    public static DialogTimeRegist newInstance() {
        DialogTimeRegist frag = new DialogTimeRegist();
        return frag;
    }
    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (MainActivity.width * 0.9),
                (int) (MainActivity.height * 0.9));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.dialog_time_regist, container);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);



        h = new ValidHandler(getActivity());

        tvTitleReg = (TextView) view.findViewById(R.id.textView_TitleReg);
        tvSessiaReg = (TextView) view.findViewById(R.id.textView_SessiaReg);
        tvTimerReg = (TextView) view.findViewById(R.id.textView_TimerRegist);
        tvResultReg = (TextView) view.findViewById(R.id.textView_RegResult);
        tvResultReg.setVisibility(View.GONE);
        progressReg = (ProgressBar) view.findViewById(R.id.progressBar_Regist);
        buttonReg = (Button) view.findViewById(R.id.button_Regist);
        buttonReg.setOnClickListener(this);

        linearProgress = (LinearLayout) view.findViewById(R.id.linear_ProgressReg);
        linearProgress.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, 0);

        tvTitleReg.setText("Шановний "
                .concat(getArguments().getString("last_name", "")).concat(" ")
                .concat(getArguments().getString("first_name", "")).concat(" ")
                .concat(getArguments().getString("second_name", "")).concat("!"));

        tvSessiaReg.setText(getResources().getString(R.string.regisr_sessia_head) + numSessia);
        progressReg.setProgress(0);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                int progressStatus = 0;
                int time = MainActivity.REGISTR_TIME;
                for (i = 0; i <= MainActivity.REGISTR_TIME; i++, time--) {
                    if (i == 0)
                        progressReg.setProgress(0);
                    else progressReg.setProgress(progressStatus += 300 / MainActivity.REGISTR_TIME);
                    Message msg = new Message();
                    Bundle data = new Bundle();


                    try {

                        data.putInt("timer", time);
                        msg.setData(data);
                        h.sendMessage(msg);

                        if (i == MainActivity.REGISTR_TIME) {
                            Message msg2 = new Message();
                            Bundle data2 = new Bundle();
                            data2.putBoolean("threadEnd", true);
                            msg.setData(data2);
                            h.sendMessage(msg2);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

            }

        };
        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onClick(View v) {
        redYes = true;
        buttonReg.setVisibility(View.GONE);
        tvResultReg.setVisibility(View.VISIBLE);
        tvResultReg.setText(getResources().getString(R.string.regisr_result_yes));

    }
    class ValidHandler extends Handler {
        WeakReference<Activity> wrActivity;

        public ValidHandler(Activity activity) {
            wrActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Activity activity = wrActivity.get();
            if (activity != null) {
                Bundle b = msg.getData();
                int time = b.getInt("timer");
                if (time > 9)
                    tvTimerReg.setText("00:" + time);
                else tvTimerReg.setText("00:0" + time);
                if (b.getBoolean("threadEnd")) {
                    if (!redYes) {
                        buttonReg.setVisibility(View.GONE);
                        tvResultReg.setVisibility(View.VISIBLE);
                        tvResultReg.setText(getResources().getString(R.string.regisr_result_no));

                        StartSessionFragment fragment = (StartSessionFragment) getFragmentManager()
                                .findFragmentByTag("start_session_fragm");
                        if (fragment != null) {
                            fragment.onFinishDialogTimeRegist(false);
                        } else {
                            Intent toMain = new Intent(getContext(), MainActivity.class);
                            startActivity(toMain);
                        }
                        dismiss();
                    } else {
                        StartSessionFragment fragment = (StartSessionFragment) getFragmentManager()
                                .findFragmentByTag("start_session_fragm");
                        if (fragment != null) fragment.onFinishDialogTimeRegist(true);
                        dismiss();
                    }
                }
            }
        }
    }

    public interface DialogTimeRegistListener {
        void onFinishDialogTimeRegist(boolean registered);
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode, android.view.KeyEvent event) {
                return false;
            }
        });
    }
}

