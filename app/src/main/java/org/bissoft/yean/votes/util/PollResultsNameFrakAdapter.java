package org.bissoft.yean.votes.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.bissoft.yean.votes.MainActivity;
import org.bissoft.yean.votes.PollResultsFragment;
import org.bissoft.yean.votes.R;

import java.util.ArrayList;

/**
 * Created by ITEA on 17.03.2016.
 */
public class PollResultsNameFrakAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private int heightDisplay =  MainActivity.height * 7 / 9;
    private int widthDisplay = MainActivity.width * 3 / 5;


    public PollResultsNameFrakAdapter(Context context, String[] values) {
        super(context, R.layout.grid_name_frak, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.grid_name_frak, parent, false);
        } else {
            rowView = (View) convertView;
        }
        TextView textView = (TextView) rowView.findViewById(R.id.textView5);
        textView.setText(values[position]);
        GridView grid = (GridView) rowView.findViewById(R.id.gridView);
        grid.setNumColumns(2);

        int num = chooseFrak(values[position]).size();
        double h = num / 2;
        if (num % 2 != 0) h += 1;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDisplay,
                (int)(((heightDisplay / 4)*h)));
        grid.setLayoutParams(params);
        Log.d("EEEE", "" + num + " " + h + " " + ((num / 2) % 2));
        nameFrakGridViewAdapter nameFrakAdapter = new nameFrakGridViewAdapter(getContext(), chooseFrak(values[position]));
        grid.setAdapter(nameFrakAdapter);

        return rowView;
    }

    public ArrayList<Person> chooseFrak(String nameFrak) {
        ArrayList<Person> arrayFrak = new ArrayList<>();
        for (int i = 0; i < PollResultsFragment.arrayPerson.size(); i++) {
            if (PollResultsFragment.arrayPerson.get(i).nameFrak.equals(nameFrak))
                arrayFrak.add(PollResultsFragment.arrayPerson.get(i));
        }
        return arrayFrak;
    }

    public class nameFrakGridViewAdapter extends ArrayAdapter<Person> {
        private final Context context;
        private final ArrayList<Person> values;

        public nameFrakGridViewAdapter(Context context, ArrayList<Person> values) {
            super(context, R.layout.grid_name_frak, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView;
            Bitmap bitmap = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.grid_name_filter, parent, false);
            } else {
                rowView = (View) convertView;
            }
            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, heightDisplay / 4);
            rowView.setLayoutParams(params);

            TextView tvPoll = (TextView) rowView.findViewById(R.id.textView4);
            //   imageView1.setBackgroundColor(color);
            Drawable poll = null;
            int color = 0;
            switch (values.get(position).poll) {
                case 1:
                    color = Color.GREEN;
                    poll = context.getResources().getDrawable(R.drawable.yes);
                    break;
                case 2:
                    color = Color.YELLOW;
                    poll = context.getResources().getDrawable(R.drawable.ref);
                    ;
                    break;
                case 3:
                    color = Color.RED;
                    poll = context.getResources().getDrawable(R.drawable.no);
                    ;
                    break;
                case 4:
                    color = Color.parseColor("#FFFFFF");
                    poll = context.getResources().getDrawable(R.drawable.ref);
                    ;
                    break;
            }

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.man);
            ImageView imageSex = (ImageView) rowView.findViewById(R.id.imageView_Sex);
            TextView tvName = (TextView) rowView.findViewById(R.id.textView_Name);
            TextView tvNameFrak = (TextView) rowView.findViewById(R.id.textView_Frak);
            ImageView imageResult = (ImageView) rowView.findViewById(R.id.imageView_Result);
            imageSex.setImageBitmap(bitmap);
            imageSex.setBackgroundColor(color);
//            imageSex.setImageBitmap(photos.get(position).getBitmap());
            tvName.setText(values.get(position).name);
            tvNameFrak.setText(values.get(position).nameFrak);
            imageResult.setBackground(poll);
            //      tvPoll.setText(poll);
            return rowView;
        }
    }
}

