package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import tools.haha.com.androidtools.R;

@SuppressWarnings("unused")
public class MyCustomView_1 extends View {
    private static final String MeasureText = "Hello World";
    private static int sCount = 0;
    private Paint mPaint;
    private Matrix mMatrix;
    private Bitmap mSky;

    public MyCustomView_1(Context context) {
        super(context);
        init();
    }

    public MyCustomView_1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCustomView_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mMatrix = new Matrix();
        mSky = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
    }

    private int measureWidth(int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            Paint p = new Paint();
            p.setTextSize(80);
            result = (int)p.measureText(MeasureText) + getPaddingLeft() + getPaddingRight();
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            Paint p = new Paint();
            p.setTextSize(80);
            result = (int)(p.descent()-p.ascent()) + getPaddingBottom() + getPaddingTop();
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(sCount++ % 3 == 0){
            canvas.drawColor(Color.GRAY);
            final int count = canvas.save();
            //canvas.clipRect(0, 0, getMeasuredWidth(), 200);
            //canvas.translate(0, 200);
            //canvas.rotate(90);
            //canvas.drawBitmap(mSky, 0, 0, null);
            //mMatrix.setTranslate(0, -200);
            //mMatrix.postTranslate(0, 200);
            mMatrix.postRotate(10);
            mMatrix.preTranslate(200, 0);
            canvas.drawBitmap(mSky, mMatrix, null);
            canvas.restoreToCount(count);
        }else {
            int w = getWidth();
            int h = getHeight();
            canvas.drawRect(0, 0, w, h, mPaint);

            mPaint.setColor(Color.YELLOW);
            mPaint.setTextSize(80);
            String text = MeasureText + String.valueOf(sCount++);
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - mPaint.ascent(), mPaint);
        }
    }
}

