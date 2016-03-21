package org.bissoft.yean.votes.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.bissoft.yean.votes.MainActivity;
import org.bissoft.yean.votes.PollResultsFragment;
import org.bissoft.yean.votes.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ITEA on 17.03.2016.
 */
public class PollResultsAllGridAdapter extends BaseAdapter {

    public double a;
    private int hA;
    private int wA;
    private int heightDisplay;
    private int widthDisplay;

    int y;

    private Bitmap bitmap = null;
    public Bitmap newBitmap = null;
    private ImageView imageView = null;

    public Context mContext;

    public ArrayList<LoadedImage> photos = new ArrayList<>();

    public PollResultsAllGridAdapter(Context context) {
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

        int color = 0;
        switch (PollResultsFragment.arrayPerson.get(position).poll){
            case 1 :
                color = Color.GREEN;
                break;
            case 2 :
                color = Color.YELLOW;
                break;
            case 3:
                color = Color.RED;
                break;
            case 4:
                color = Color.parseColor("#FFFFFF");
                break;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(color);
        imageView.setImageBitmap(photos.get(position).getBitmap());

        return relativeLayout;

/*        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        int color = 0;
        switch (PollResultsFragment.arrayPerson.get(position).poll){
            case 1 :
                color = Color.GREEN;
                break;
            case 2 :
                color = Color.YELLOW;
                break;
            case 3:
                color = Color.RED;
                break;
            case 4:
                color = Color.parseColor("#FFFFFF");
                break;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(color);
        imageView.setImageBitmap(photos.get(position).getBitmap());
        return imageView;*/
    }
    public void setupViews(GridView grid,ImageView imageHaid,int numPersone, int y) {
        heightDisplay = (MainActivity.height * 7 / 9) * (y - 1) / y;
        widthDisplay = MainActivity.width * 3 / 5;
        wA = y - 1;
        if (numPersone % (y - 1) == 0) {
            hA = numPersone / (y - 1);
        } else {
            hA = numPersone / (y - 2);
        }
        a = MainActivity.height * 7 / 9 / y;
        grid.setNumColumns(hA);
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.man);
        newBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (a - 8 * MainActivity.density),
                (int) (a - 8 * MainActivity.density), true);
        imageHaid.setImageBitmap(newBitmap);
        grid.setClipToPadding(true);
        grid.setAdapter(this);
/*
        heightDisplay =  MainActivity.height * 7 / 9 * 9 / 10;
        widthDisplay = MainActivity.width * 3 / 5;
        double s = heightDisplay * widthDisplay;
        a = Math.sqrt(s / numPersone);
        wA = (int)(heightDisplay / a);
        hA = (int)(numPersone / wA);
        if((wA * hA)< numPersone) hA +=1;
        grid.setNumColumns(hA);
        grid.setClipToPadding(true);
        grid.setAdapter(this);

        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.man);
        newBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (a - 4 * MainActivity.density),
                (int) (a - 4 * MainActivity.density), true);
        imageHaid.setImageBitmap(newBitmap);*/
    }

}

