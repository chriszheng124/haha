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
public class RoundedDrawable extends Drawable {
    private Paint mPaint;
    private float mCornerRadius;
    private BitmapShader mShader;
    private int mMargin;
    private RectF mRect;
    private RectF mBitmapRect;

    public RoundedDrawable(Bitmap bitmap, float cornerRadius, int margin) {
        super();
        mCornerRadius = cornerRadius;
        mMargin = margin;
        mRect = new RectF();
        mShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapRect = new RectF (margin, margin,
                bitmap.getWidth() - margin, bitmap.getHeight() - margin);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setShader(mShader);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);

        Matrix shaderMatrix = new Matrix();
        shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
        mShader.setLocalMatrix(shaderMatrix);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
