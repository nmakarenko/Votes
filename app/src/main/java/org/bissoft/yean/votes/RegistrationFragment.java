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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
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

/**
 * Created by Natalia on 14.03.2016.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {

    boolean isHead;

    LinearLayout linearRegTime;
    LinearLayout linearResultReg;
    LinearLayout linearHaidIcon;

    TextView tvSessiaReg;
    TextView tvTimerReg;
    TextView tvResultAll;
    TextView tvResultReg;
    TextView tvResultEps;
    TextView tvQvorum;

    Button buttonContinue;
    Button buttonRestartReg;

    ImageView imageHaid;

    GridView gridPhotos;

    CustomDiagram imageDiagram;

    ProgressBar progressReg;

    Thread thread;
    Handler h;
    Runnable runnable;
    ImageAdapter imageAdapter;
    Bitmap bitmap = null;
    Bitmap newBitmap = null;

    LoadPhoto loadImages;
    LoadedImage[] photos = null;

    double a;
    double s;

    int heightGrid;
    int widthGrid;


    int num = 120;
    int hA;
    int wA;

    int numSessia = 45;
    int resultAll = 120;
    int resultReg = 80;
    int resultEps = 40;


    volatile boolean stopThread = false;

    int y;

    public RegistrationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        h = new ValidHandler(getActivity());

        isHead = getArguments().getBoolean("is_head", false);
        num = getArguments().getInt("total", 120);

        buttonContinue = (Button) rootView.findViewById(R.id.button_continue);
        buttonContinue.setOnClickListener(this);

        buttonRestartReg = (Button) rootView.findViewById(R.id.button_restart_regestration);
        buttonRestartReg.setOnClickListener(this);

        imageHaid = (ImageView) rootView.findViewById(R.id.imageView_Haid);

        linearHaidIcon = (LinearLayout) rootView.findViewById(R.id.linear_Haid_Icon);
        linearRegTime = (LinearLayout) rootView.findViewById(R.id.linear_RegTime);
        linearResultReg = (LinearLayout) rootView.findViewById(R.id.linear_ResultReg);
        linearResultReg.setVisibility(View.GONE);
        tvSessiaReg = (TextView) rootView.findViewById(R.id.textView_SessiaReg_Head);
        tvSessiaReg.setText(getResources().getString(R.string.regisr_sessia_head) + numSessia);// + numSessia);
        tvTimerReg = (TextView) rootView.findViewById(R.id.textView_TimerRegist_Head);
        tvResultAll = (TextView) rootView.findViewById(R.id.textView_ResultAll);
        tvResultAll.setText(getResources().getString(R.string.resut_all)+" " + resultAll);
        tvResultReg = (TextView) rootView.findViewById(R.id.textView_ResultReg);
        tvResultReg.setText(getResources().getString(R.string.result_reg)+" " + resultReg);
        tvResultEps = (TextView) rootView.findViewById(R.id.textView_ResultEps);
        tvResultEps.setText(getResources().getString(R.string.result_eps)+" " + resultEps);
        tvQvorum = (TextView) rootView.findViewById(R.id.textView_Qvorum);
        tvQvorum.setText(getResources().getString(R.string.qvorum)+" досягнуто");

        progressReg = (ProgressBar) rootView.findViewById(R.id.progressBar_Regist_Head);
        progressReg.setPadding(MainActivity.height / 15, 0, MainActivity.height / 15, 0);

        imageDiagram = (CustomDiagram) rootView.findViewById(R.id.Image_Progress);

        gridPhotos = (GridView) rootView.findViewById(R.id.grid);
        imageAdapter = new ImageAdapter(getActivity());
        gridPhotos.setAdapter(imageAdapter);

      //  num = 26;
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        double coefWH = ((double) MainActivity.width * 3 / 5) / (MainActivity.height * 8 / 9);
        double coefWHIm = ((double) bitmap1.getHeight()) / bitmap1.getWidth();
        double coef = coefWH * coefWHIm;
        double y1 = (coef + Math.sqrt(coef * coef + 4 * num * coef)) / (2 * coef);
        y = (int) (y1);
        if (y1 - y > 0) y++;
        int x = (int) (y * coefWH * coefWHIm);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                MainActivity.width * 3 / 5, (MainActivity.height * 8 / 9) / y
        );
        linearHaidIcon.setLayoutParams(layoutParams);
        layoutParams = new LinearLayout.LayoutParams(
                MainActivity.width * 3 / 5, (MainActivity.height * 8 / 9) * (y - 1) / y
        );
        LinearLayout llGrid = (LinearLayout) rootView.findViewById(R.id.llGrid);
        llGrid.setLayoutParams(layoutParams);

        setupViews();
        loadImages();


        imageDiagram.setParams(80, 40);
        runnable = new Runnable() {
            @Override
            public void run() {

                if(!stopThread) {
                    int progressStatus = 0;
                    int time = MainActivity.REGISTR_TIME;
                    for (int i = 0; i <= MainActivity.REGISTR_TIME; i++, time--) {
                        if (i == 0)
                            progressReg.setProgress(0);
                        else
                            progressReg.setProgress(progressStatus += 300 / MainActivity.REGISTR_TIME);
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
            }

        };
        startThread();
        return rootView;
    }

    private void startThread(){
        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_continue :
                Bundle args = new Bundle();
                args.putBoolean("is_head", isHead);
                ((MainActivity) getActivity()).changeFragment(new BillListFragment(), args, "bill_list_fragm", "");
                break;
            case R.id.button_restart_regestration :
                progressReg.setProgress(0);
                gridPhotos.invalidateViews();
                gridPhotos.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();

                linearRegTime.setVisibility(View.VISIBLE);
                linearResultReg.setVisibility(View.GONE);
                stopThread = false;

                startThread();
                ((MainActivity) getActivity()).startSession();
                break;
        }
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
                int timer = b.getInt("timer");
                if (timer > 9)
                    tvTimerReg.setText("00:" + timer);
                else tvTimerReg.setText("00:0" + timer);
                if (b.getBoolean("threadEnd")) {
                    //    stopThread = true;

                    linearRegTime.setVisibility(View.GONE);
                    linearResultReg.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setupViews() {
        heightGrid = (MainActivity.height * 8 / 9) * (y - 1) / y;
        widthGrid = MainActivity.width * 3 / 5;
        wA = y - 1;
        if (num % (y - 1) == 0) {
            hA = num / (y - 1);
        } else {
            hA = num / (y - 2);
        }
        a = MainActivity.height * 8 / 9 / y;
        gridPhotos.setNumColumns(hA);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        newBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (a - 8 * MainActivity.density),
                (int) (a - 8 * MainActivity.density), true);
        imageHaid.setImageBitmap(newBitmap);
        imageHaid.setBackgroundColor(Color.GREEN);
        gridPhotos.setClipToPadding(true);

     /*   heightGrid = MainActivity.height * 8 / 10;
        widthGrid = MainActivity.width * 3 / 5;
        s = heightGrid * widthGrid;
        a = Math.sqrt(s / num);
        wA = (int)(heightGrid / a);
        hA = (int)(num / wA);
        if((wA * hA)< num) hA +=1;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  (int) (a - 4 * MainActivity.density));
        linearHaidIcon.setLayoutParams(params);

        heightGrid = MainActivity.height * 8 / 9 - (int) (a - 4 * MainActivity.density);
        widthGrid = MainActivity.width * 3 / 5;
        s = heightGrid * widthGrid;
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
        imageHaid.setBackgroundColor(Color.GREEN);
        gridPhotos.setClipToPadding(true);*/
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

            if(!stopThread) {
                Random random = new Random();
                if (random.nextInt(30) == 1)
                    imageView.setBackgroundColor(Color.GREEN);
            }


            imageView.setImageBitmap(photos.get(position).getBitmap());

            return relativeLayout;

           /* ImageView imageView = null;
            if (convertView == null) {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if(!stopThread) {
                Random random = new Random();
                if (random.nextInt(30) == 1)
                    imageView.setBackgroundColor(Color.GREEN);
            }


            imageView.setImageBitmap(photos.get(position).getBitmap());

            return imageView;
*/
        }
    }
    class LoadPhoto extends AsyncTask<Objects,LoadedImage,Objects> {

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

    public void loadImages() {

        final Object data = getActivity().getLastNonConfigurationInstance();
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

    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            imageAdapter.addPhoto(image);
            imageAdapter.notifyDataSetChanged();
        }
    }
}
