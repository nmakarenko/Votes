package org.bissoft.yean.votes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bissoft.yean.votes.util.CustomDiagram;
import org.bissoft.yean.votes.util.LoadedImage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class HeadPollFragment extends Fragment implements View.OnClickListener {

    LinearLayout linearStartPoll;
    LinearLayout linearPoll;
    LinearLayout linearPollResult;
    LinearLayout linearPollRestart;

    LinearLayout linearButtonYes;
    LinearLayout linearButtonRefrain;
    LinearLayout linearButtonNo;
    LinearLayout linearButtonCancel;

    LinearLayout linearButtonsPoll;
    LinearLayout linearButtonsResult;

    Button buttonBasis;
    Button buttonGeneral;
    Button buttonBasisGeneral;
    Button button1_2All;
    Button button2_3All;
    Button button1_2Presant;
    Button button1_3All;
    Button buttonStartPoll;
    Button buttonStartSignalPoll;

    TextView tvPollTitle;
    TextView tvPollQuestion;
    TextView tvPollQuestion2;
    TextView tvPollTimer;
    TextView tvPollResult;
    TextView tvPoll;

    ProgressBar progressBarPollHead;

    Button buttonYes;
    Button buttonRefrain;
    Button buttonNo;
    Button buttonCancel;
    Button buttonConfirm;
    Button buttonNoConfirm;
    Button buttonOk;

    CustomDiagram diagramHeadPoll;

    TextView tvPollResult2;
    Button buttonPollRestart;
    Button buttonPollProtocol;
    Button buttonPollRestart2;

    Handler h;
    Thread thread;
    Runnable runnable;
    GridView gridPhotos;

    ImageAdapter imageAdapter;
    Bitmap bitmap = null;
    Bitmap newBitmap  = null;

    LoadPhoto loadImages;
    LoadedImage[] photos = null;

    ImageView imageHaid;

    String question;
    String pollTitle="";

    int pollType = 0;
    int resultType = 0;
    volatile int i = 0;

    int num = 120;
    int hA;
    int wA;
    int heightGrid;
    int widthGrid;
//    int ii;
//    int numSessia = 45;
//    int resultAll = 120;
//    int resultReg = 80;
//    int resultEps = 40;

    double a;

    boolean startSession = false;
    boolean pollOk = false;
    boolean pollCancel = false;
    volatile boolean stopThread = false;

    public static boolean isVoting = false;

    int y;
    int headChoose = 0;


    public HeadPollFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_head_poll, container, false);

        h = new ValidHandler(getActivity());

        gridPhotos = (GridView) rootView.findViewById(R.id.grid);
        imageAdapter = new ImageAdapter(getContext());
        gridPhotos.setAdapter(imageAdapter);
        imageHaid = (ImageView) rootView.findViewById(R.id.imageView_Haid);

        LinearLayout llHead = (LinearLayout) rootView.findViewById(R.id.linear_Haid_Icon);
        LinearLayout llGrid = (LinearLayout) rootView.findViewById(R.id.llGrid);

        num = 134;
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        double coefWH = ((double) MainActivity.width * 3 / 5) / (MainActivity.height * 8 / 9 * 7 / 9);
        double coefWHIm = ((double) bitmap1.getHeight()) / bitmap1.getWidth();
        double coef = coefWH * coefWHIm;
        double y1 = (coef + Math.sqrt(coef * coef + 4 * num * coef)) / (2 * coef);
        y = (int) (y1);
        if (y1 - y > 0) y++;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                MainActivity.width * 3 / 5, (MainActivity.height * 8 / 9 * 7 / 9) / y
        );
        llHead.setLayoutParams(layoutParams);
        layoutParams = new LinearLayout.LayoutParams(
                MainActivity.width * 3 / 5, (MainActivity.height * 8 / 9 * 7 / 9) * (y - 1) / y
        );
        llGrid.setLayoutParams(layoutParams);

        setupViews();
        loadImages();


        linearStartPoll = (LinearLayout) rootView.findViewById(R.id.linear_Start_Poll);
        linearPoll = (LinearLayout) rootView.findViewById(R.id.linear_Poll);
        linearPollResult = (LinearLayout) rootView.findViewById(R.id.linear_Head_Poll_Result);
        linearPollRestart = (LinearLayout) rootView.findViewById(R.id.linear_PollCancel);

        linearPoll.setVisibility(View.GONE);
        linearPollResult.setVisibility(View.GONE);
        linearPollRestart.setVisibility(View.GONE);

        buttonBasis = (Button) rootView.findViewById(R.id.button_basis);
        buttonGeneral = (Button) rootView.findViewById(R.id.button_general);
        buttonBasisGeneral = (Button) rootView.findViewById(R.id.button_basis_general);
        buttonStartPoll = (Button) rootView.findViewById(R.id.button_start_poll);
        buttonStartSignalPoll = (Button) rootView.findViewById(R.id.button_signal_poll);
        button1_2All = (Button) rootView.findViewById(R.id.button_1_2All);
        button2_3All = (Button) rootView.findViewById(R.id.button_2_3All);
        button1_2Presant = (Button) rootView.findViewById(R.id.button_1_2Presant);
        button1_3All = (Button) rootView.findViewById(R.id.button_1_3All);
        buttonBasis.setOnClickListener(this);
        buttonGeneral.setOnClickListener(this);
        buttonBasisGeneral.setOnClickListener(this);
        buttonStartPoll.setOnClickListener(this);
        buttonStartSignalPoll.setOnClickListener(this);
        button1_2All.setOnClickListener(this);
        button2_3All.setOnClickListener(this);
        button1_2Presant.setOnClickListener(this);
        button1_3All.setOnClickListener(this);
        buttonBasis.setMinimumHeight(100);
        buttonGeneral.setMinimumHeight(100);
        buttonBasisGeneral.setMinimumHeight(100);
        buttonStartPoll.setMinimumHeight(100);
        buttonStartSignalPoll.setMinimumHeight(100);

        tvPollTitle = (TextView) rootView.findViewById(R.id.textView_poll_title);
        tvPollQuestion = (TextView) rootView.findViewById(R.id.textView_poll_question);
        tvPollTimer = (TextView) rootView.findViewById(R.id.textView_poll_timer);

        question = "Про надання пільг Голяку Роману і присвоєння звання Генерала Збройних Сил України";
        tvPollTitle.setPadding(MainActivity.height / 30, 0, MainActivity.height / 30, 0);
        tvPollQuestion.setPadding(MainActivity.height / 20, MainActivity.height / 100, MainActivity.height / 20, 0);
        tvPollQuestion.setText(question);
        tvPollTimer.setPadding(0, MainActivity.height / 100, 0, 0);

        progressBarPollHead = (ProgressBar) rootView.findViewById(R.id.progressBar_Poll_Head);
        progressBarPollHead.setPadding(MainActivity.height / 20, MainActivity.height / 100, MainActivity.height / 20, 0);



        linearButtonYes = (LinearLayout) rootView.findViewById(R.id.linear_ButtonYes);
        linearButtonRefrain = (LinearLayout) rootView.findViewById(R.id.linear_ButtonRefrain);
        linearButtonNo = (LinearLayout) rootView.findViewById(R.id.linear_ButtonNo);
        linearButtonCancel = (LinearLayout) rootView.findViewById(R.id.linear_ButtonCancel);
        linearButtonYes.setPadding(MainActivity.height / 20, MainActivity.height / 50, MainActivity.height / 20, MainActivity.height / 50);
        linearButtonRefrain.setPadding(MainActivity.height / 20, MainActivity.height / 50, MainActivity.height / 20, MainActivity.height / 50);
        linearButtonNo.setPadding(MainActivity.height / 20, MainActivity.height / 50, MainActivity.height / 20, MainActivity.height / 50);
        linearButtonCancel.setPadding(MainActivity.height / 20, MainActivity.height / 50, MainActivity.height / 20, MainActivity.height / 50);

        buttonYes = (Button) rootView.findViewById(R.id.button_head_yes);
        buttonRefrain = (Button) rootView.findViewById(R.id.button_head_refrain);
        buttonNo = (Button) rootView.findViewById(R.id.button_head_no);
        buttonCancel = (Button) rootView.findViewById(R.id.button_head_poll_cancel);
        buttonYes.setOnClickListener(this);
        buttonRefrain.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        tvPoll = (TextView) rootView.findViewById(R.id.textView_Poll);
        tvPoll.setVisibility(View.GONE);
        tvPollResult = (TextView) rootView.findViewById(R.id.textView_PollResult);
        tvPollResult.setVisibility(View.GONE);

        linearButtonsPoll = (LinearLayout) rootView.findViewById(R.id.linear_Buttons_Poll);
        linearButtonsResult = (LinearLayout) rootView.findViewById(R.id.linear_Buttons_Result);
        linearButtonsResult.setVisibility(View.GONE);

        buttonConfirm = (Button) rootView.findViewById(R.id.buttonYes);
        buttonOk = (Button) rootView.findViewById(R.id.buttonOk);
        buttonNoConfirm = (Button) rootView.findViewById(R.id.buttonNo);
        buttonConfirm.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        buttonNoConfirm.setOnClickListener(this);
        buttonConfirm.setVisibility(View.GONE);
        buttonOk.setVisibility(View.GONE);
        buttonNoConfirm.setVisibility(View.GONE);
        buttonConfirm.setMinimumHeight(100);
        buttonOk.setMinimumHeight(100);
        buttonNoConfirm.setMinimumHeight(100);
        buttonConfirm.setMinimumWidth(200);
        buttonOk.setMinimumWidth(200);
        buttonNoConfirm.setMinimumWidth(200);

        tvPollQuestion2 = (TextView) rootView.findViewById(R.id.textView_poll_question2);
        tvPollQuestion2.setPadding(MainActivity.height / 20, MainActivity.height / 100, MainActivity.height / 20, 0);
        tvPollQuestion2.setText(question);

        tvPollResult2 = (TextView) rootView.findViewById(R.id.textView_PollResult2);
        buttonPollRestart = (Button) rootView.findViewById(R.id.button_PollRestart);
        buttonPollProtocol = (Button) rootView.findViewById(R.id.button_PollProtocol);
        buttonPollRestart2 = (Button) rootView.findViewById(R.id.button_PollRestart2);

        diagramHeadPoll = (CustomDiagram) rootView.findViewById(R.id.customImage_Progress_Head);
        diagramHeadPoll.setParams(80, 40, 20);

        buttonPollRestart.setOnClickListener(this);
        buttonPollProtocol.setOnClickListener(this);
        buttonPollRestart2.setOnClickListener(this);

        runnable = new Runnable() {
            @Override
            public void run() {
                if(!stopThread) {
                    isVoting = true;
                    startSession = true;
                    int progressStatus = 0;
                    int time = MainActivity.POLL_TIME;
                    for (i = 0; i <= MainActivity.POLL_TIME; i++, time--) {
                        if (i == 0)
                            progressBarPollHead.setProgress(0);
                        else progressBarPollHead.setProgress(progressStatus += 150 / MainActivity.POLL_TIME);
                        Message msg = new Message();
                        Bundle data = new Bundle();

                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!stopThread)
                                        imageAdapter.notifyDataSetChanged();
                                }
                            });
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
            }
        };


        return rootView;
    }
    private void setupViews() {
        heightGrid = (MainActivity.height * 8 / 9 * 7 / 9) * (y - 1) / y;
        widthGrid = MainActivity.width * 3 / 5;
        wA = y - 1;
        if (num % (y - 1) == 0) {
            hA = num / (y - 1);
        } else {
            hA = num / (y - 2);
        }
        a = MainActivity.height * 8 / 9 * 7 / 9 / y;
        gridPhotos.setNumColumns(hA);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        newBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (a - 8 * MainActivity.density),
                (int) (a - 8 * MainActivity.density), true);
        imageHaid.setImageBitmap(newBitmap);
     //   imageHaid.setBackgroundColor(Color.GREEN);
        gridPhotos.setClipToPadding(true);

     /*   heightGrid = MainActivity.height * 56 / 81 * 8 / 9;
        widthGrid = MainActivity.width * 3 / 5;
        double s = heightGrid * widthGrid;
        a = Math.sqrt(s / num);
        wA = (int)(heightGrid / a);
        hA = (int)(num / wA);
        if((wA * hA)< num) hA +=1;

        gridPhotos.setNumColumns(hA);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        newBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (a - 4 * MainActivity.density),
                (int) (a - 4 * MainActivity.density), true);
        imageHaid.setImageBitmap(newBitmap);
        gridPhotos.setClipToPadding(true);
*/

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_basis :
                v.setBackgroundColor(Color.GREEN);
                buttonGeneral.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                buttonBasisGeneral.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                pollType = 1;
                break;
            case R.id.button_general :
                v.setBackgroundColor(Color.GREEN);
                buttonBasis.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                buttonBasisGeneral.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                pollType = 2;
                break;
            case R.id.button_basis_general :
                v.setBackgroundColor(Color.GREEN);
                buttonGeneral.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                buttonBasis.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                pollType = 3;
                break;
            case R.id.button_start_poll :
                if(pollType != 0 && resultType != 0){
                    linearPoll.setVisibility(View.VISIBLE);
                    linearButtonsPoll.setVisibility(View.VISIBLE);
                    linearButtonsResult.setVisibility(View.GONE);
                    linearPollResult.setVisibility(View.GONE);
                    buttonOk.setVisibility(View.GONE);
                    linearStartPoll.setVisibility(View.GONE);
                    linearPollRestart.setVisibility(View.GONE);
                    tvPollResult.setVisibility(View.GONE);
                    tvPoll.setVisibility(View.GONE);
                    tvPoll.setText("ВИ ГОЛОСУЄТЕ");
                    stopThread = false;
                    pollCancel = false;
                    thread = new Thread(runnable);
                    thread.start();
                    ((MainActivity) getActivity()).startPoll();
                }
                break;
            case R.id.button_signal_poll :
//                if(pollType != 0 && resultType != 0){
//                    linearPoll.setVisibility(View.VISIBLE);
//                    linearPollResult.setVisibility(View.GONE);
//                    linearStartPoll.setVisibility(View.GONE);
//                    linearButtonsPoll.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.button_PollProtocol:
                Bundle args = new Bundle();
                args.putBoolean("is_head", true);
                args.putInt("bill_num", getArguments().getInt("bill_num", 0));
                ((MainActivity) getActivity()).changeFragment(new PollResultsFragment(), args,
                        "poll_results_fragm", "bill_list_fragm");
                break;
            case R.id.button_1_2All :
                resultType = 1;
                v.setBackgroundColor(Color.GREEN);
                button2_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2Presant.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                break;
            case R.id.button_2_3All :
                resultType = 2;
                v.setBackgroundColor(Color.GREEN);
                button1_2All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2Presant.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                break;
            case R.id.button_1_2Presant :
                resultType = 3;
                v.setBackgroundColor(Color.GREEN);
                button2_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                break;
            case R.id.button_1_3All :
                resultType = 4;
                v.setBackgroundColor(Color.GREEN);
                button2_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2Presant.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                break;
            case R.id.button_PollRestart :
                gridPhotos.invalidateViews();
                gridPhotos.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
            case R.id.button_PollRestart2 :
                i = MainActivity.POLL_TIME;
                resultType = 0;
                progressBarPollHead.setProgress(0);
                pollType = 0;
                pollOk = false;
                buttonBasis.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                buttonGeneral.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                buttonBasisGeneral.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button2_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_2Presant.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                button1_3All.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                linearPoll.setVisibility(View.GONE);
                linearPollResult.setVisibility(View.GONE);
                linearStartPoll.setVisibility(View.VISIBLE);
                linearPollRestart.setVisibility(View.GONE);
                break;

        }
        if (startSession) {
            switch (v.getId()) {
                case R.id.button_head_yes :
                    headChoose = 1;
                    showButtons();
                    tvPollResult.setText(getContext().getResources().getString(R.string.by2));
                    break;
                case R.id.button_head_refrain:
                    headChoose = 2;
                    showButtons();
                    tvPollResult.setText(getContext().getResources().getString(R.string.abstentions2));
                    break;
                case R.id.button_head_no :
                    headChoose = 3;
                    showButtons();
                    tvPollResult.setText(getContext().getResources().getString(R.string.against2));
                    break;
                case R.id.button_head_poll_cancel :
                    headChoose = 0;
                    pollCancel = true;
                    showButtons();
                    tvPoll.setVisibility(View.GONE);
                    tvPollResult.setText(getContext().getResources().getString(R.string.cancel_poll));
                    tvPollResult.setGravity(Gravity.CENTER_HORIZONTAL);
                    break;
                case R.id.buttonYes:
                    if (headChoose == 0) {
                        imageHaid.setBackgroundColor(Color.WHITE);
                    } else if (headChoose == 1) {
                        imageHaid.setBackgroundColor(Color.GREEN);
                    } else if (headChoose == 2) {
                        imageHaid.setBackgroundColor(Color.YELLOW);
                    } else if (headChoose == 3) {
                        imageHaid.setBackgroundColor(Color.RED);
                    }
                    pollOk = true;
                    tvPoll.setText("ВИ ПРОГОЛОСУВАЛИ");
                    buttonConfirm.setVisibility(View.GONE);
                    buttonNoConfirm.setVisibility(View.GONE);
                    if (pollCancel){
                        isVoting = false;
                        stopThread = true;
                        linearPoll.setVisibility(View.GONE);
                        linearPollResult.setVisibility(View.GONE);
                        linearStartPoll.setVisibility(View.GONE);
                        linearPollRestart.setVisibility(View.VISIBLE);
                        gridPhotos.invalidateViews();
                        gridPhotos.setAdapter(imageAdapter);
                        imageAdapter.notifyDataSetChanged();
                    }

                    break;
                case R.id.buttonNo:
                    pollCancel = false;
                    pollOk = false;
                    linearButtonsPoll.setVisibility(View.VISIBLE);
                    linearButtonsResult.setVisibility(View.GONE);
                    break;
            }
        }
        if(v.getId() == R.id.buttonOk){
            if(!pollCancel) {
                linearPoll.setVisibility(View.GONE);
                linearPollResult.setVisibility(View.VISIBLE);
                linearStartPoll.setVisibility(View.GONE);
            }
        }
        switch (pollType){
            case 1 :
                pollTitle = "За основу";
                break;
            case 2 :
                pollTitle = "Вцілому";
                break;
            case 3 :
                pollTitle = "За основу / В цілому";
                break;
        }
        tvPollTitle.setText("Голосування " + pollTitle);
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
                    tvPollTimer.setText("00:" + time);
                else tvPollTimer.setText("00:0" + time);
                if (b.getBoolean("threadEnd") && !stopThread ) {
                    isVoting = false;
                    if(!pollOk) {
                        pollCancel = false;
                        showButtonOk();
                    }else {
                        linearPoll.setVisibility(View.GONE);
                        linearPollResult.setVisibility(View.VISIBLE);
                        linearStartPoll.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
    public void showButtons() {
        linearButtonsPoll.setVisibility(View.GONE);
        linearButtonsResult.setVisibility(View.VISIBLE);
        buttonConfirm.setVisibility(View.VISIBLE);
        buttonNoConfirm.setVisibility(View.VISIBLE);
        tvPoll.setVisibility(View.VISIBLE);
        tvPollResult.setVisibility(View.VISIBLE);
    }
    public void showButtonOk() {
        tvPollResult.setVisibility(View.VISIBLE);
        tvPollResult.setText(getContext().getResources().getString(R.string.no_poll));
        linearButtonsPoll.setVisibility(View.GONE);
        linearButtonsResult.setVisibility(View.VISIBLE);
        buttonConfirm.setVisibility(View.GONE);
        buttonOk.setVisibility(View.VISIBLE);
        buttonNoConfirm.setVisibility(View.GONE);
    }
    public void loadImages() {

        final Object data =  getActivity().getLastNonConfigurationInstance();
        if (data == null) {

            loadImages =   (LoadPhoto)   new LoadPhoto().execute();
        } else {
            photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                loadImages =   (LoadPhoto) new LoadPhoto().execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }
    public void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            imageAdapter.addPhoto(image);
            imageAdapter.notifyDataSetChanged();
        }
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public void addPhoto(LoadedImage photo) {
            photos.add(photo);
        }

        public int getCount() {
            return photos.size();
        }

        public Object getItem(int position) {
            return photos.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout relativeLayout = null;
            ImageView imageView = null;
            if (convertView == null) {
                relativeLayout = new RelativeLayout(mContext);
                relativeLayout.setPadding(
                        (int) (4 * MainActivity.density),
                        (int) (4 * MainActivity.density),
                        (int) (4 * MainActivity.density),
                        (int) (4 * MainActivity.density));
                imageView = new ImageView(mContext);
                relativeLayout.addView(imageView);
            } else {
                relativeLayout = (RelativeLayout) convertView;
                imageView = (ImageView) relativeLayout.getChildAt(0);
            }

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Random random = new Random();
            if(startSession && !stopThread) {
                switch (random.nextInt(100)) {
                    case 1:
                        imageView.setBackgroundColor(Color.GREEN);
                        break;
                    case 2:
                        imageView.setBackgroundColor(Color.RED);
                        break;
                    case 3:
                        imageView.setBackgroundColor(Color.YELLOW);

                        break;
                }
            }//else imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setImageBitmap(photos.get(position).getBitmap());

            return relativeLayout;

            /*
            ImageView imageView = null;

            if (convertView == null) {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
//            int color = 0;
//            String poll = "";
//            switch (MainActivity.arrayPerson.get(position).poll){
//                case 1 :
//                    color = Color.GREEN;
//                    poll = "ЗА";
//                    break;
//                case 2 :
//                    color = Color.YELLOW;
//                    poll = "УТРИМАВСЯ";
//                    break;
//                case 3:
//                    color = Color.RED;
//                    poll = "ПРОТИ";
//                    break;
//                case 4:
//                    color = Color.parseColor("#FFFFFF");
//                    poll = "НЕ ГОЛОСУВАВ";
//                    break;
//            }


            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Random random = new Random();
            if(startSession && !stopThread) {
                switch (random.nextInt(100)) {
                    case 1:
                        imageView.setBackgroundColor(Color.GREEN);
                        break;
                    case 2:
                        imageView.setBackgroundColor(Color.RED);
                        break;
                    case 3:
                        imageView.setBackgroundColor(Color.YELLOW);

                        break;
                }
            }//else imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setImageBitmap(photos.get(position).getBitmap());
            return imageView;
            */
        }
    }
    public  class LoadPhoto extends AsyncTask<Objects,LoadedImage,Objects> {

        @Override
        protected Objects doInBackground(Objects... params) {

            for (int i = 0; i < num; i++) {
                publishProgress(new LoadedImage(newBitmap));
            }
            return null;
        }
        @Override
        public void onProgressUpdate(LoadedImage... value) {
            addImage(value);
        }
    }
}
