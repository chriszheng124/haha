package tools.haha.com.androidtools.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import tools.haha.com.androidtools.utils.CommonUtils;
import tools.haha.com.androidtools.utils.Preconditions;

/**
 * A drawable that can have rounded corners.
 */
public class RoundedBitmapDrawable extends BitmapDrawable{
    boolean mIsCircle = false;
    float[] mCornerRadii = new float[8];
    RectF mRootBounds = new RectF();
    //final RectF mLastRootBounds = new RectF();
    final Matrix mTransform = new Matrix();
    final Matrix mInverseTransform = new Matrix();
    //final Matrix mLastTransform = new Matrix();
    float mBorderWidth = 0;
    int mBorderColor = Color.TRANSPARENT;
    boolean mIsNonzero = true;

    private final Path mPath = new Path();
    private boolean mIsPathDirty = true;
    /** True if this rounded bitmap drawable will actually do anything. */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mIsShaderTransformDirty = true;
    private WeakReference<Bitmap> mLastBitmap;

    public RoundedBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Creates a new RoundedBitmapDrawable from the given BitmapDrawable.
     * @param res resources to use for this drawable
     * @param bitmapDrawable bitmap drawable containing the bitmap to be used for this drawable
     * @return the RoundedBitmapDrawable that is created
     */
    public static RoundedBitmapDrawable fromBitmapDrawable(
            Resources res,
            BitmapDrawable bitmapDrawable) {
        return new RoundedBitmapDrawable(res, bitmapDrawable.getBitmap());
    }

    /**
     * Sets whether to round as circle.
     *
     * @param isCircle whether or not to round as circle
     */
    public void setCircle(boolean isCircle) {
        mIsCircle = isCircle;
        mIsPathDirty = true;
        invalidateSelf();
    }

    /**
     * Specify radius for the corners of the rectangle. If this is > 0, then the
     * drawable is drawn in a round-rectangle, rather than a rectangle.
     * @param radius the radius for the corners of the rectangle
     */
    public void setRadius(float radius) {
        Preconditions.checkState(radius >= 0);
        Arrays.fill(mCornerRadii, radius);
        mIsPathDirty = true;
        invalidateSelf();
    }

    /**
     * Specify radii for each of the 4 corners. For each corner, the array
     * contains 2 values, [X_radius, Y_radius]. The corners are ordered
     * top-left, top-right, bottom-right, bottom-left
     * @param radii the x and y radii of the corners
     */
    public void setRadii(float[] radii) {
        if (radii == null) {
            Arrays.fill(mCornerRadii, 0);
        } else {
            Preconditions.checkArgument(radii.length == 8, "radii should have exactly 8 values");
            System.arraycopy(radii, 0, mCornerRadii, 0, 8);
        }
        mIsPathDirty = true;
        invalidateSelf();
    }

    /**
     * Sets the border
     * @param color of the border
     * @param width of the border
     */
    public void setBorder(int color, float width) {
        if (mBorderColor != color || mBorderWidth != width) {
            mBorderColor = color;
            mBorderWidth = width;
            mIsPathDirty = true;
            invalidateSelf();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != mPaint.getAlpha()) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        updateTransform();
        updateNonzero();
        if (!mIsNonzero) {
            super.draw(canvas);
            return;
        }
        updatePath();
        updatePaint();
        int saveCount = canvas.save();
        canvas.concat(mInverseTransform);
        canvas.drawPath(mPath, mPaint);
        if (mBorderWidth != 0) {
            mBorderPaint.setStrokeWidth(mBorderWidth);
            mBorderPaint.setColor(CommonUtils.multiplyColorAlpha(mBorderColor, mPaint.getAlpha()));
            canvas.drawPath(mPath, mBorderPaint);
        }
        canvas.restoreToCount(saveCount);
    }

    /**
     * If both the radii and border width are zero, there is nothing to round.
     * If so, we set internal state to delegate drawing to the superclass.
     */
    private void updateNonzero() {
        if (mIsPathDirty) {
            mIsNonzero = false;
            if (mIsCircle || mBorderWidth > 0) {
                mIsNonzero = true;
            }
            for (int i = 0; i < mCornerRadii.length; i++) {
                if (mCornerRadii[i] > 0) {
                    mIsNonzero = true;
                }
            }
        }
    }

    private void updateTransform() {
//        if (mTransformCallback != null) {
//            mTransformCallback.getTransform(mTransform);
//            mTransformCallback.getRootBounds(mRootBounds);
//        } else {
            mTransform.reset();
            mRootBounds.set(getBounds());
        //}

//        if (!mTransform.equals(mLastTransform)) {
//            mIsShaderTransformDirty = true;
//            if (!mTransform.invert(mInverseTransform)) {
//                mInverseTransform.reset();
//                mTransform.reset();
//            }
//            mLastTransform.set(mTransform);
//        }
//
//        if (!mRootBounds.equals(mLastRootBounds)) {
//            mIsPathDirty = true;
//            mLastRootBounds.set(mRootBounds);
//        }
    }

    private void updatePath() {
        if (mIsPathDirty) {
            mPath.reset();
            mRootBounds.inset(mBorderWidth/2, mBorderWidth/2);
            if (mIsCircle) {
                mPath.addCircle(
                        mRootBounds.centerX(),
                        mRootBounds.centerY(),
                        Math.min(mRootBounds.width(), mRootBounds.height())/2,
                        Path.Direction.CW);
            } else {
                mPath.addRoundRect(mRootBounds, mCornerRadii, Path.Direction.CW);
            }
            mRootBounds.inset(-(mBorderWidth/2), -(mBorderWidth/2));
            mPath.setFillType(Path.FillType.WINDING);
            mIsPathDirty = false;
        }
    }

    private void updatePaint() {
        Bitmap bitmap = getBitmap();
        if (mLastBitmap == null || mLastBitmap.get() != bitmap) {
            mLastBitmap = new WeakReference<>(bitmap);
            mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            mIsShaderTransformDirty = true;
        }
        if (mIsShaderTransformDirty) {
            mPaint.getShader().setLocalMatrix(mTransform);
            mIsShaderTransformDirty = false;
        }
    }
}
