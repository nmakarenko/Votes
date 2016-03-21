package org.bissoft.yean.votes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.bissoft.yean.votes.util.CustomDiagram;
import org.bissoft.yean.votes.util.LoadedImage;
import org.bissoft.yean.votes.util.Person;
import org.bissoft.yean.votes.util.PollResultsAllGridAdapter;
import org.bissoft.yean.votes.util.PollResultsNameAdapter;
import org.bissoft.yean.votes.util.PollResultsNameFrakAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * Created by ITEA on 15.03.2016.
 */
public class PollResultsFragment extends Fragment implements View.OnClickListener {

    LinearLayout linearAll;
    LinearLayout linearName;
    LinearLayout linearFrak;
    LinearLayout linearCustomDiagram;
    LinearLayout linearRadioButtons;
    LinearLayout linearRelultIcons;
    RadioButton rbAll;
    RadioButton rbName;
    RadioButton rbFrak;

    ListView listView;
    ImageView imageHaid;

    PollResultsAllGridAdapter allAdapter;
    PollResultsNameAdapter nameAdapter;
    PollResultsNameFrakAdapter nameFrakAdapter;

    LoadPhoto loadImages;
    LoadedImage[] photos = null;

    GridView gridPollAllResults;
    GridView gridPollNameReluts;

    Button btBack;
    boolean fromBillInfo;
    Bundle args;

    CustomDiagram customDiagram;

    int numPersone = 120;
    int y;

    public static ArrayList<Person> arrayPerson;
    static   String[] namePerson = {"Иван","Петро","Роман","Паша","Сергій","Влад","Саша","Андрій","Олег","Вася"};
    public  static String[] nameFrak = {"Козлята","Поросята","Гусаки","Курки","Кабани"};
    public PollResultsFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_results, container, false);


        args = getArguments();
        if (args.containsKey("from_bill_info_fragm")) {
            args.remove("from_bill_info_fragm");
            fromBillInfo = true;
        } else {
            fromBillInfo = false;
        }

        btBack = (Button) rootView.findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromBillInfo) {
                    ((MainActivity) getActivity()).changeFragment(new BillDetailFragment(),
                            args, "bill_detail_fragm", "bill_list_fragm");
                } else {
                    ((MainActivity) getActivity()).changeFragment(new BillListFragment(),
                            args, "bill_list_fragm", "");
                }
            }
        });

        TextView tvBillTitle = (TextView) rootView.findViewById(R.id.tvBillTitle);
        tvBillTitle.setText("Проект №" + getArguments().getInt("bill_num", 0) + ". Результати голосування");

        LinearLayout llHead = (LinearLayout) rootView.findViewById(R.id.llHead);
        LinearLayout llGrid = (LinearLayout) rootView.findViewById(R.id.llGrid);

        numPersone = 134;
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        double coefWH = ((double) MainActivity.width * 3 / 5) / (MainActivity.height * 7 / 9);
        double coefWHIm = ((double) bitmap1.getHeight()) / bitmap1.getWidth();
        double coef = coefWH * coefWHIm;
        double y1 = (coef + Math.sqrt(coef * coef + 4 * numPersone * coef)) / (2 * coef);
        y = (int) (y1);
        if (y1 - y > 0) y++;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                MainActivity.width * 3 / 5, (MainActivity.height * 7 / 9) / y
        );
        llHead.setLayoutParams(layoutParams);
        layoutParams = new LinearLayout.LayoutParams(
                MainActivity.width * 3 / 5, (MainActivity.height * 7 / 9) * (y - 1) / y
        );
        llGrid.setLayoutParams(layoutParams);

        Random r = new Random();
        arrayPerson = new ArrayList<>();
        for(int i = 0;i < numPersone;i++)
            arrayPerson.add(new Person(namePerson[r.nextInt(10)], r.nextInt(2), r.nextInt(4) + 1, nameFrak[r.nextInt(5)]));

        imageHaid = (ImageView)   rootView.findViewById(R.id.imageView_Haid);
        gridPollAllResults = (GridView) rootView.findViewById(R.id.grid_All);
        allAdapter = new PollResultsAllGridAdapter(getActivity());
        allAdapter.setupViews(gridPollAllResults, imageHaid, numPersone, y);
        loadImages();

        gridPollNameReluts = (GridView) rootView.findViewById(R.id.grid_Name);

        listView = (ListView) rootView.findViewById(R.id.listView_Frak);

        linearFrak = (LinearLayout) rootView.findViewById(R.id.linear_Frak);
        linearFrak.setVisibility(View.GONE);
        linearName = (LinearLayout) rootView.findViewById(R.id.linear_Name);
        linearName.setVisibility(View.GONE);
        linearAll = (LinearLayout) rootView.findViewById(R.id.linear_All);

        linearCustomDiagram = (LinearLayout) rootView.findViewById(R.id.linear_CustomDiagram);
        linearRadioButtons = (LinearLayout) rootView.findViewById(R.id.linear_RadioButtons);
        linearRadioButtons.setPadding((int)(MainActivity.width * 0.12),0,0,0);
        linearRelultIcons = (LinearLayout) rootView.findViewById(R.id.linear_ResultIcons);
        linearRelultIcons.setPadding((int)(MainActivity.width * 0.12),0,0,0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(MainActivity.height * 0.35),(int)(MainActivity.height * 0.35));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        linearCustomDiagram.setLayoutParams(params);

        rbAll = (RadioButton) rootView.findViewById(R.id.radioButton_All);
        rbAll.setChecked(true);
        rbName = (RadioButton) rootView.findViewById(R.id.radioButton_Name);
        rbFrak = (RadioButton) rootView.findViewById(R.id.radioButton_Frak);
        rbAll.setOnClickListener(this);
        rbName.setOnClickListener(this);
        rbFrak.setOnClickListener(this);

        customDiagram = (CustomDiagram) rootView.findViewById(R.id.customImage_Progress_Poll_Results);
        customDiagram.setParams(80,40,20,20);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        RadioButton rb = (RadioButton)v;
        switch (rb.getId()){
            case R.id.radioButton_All :
                linearAll.setVisibility(View.VISIBLE);
                linearFrak.setVisibility(View.GONE);
                linearName.setVisibility(View.GONE);
                allAdapter = new PollResultsAllGridAdapter(getActivity());
                allAdapter.setupViews(gridPollAllResults, imageHaid, numPersone, y);
                loadImages();
                break;
            case R.id.radioButton_Name :
                linearName.setVisibility(View.VISIBLE);
                linearAll.setVisibility(View.GONE);
                linearFrak.setVisibility(View.GONE);
                Collections.sort(arrayPerson, Person.PersoneComparator);
                nameAdapter = new PollResultsNameAdapter(getActivity());
                nameAdapter.setupViews(gridPollNameReluts,numPersone);
                loadImages();
                break;
            case R.id.radioButton_Frak :
                nameFrakAdapter = new PollResultsNameFrakAdapter(getActivity(),nameFrak);
                listView.setAdapter(nameFrakAdapter);
                linearFrak.setVisibility(View.VISIBLE);
                linearAll.setVisibility(View.GONE);
                linearName.setVisibility(View.GONE);
                break;
        }
    }

    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            if(rbAll.isChecked()) {
                allAdapter.addPhoto(image);
                allAdapter.notifyDataSetChanged();
            }
            if(rbName.isChecked()){
                nameAdapter.addPhoto(image);
                nameAdapter.notifyDataSetChanged();
            }
        }
    }
    class LoadPhoto extends AsyncTask<Objects,LoadedImage,Objects> {
        @Override
        protected Objects doInBackground(Objects... params) {

            for (int i = 0; i <arrayPerson.size(); i++) {
                publishProgress(new LoadedImage(allAdapter.newBitmap));
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
            loadImages = (LoadPhoto) new LoadPhoto().execute();
        } else {
            photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                loadImages = (LoadPhoto) new LoadPhoto().execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }
}


