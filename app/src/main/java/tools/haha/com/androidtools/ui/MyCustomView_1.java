package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("unused")
public class MyCustomView_1 extends View {

    private static int sCount = 0;
    private Paint mPaint;
    private static final String MeasureText = "Hello World";

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
    }

    private int measureWidth(int mesureSpec){
        int result;
        int specMode = MeasureSpec.getMode(mesureSpec);
        int specSize = MeasureSpec.getSize(mesureSpec);
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

    private int measureHeght(int mesureSpec){
        int result;
        int specMode = MeasureSpec.getMode(mesureSpec);
        int specSize = MeasureSpec.getSize(mesureSpec);
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
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeght(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        canvas.drawRect(0, 0, w, h, mPaint);

        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(80);
        String text = MeasureText + String.valueOf(sCount++);
        canvas.drawText(text, getPaddingLeft(), getPaddingTop() - mPaint.ascent(), mPaint);
    }
}

