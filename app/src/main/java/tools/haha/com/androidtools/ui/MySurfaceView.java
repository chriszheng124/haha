package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import tools.haha.com.androidtools.R;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private Thread mThread;
    private Canvas mCanvas;
    private int mCount;
    private boolean mRunning;
    private Paint mPaint;
    private Bitmap mBitmap;
    private int bm_offsetX, bm_offsetY;

    private Path mAnimPath;
    private PathMeasure mPathMeasure;
    private float mPathLength;

    private float mStep;   //distance each step
    private float mDistance;  //distance moved

    private float[] mPos;
    private float[] mTan;

    private Matrix mMatrix;
    private Paint mClearPaint;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRunning = true;
        start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Log.v("test_11", "surfaceDestroyed");
        stop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private void init(){
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        //paint.setPathEffect(new DashPathEffect(new float[]{20,10,5,10}, 1));
        //paint.setPathEffect(new CornerPathEffect(60));

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.facebook);
        bm_offsetX = mBitmap.getWidth()/2;
        bm_offsetY = mBitmap.getHeight()/2;

        mAnimPath= new Path();
        mAnimPath.moveTo(100, 100);
        mAnimPath.lineTo(200, 100);
        mAnimPath.lineTo(300, 50);
        mAnimPath.lineTo(400, 150);
        mAnimPath.lineTo(100, 300);
        mAnimPath.lineTo(600, 300);
        mAnimPath.lineTo(100, 100);
        mAnimPath.close();

        mPathMeasure = new PathMeasure(mAnimPath, false);
        mPathLength = mPathMeasure.getLength();

        mStep = 10;
        mDistance = 0;
        mPos = new float[2];
        mTan = new float[2];

        mMatrix = new Matrix();
        getHolder().addCallback(this);
        //this.setZOrderOnTop(true);
    }

    public void stop(){
        mRunning = false;
        getHolder().removeCallback(this);
        mThread.interrupt();
    }

    private void start(){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mRunning){
                    doDraw();
                    try {
                        Thread.sleep(600);
                    }catch (Exception e){
                        return;
                    }finally {
                        if(mCanvas != null){
                            getHolder().unlockCanvasAndPost(mCanvas);
                        }
                    }

                }
            }
        }, "my_surface_view_thread");
        mThread.start();
    }

    private void doDraw(){
        mCanvas = getHolder().lockCanvas();
        if(mCanvas == null){
            return;
        }
        mCanvas.drawPaint(mClearPaint);
        mCanvas.drawPath(mAnimPath, mPaint);
        if(mDistance < mPathLength){
            mPathMeasure.getPosTan(mDistance, mPos, mTan);

            mMatrix.reset();
            float degrees = (float)(Math.atan2(mTan[1], mTan[0])*180.0/Math.PI);
            mMatrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            mMatrix.postTranslate(mPos[0]-bm_offsetX, mPos[1]-bm_offsetY);

            mCanvas.drawBitmap(mBitmap, mMatrix, null);

            mDistance += mStep;
        }else{
            mDistance = 0;
        }
        getHolder().unlockCanvasAndPost(mCanvas);
        mCanvas = null;
    }
}
