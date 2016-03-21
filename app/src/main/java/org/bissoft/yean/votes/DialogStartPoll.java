package org.bissoft.yean.votes;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class DialogStartPoll extends DialogFragment implements View.OnClickListener {

    TextView tvNumSesion;
    TextView tvDataSesion;
    TextView tvNameRada;
    TextView tvRegionRada;
    TextView tvNumQuestion;
    TextView tvQuestion;
    TextView tvTimer;
    TextView tvPoll;
    TextView tvPollResult;


    ProgressBar progreeBar;

    LinearLayout linearYes;
    LinearLayout linearRefrain;
    LinearLayout linearNo;
    LinearLayout linearTitle;
    LinearLayout linearQuestion;
    LinearLayout linearProgress;
    LinearLayout linearButtons;

    Thread thread;
    Handler h;

    Button buttonYes;
    Button buttonNo;
    Button buttonOk;

    int i = 0;
    int numQuestion;

    boolean startSession = false;

    boolean answered = false;

    String question;
    public static String NUM_QUESTION = "NUM_QUESTION";
    public static String QUESTION = "QUESTION";

    public DialogStartPoll() {
    }

    public static DialogStartPoll newInstance() {
        DialogStartPoll frag = new DialogStartPoll();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.dialog_start_poll, container);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (MainActivity.width * 0.9),
                (int) (MainActivity.height * 0.9));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);


        h = new ValidHandler(getActivity());

        tvPoll = (TextView) view.findViewById(R.id.textView_Poll);
        tvPoll.setVisibility(View.GONE);
        tvPollResult = (TextView) view.findViewById(R.id.textView_PollResult);
        tvPollResult.setVisibility(View.GONE);
        tvTimer = (TextView) view.findViewById(R.id.textView_Timer);

        tvNumSesion = (TextView) view.findViewById(R.id.textView_NumSession);
        tvDataSesion = (TextView) view.findViewById(R.id.textView_DateSession);
        tvNameRada = (TextView) view.findViewById(R.id.textView1);
        tvRegionRada = (TextView) view.findViewById(R.id.textView2);
        tvNumQuestion = (TextView) view.findViewById(R.id.textView_NumQuestion);
        tvQuestion = (TextView) view.findViewById(R.id.textView_Question);

        progreeBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progreeBar.setProgress(0);

        linearYes = (LinearLayout) view.findViewById(R.id.linear_ButtonYes);
        linearRefrain = (LinearLayout) view.findViewById(R.id.linear_ButtonRefrain);
        linearNo = (LinearLayout) view.findViewById(R.id.linear_ButtonNo);
        linearYes.setOnClickListener(this);
        linearRefrain.setOnClickListener(this);
        linearNo.setOnClickListener(this);

        linearTitle = (LinearLayout) view.findViewById(R.id.linear_Title);
        linearQuestion = (LinearLayout) view.findViewById(R.id.linear_Question);
        linearProgress = (LinearLayout) view.findViewById(R.id.linear_Progress);
        linearButtons = (LinearLayout) view.findViewById(R.id.linear_Buttons);

        linearTitle.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, 0);
        linearQuestion.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, 0);
        linearProgress.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, 0);
        linearButtons.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, MainActivity.height / 15);

        LinearLayout.LayoutParams paramsButtonYes = (LinearLayout.LayoutParams) linearYes.getLayoutParams();
        paramsButtonYes.setMargins(0, 0, (MainActivity.height / 20), 0);
        linearYes.setLayoutParams(paramsButtonYes);
        LinearLayout.LayoutParams paramsButtonRefrain = (LinearLayout.LayoutParams) linearRefrain.getLayoutParams();
        paramsButtonRefrain.setMargins((MainActivity.height / 40), 0, (MainActivity.height / 40), 0);
        linearRefrain.setLayoutParams(paramsButtonRefrain);
        LinearLayout.LayoutParams paramsButtonNo = (LinearLayout.LayoutParams) linearNo.getLayoutParams();
        paramsButtonNo.setMargins((MainActivity.height / 20), 0, 0, 0);
        linearNo.setLayoutParams(paramsButtonNo);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearTitle.getLayoutParams();
        layoutParams.setMargins(0, MainActivity.height / 15, 0, 0);
        linearTitle.setLayoutParams(layoutParams);

        buttonYes = (Button) view.findViewById(R.id.buttonYes);
        buttonOk = (Button) view.findViewById(R.id.buttonOk);
        buttonNo = (Button) view.findViewById(R.id.buttonNo);
        buttonYes.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonYes.setVisibility(View.GONE);
        buttonOk.setVisibility(View.GONE);
        buttonNo.setVisibility(View.GONE);

        numQuestion = 3;
        question = "Про надання пільг Голяку Роману і присвоєння звання Генерала Збройних Сил України";

        tvNumQuestion.setText("Питання №" + numQuestion);
        tvQuestion.setText(question);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                startSession = true;
                int progressStatus = 0;
                int time = MainActivity.POLL_TIME;
                for (i = 0; i <= MainActivity.POLL_TIME; i++, time--) {
                    if (i == 0)
                        progreeBar.setProgress(0);
                    else progreeBar.setProgress(progressStatus += 150 / MainActivity.POLL_TIME);
                    Message msg = new Message();
                    Bundle data = new Bundle();


                    try {

                        data.putInt("timer", time);
                        msg.setData(data);
                        h.sendMessage(msg);

                        if (i == MainActivity.POLL_TIME) {
                            Message msg2 = new Message();
                            Bundle data2 = new Bundle();
                            data2.putBoolean("threadEnd", true);
                            startSession = false;
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
        if (startSession) {
            switch (v.getId()) {
                case R.id.linear_ButtonYes:
                    tvPoll.setVisibility(View.VISIBLE);
                    tvPollResult.setVisibility(View.VISIBLE);
                    tvPollResult.setText(getContext().getResources().getString(R.string.by2));
                    showButtons();
                    break;
                case R.id.linear_ButtonRefrain:
                    tvPoll.setVisibility(View.VISIBLE);
                    tvPollResult.setVisibility(View.VISIBLE);
                    tvPollResult.setText(getContext().getResources().getString(R.string.abstentions2));
                    showButtons();
                    break;
                case R.id.linear_ButtonNo:
                    tvPoll.setVisibility(View.VISIBLE);
                    tvPollResult.setVisibility(View.VISIBLE);
                    tvPollResult.setText(getContext().getResources().getString(R.string.against2));
                    showButtons();
                    break;
                case R.id.buttonYes:
                    // showDialog();
                    answered = true;
                    buttonYes.setVisibility(View.GONE);
                    buttonNo.setVisibility(View.GONE);
                    tvPoll.setText("Ви проголосували");
                    break;
                case R.id.buttonNo:
                    tvPoll.setVisibility(View.GONE);
                    tvPollResult.setVisibility(View.GONE);
                    buttonYes.setVisibility(View.GONE);
                    buttonNo.setVisibility(View.GONE);
                    linearYes.setVisibility(View.VISIBLE);
                    linearRefrain.setVisibility(View.VISIBLE);
                    linearNo.setVisibility(View.VISIBLE);
                    break;
            }
        }
        if (v.getId() == R.id.buttonOk) {
            showDialog();
            dismiss();
        }
    }

    public void showButtons() {
        linearYes.setVisibility(View.GONE);
        linearRefrain.setVisibility(View.GONE);
        linearNo.setVisibility(View.GONE);
        buttonYes.setVisibility(View.VISIBLE);
        buttonNo.setVisibility(View.VISIBLE);
    }

    public void showButtonOk() {
        tvPollResult.setVisibility(View.VISIBLE);
        tvPollResult.setText(getContext().getResources().getString(R.string.no_poll));
        linearYes.setVisibility(View.GONE);
        linearRefrain.setVisibility(View.GONE);
        linearNo.setVisibility(View.GONE);
        buttonYes.setVisibility(View.GONE);
        buttonOk.setVisibility(View.VISIBLE);
        buttonNo.setVisibility(View.GONE);
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
                    tvTimer.setText("00:" + time);
                else tvTimer.setText("00:0" + time);
                if (b.getBoolean("threadEnd")) {
                    if (answered) {
                        showDialog();
                        dismiss();
                    }
                    else showButtonOk();
                }
            }
        }
    }

    void showDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog_start_poll");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogPollResults newFragment = DialogPollResults.newInstance();
        newFragment.setCancelable(false);
        newFragment.setArguments(getArguments());
        newFragment.show(ft, "dialog_poll_results");
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

