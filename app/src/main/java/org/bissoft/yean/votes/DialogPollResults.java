package org.bissoft.yean.votes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bissoft.yean.votes.util.CustomDiagram;

public class DialogPollResults extends DialogFragment {
    TextView tvNumQuestion;
    TextView tvQuestion;

    TextView tvAllRes;
    TextView tvForRes;
    TextView tvRefrRes;
    TextView tvAgRes;
    TextView tvNotVotRes;

    Button buttonClose;
    Button buttonPollResults;

    CustomDiagram imageDiagram;

    LinearLayout linearTitle;
    LinearLayout linearQuestion;
    LinearLayout linearPollResults;
    LinearLayout linearPollResults2;
    LinearLayout linearMarking;


    int numQuestion;
    int allRes;
    int forRes;
    int refrRes;
    int agRes;
    int notVotRes;

    String question;

    public DialogPollResults() {}

    public static DialogPollResults newInstance( ) {
        DialogPollResults frag = new DialogPollResults();
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_poll_results, container);
    }
    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int)(MainActivity.width * 0.9),
                (int)(MainActivity.height * 0.9));
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        allRes = 120;
        forRes = 80;
        refrRes = 20;
        agRes = 10;
        notVotRes = 10;

        imageDiagram = (CustomDiagram) view.findViewById(R.id.customImage_Progress);
        imageDiagram.setParams(forRes, agRes, refrRes, notVotRes);

        tvNumQuestion = (TextView) view.findViewById(R.id.textView_NumQuestion);
        tvQuestion = (TextView) view.findViewById(R.id.textView_Question);
        tvAllRes = (TextView) view.findViewById(R.id.tvAllRes);
        tvForRes = (TextView) view.findViewById(R.id.tvForRes);
        tvRefrRes = (TextView) view.findViewById(R.id.tvRefrRes);
        tvAgRes = (TextView) view.findViewById(R.id.tvAgRes);
        tvNotVotRes = (TextView) view.findViewById(R.id.tvNotVotRes);

        buttonClose = (Button) view.findViewById(R.id.button_Close);

        linearTitle = (LinearLayout) view.findViewById(R.id.linear_Title);
        linearQuestion = (LinearLayout) view.findViewById(R.id.linear_Question);
        linearPollResults = (LinearLayout) view.findViewById(R.id.linear_PollResults);
        linearPollResults2 = (LinearLayout) view.findViewById(R.id.linear_PollResults2);
        linearMarking = (LinearLayout) view.findViewById(R.id.linear_Marking);

//        linearTitle.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, 0);
        linearQuestion.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, 0);
//        linearPollResults.setPadding(MainActivity.height / 10, 0, MainActivity.height / 10, MainActivity.height / 15);
        //  linearMarking.setPadding(MainActivity.height / 50, MainActivity.height / 8, 0, MainActivity.height / 8);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearTitle.getLayoutParams();
        layoutParams.setMargins(0, MainActivity.height / 15, 0, 0);
        linearTitle.setLayoutParams(layoutParams);

        numQuestion = 3;
        question = "Про надання пільг Голяку Роману і присвоєння звання Генерала Збройних Сил України";

        tvNumQuestion.setText("Питання №" + numQuestion);
        tvQuestion.setText(question);
        tvAllRes.setText("Всі - "+ allRes);
        tvForRes.setText("За - "+ forRes);
        tvRefrRes.setText("Утрималось - "+ refrRes);
        tvAgRes.setText("Проти - "+ agRes);
        tvNotVotRes.setText("Не проголосували - "+ notVotRes);

        //   drawRect();

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent(getContext(), MainActivity.class);
                toMain.putExtra("from_dialog", true);
                startActivity(toMain);
             /*   Bundle args = new Bundle();
                args.putBoolean("is_head", false);
                ((MainActivity) getActivity()).changeFragment(new BillListFragment(), args, "bill_list_fragm", "");*/
              /*  BillInfoFragment fragment = (BillInfoFragment) getFragmentManager()
                        .findFragmentByTag("bill_info_fragm");
                fragment.onFinishDialogPollResults();
                dismiss();*/
            }
        });


        buttonPollResults = (Button) view.findViewById(R.id.button_PollResult);
        buttonPollResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent(getContext(), MainActivity.class);
                toMain.putExtra("from_dialog_to_results", true);
                toMain.putExtra("bill_num", getArguments().getInt("bill_num", 0));
                startActivity(toMain);
             /*   Bundle args = new Bundle();
                args.putBoolean("is_head", false);
             //   args.putInt("bill_num", getArguments().getInt("bill_num", 0));
                ((MainActivity) getActivity()).changeFragment(new PollResultsFragment(), args, "poll_results_fragm", "");
                dismiss();*/
            }
        });
    }

    public interface DialogPollResultsListener {
        void onFinishDialogPollResults();
    }

//    public void drawRect(){
//        Paint paintBy = new Paint();
//        Paint  paintAgaint = new Paint();;
//        Paint  paintAbstentions = new Paint();
//
//        Bitmap bitmapBy = Bitmap.createBitmap(14,14, Bitmap.Config.ARGB_8888);
//        Bitmap bitmapAgaint = Bitmap.createBitmap(14,14, Bitmap.Config.ARGB_8888);
//        Bitmap bitmapAbstentions = Bitmap.createBitmap(14,14, Bitmap.Config.ARGB_8888);
//
//        Canvas canvasBy = new Canvas(bitmapBy);
//        Canvas canvasAgaint = new Canvas(bitmapAgaint);
//        Canvas canvasAbstentions = new Canvas(bitmapAbstentions);
//
//        paintBy.setColor(Color.parseColor("#FFFFFF"));
//        canvasBy.drawRect(0, 0, 14, 14, paintBy);
//        paintBy.setColor(Color.GREEN);
//        canvasBy.drawRect(2, 2, 12, 12, paintBy);
//        imageViewBy.setImageBitmap(bitmapBy);
//
//        paintAbstentions.setColor(Color.parseColor("#FFFFFF"));
//        canvasAbstentions.drawRect(0, 0, 14, 14, paintAbstentions);
//        paintAbstentions.setColor(Color.YELLOW);
//        canvasAbstentions.drawRect(2, 2, 12, 12, paintAbstentions);
//        imageViewAbstentions.setImageBitmap(bitmapAbstentions);
//
//        paintAgaint.setColor(Color.parseColor("#FFFFFF"));
//        canvasAgaint.drawRect(0, 0, 14, 14, paintAgaint);
//        paintAgaint.setColor(Color.RED);
//        canvasAgaint.drawRect(2, 2, 12, 12, paintAgaint);
//        imageViewAgaint.setImageBitmap(bitmapAgaint);
//
//    }

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


