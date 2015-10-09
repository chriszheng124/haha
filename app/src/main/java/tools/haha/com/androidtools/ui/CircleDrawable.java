package tools.haha.com.androidtools.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

@Deprecated
public class CircleDrawable extends Drawable {

    protected float radius;

    protected final RectF mRect = new RectF();
    protected final RectF mBitmapRect;
    protected final BitmapShader mShader;
    protected final Paint mPaint;
    protected final Paint mStrokePaint;
    protected final float mStrokeWidth;
    protected float mStrokeRadius;

    public CircleDrawable(Bitmap bitmap, Integer strokeColor, float strokeWidth) {
        radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;

        mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mShader);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        if (strokeColor == null) {
            mStrokePaint = null;
        } else {
            mStrokePaint = new Paint();
            mStrokePaint.setStyle(Paint.Style.STROKE);
            mStrokePaint.setColor(strokeColor);
            mStrokePaint.setStrokeWidth(strokeWidth);
            mStrokePaint.setAntiAlias(true);
        }
        this.mStrokeWidth = strokeWidth;
        mStrokeRadius = radius - mStrokeWidth / 2;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(0, 0, bounds.width(), bounds.height());
        radius = Math.min(bounds.width(), bounds.height()) / 2;
        mStrokeRadius = radius - mStrokeWidth / 2;

        Matrix shaderMatrix = new Matrix();
        shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
        mShader.setLocalMatrix(shaderMatrix);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(radius, radius, radius, mPaint);
        if (mStrokePaint != null) {
            canvas.drawCircle(radius, radius, mStrokeRadius, mStrokePaint);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
}
