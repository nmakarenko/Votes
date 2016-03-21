package org.bissoft.yean.votes.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.bissoft.yean.votes.MainActivity;
import org.bissoft.yean.votes.PollResultsFragment;
import org.bissoft.yean.votes.R;

import java.util.ArrayList;

/**
 * Created by ITEA on 17.03.2016.
 */
public class PollResultsNameAdapter extends BaseAdapter {

    public double a;
    private int hA;
    private int wA;
    private int heightDisplay;
    private int widthDisplay;

    private Bitmap bitmap = null;
    public Bitmap newBitmap = null;
    private View rowView = null;

    public Context mContext;

    public ArrayList<LoadedImage> photos = new ArrayList<>();

    public PollResultsNameAdapter(Context context) {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)  mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.grid_name_filter,parent,false);
        } else {
            rowView = (View) convertView;
        }
        int color = 0;
        Drawable poll = null;
        switch (PollResultsFragment.arrayPerson.get(position).poll){
            case 1 :
                color = Color.GREEN;
                poll = mContext.getResources().getDrawable(R.drawable.yes);
                break;
            case 2 :
                color = Color.YELLOW;
                poll = mContext.getResources().getDrawable(R.drawable.ref);;
                break;
            case 3:
                color = Color.RED;
                poll = mContext.getResources().getDrawable(R.drawable.no);;
                break;
            case 4:
                color = Color.parseColor("#FFFFFF");
                poll = mContext.getResources().getDrawable(R.drawable.ref);;
                break;
        }
        ImageView imageSex = (ImageView) rowView.findViewById(R.id.imageView_Sex);
        TextView tvName = (TextView) rowView.findViewById(R.id.textView_Name);
        TextView tvNameFrak = (TextView) rowView.findViewById(R.id.textView_Frak);
        ImageView imageResult = (ImageView) rowView.findViewById(R.id.imageView_Result);
        imageSex.setBackgroundColor(color);
        imageSex.setImageBitmap(photos.get(position).getBitmap());
        tvName.setText(PollResultsFragment.arrayPerson.get(position).name);
        tvNameFrak.setText("Фракція - "+PollResultsFragment.arrayPerson.get(position).nameFrak);
        imageResult.setBackground(poll);
        return rowView;
    }
    public void setupViews(GridView grid,int numPersone) {

        heightDisplay =  MainActivity.height * 7 / 9 * 9 / 10;
        widthDisplay = MainActivity.width * 3 / 5;
        double s = heightDisplay * widthDisplay;
        a = Math.sqrt(s / numPersone);
        wA = (int)(heightDisplay / a);
        hA = (int)(numPersone / wA);
        if((wA * hA)< numPersone) hA +=1;
        grid.setNumColumns(2);
        grid.setClipToPadding(true);
        grid.setAdapter(this);

        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.man);
        newBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (a - 4 * MainActivity.density),
                (int) (a - 4 * MainActivity.density), true);
    }

}
