package tools.haha.com.androidtools.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sCount = 0;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(sCount++ == 0){
            drawMultiLayer(canvas);
        }else if(sCount == 1){
            final int count = canvas.save();
            canvas.drawColor(Color.GRAY);
            //canvas.clipRect(0, 0, getMeasuredWidth(), 200);
            //canvas.scale(1, 0.5f);
            //canvas.translate(0, 200);
            //canvas.rotate(90);
            //canvas.drawBitmap(mSky, 0, 0, null);
            mMatrix.setTranslate(0, -200);
            //mMatrix.postTranslate(0, 200);
            //mMatrix.postRotate(10);
            //mMatrix.preTranslate(200, 0);
            //mMatrix.setScale(1, 0.5f);
            canvas.drawBitmap(mSky, mMatrix, null);
            canvas.restoreToCount(count);
            mPaint.setColor(Color.YELLOW);
            canvas.drawCircle(100, 100, 100, mPaint);
        }else {
            int w = getWidth();
            int h = getHeight();
            mPaint.setColor(Color.GRAY);
            canvas.drawRect(0, 0, w, h, mPaint);
            mPaint.setColor(Color.YELLOW);

            mPaint.setTextSize(80);
            String text = MeasureText + String.valueOf(sCount++);
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - mPaint.ascent(), mPaint);
        }
    }

    private void drawMultiLayer(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        canvas.translate(10, 10);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(75, 75, 75, mPaint);
        canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, Canvas.ALL_SAVE_FLAG);
        //canvas.saveLayer(0, 0, 200, 200, mPaint, Canvas.ALL_SAVE_FLAG);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(125, 125, 75, mPaint);
        canvas.restore();
    }
}

