package org.bissoft.yean.votes.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomDiagram  extends View {

    private float ARC_START_ANGLE = 270; // 12 o'clock

    public float THICKNESS_SCALE = 0.1f;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private RectF mCircleOuterBounds;
    private RectF mCircleInnerBounds;

    public Paint mCirclePaint;
    private Paint mEraserPaint;

    int width;
    int height;
    int[] progressDiagram;

    static int[] color = {Color.GREEN,Color.RED, Color.YELLOW,
            Color.BLUE,Color.GRAY,Color.BLACK,
            Color.CYAN,Color.DKGRAY,Color.MAGENTA,Color.LTGRAY};
//    public CustomDiagram(Context context,String s ) {
//        super(context);
////        int numberParams = array.length;
////        progressDiagram = new int[numberParams];
////        for (int i = 0;i < numberParams; i++)
////            progressDiagram[i] = array[i];
//  //      progressDiagram = array;
//        ss = s;
//    }

    public CustomDiagram(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDiagram(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mEraserPaint = new Paint();
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setColor(Color.parseColor("#FFFFFF"));//Color.TRANSPARENT);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    }



    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // Trick to make the view square
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.parseColor("#FFFFFF"));//Color.TRANSPARENT);
            mCanvas = new Canvas(mBitmap);
            width = w;
            height = h;
        }

        super.onSizeChanged(w, h, oldw, oldh);
        updateBounds();
    }
    public void setParams(int ... params){
        progressDiagram = new int[params.length];
        for (int i = 0;i < params.length; i++)
            progressDiagram[i] = params[i];
    }

    @Override
    public void onDraw(Canvas canvas) {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        mCanvas.drawCircle(width / 2, height / 2, width / 2, mCirclePaint);

        //    Toast.makeText(getContext(),ss,Toast.LENGTH_LONG).show();

        float totalProgress = 0;
        for(int i = 0; i < progressDiagram.length; i++){
            totalProgress +=  progressDiagram[i];
        }
        Log.d("procc", "" + totalProgress);
        float procc = (float)(360 / totalProgress);
        Log.d("procc",""+procc);
        int sum = 0;
        for(int i =0; i<progressDiagram.length; i++){
            mCirclePaint.setColor(color[i]);
            sum += procc * progressDiagram[i];
            if(i == 0) {
                mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, (float) procc * progressDiagram[i], true, mCirclePaint);
                mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
                mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, 1f, true, mCirclePaint);
            }
            else {
                ARC_START_ANGLE += procc * progressDiagram[i-1];
                mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, (float)procc * progressDiagram[i], true, mCirclePaint);
                mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
                mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, 1f, true, mCirclePaint);
            }
        }
        Log.d("procc",""+sum);


        mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        mCanvas.drawCircle(width / 2, height / 2, (width / 2 - width * THICKNESS_SCALE), mCirclePaint);
        mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);

        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    private void updateBounds() {
        float thickness = getWidth() * THICKNESS_SCALE  ;

        mCircleOuterBounds = new RectF(4, 4, getWidth() -4, getHeight() - 4);
        mCircleInnerBounds = new RectF(
                mCircleOuterBounds.left + thickness,
                mCircleOuterBounds.top + thickness,
                mCircleOuterBounds.right - thickness,
                mCircleOuterBounds.bottom - thickness);

        invalidate();
    }
}

