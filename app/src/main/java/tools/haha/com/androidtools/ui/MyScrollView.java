package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

@SuppressWarnings("unused")
public class MyScrollView extends ViewGroup {

    private static final String LOG_TAG = "VerticalViewPager";
    private static final boolean DEBUG = true;
    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips
    private static final int MIN_FLING_VELOCITY = 400; // dips
    private static final int CLOSE_ENOUGH = 2; // dp
    private static final int INVALID_POINTER = -1;

    public static final int SCROLL_STATE_IDLE = 0;
    /**
     * Indicates that the pager is currently being dragged by the user.
     */
    public static final int SCROLL_STATE_DRAGGING = 1;
    /**
     * Indicates that the pager is in the process of settling to a final position.
     */
    public static final int SCROLL_STATE_SETTLING = 2;
    private int mScrollState = SCROLL_STATE_IDLE;

    private VelocityTracker mVelocityTracker;

    private Scroller mScroller;
    private int mChildWidthMeasureSpec;
    private int mChildHeightMeasureSpec;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mCloseEnough;
    private boolean mIsBeingDragged;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = -1;

    private int mTopShowingPage;
    private int mChildHeight;
    private int mOldScrollY;


    public MyScrollView(Context context) {
        super(context);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        Context context = getContext();
        mTopShowingPage = 1;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        final float density = context.getResources().getDisplayMetrics().density;
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mMinimumVelocity = (int) (MIN_FLING_VELOCITY * density);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
        mCloseEnough = (int) (CLOSE_ENOUGH * density);

        if(mScroller == null){
            mScroller = new Scroller(getContext());
        }
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int w = getDefaultSize(0, widthMeasureSpec);
        setMeasuredDimension(w,
                getDefaultSize(0, heightMeasureSpec));
        final int measuredWidth = getMeasuredWidth();
        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int heightSpec = MeasureSpec.makeMeasureSpec(
                        (int)(childHeightSize * lp.heightFactor), MeasureSpec.EXACTLY);
                child.measure(mChildWidthMeasureSpec, heightSpec);
            }
        }

    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int childLeft = 0;
        int childTop = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                mChildHeight = child.getMeasuredHeight();
                childTop = mChildHeight * i;

                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());
            }
        }
    }

    // ----------------------------------------------------------------------
    // The rest of the implementation is for custom per-child layout parameters.
    // If you do not need these (for example you are writing a layout manager
    // that does fixed positioning of its children), you can drop all of this.

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyScrollView.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    private void setScrollState(int newState) {
        if (mScrollState == newState) {
            return;
        }
        mScrollState = newState;
//        if (mOnPageChangeListener != null) {
//            mOnPageChangeListener.onPageScrollStateChanged(newState);
//        }
    }

    private void completeScroll() {
        setScrollingCacheEnabled(false);
        mScroller.abortAnimation();
//        int oldX = getScrollX();
//        int oldY = getScrollY();
//        int x = mScroller.getCurrX();
//        int y = mScroller.getCurrY();
//        if (oldX != x || oldY != y) {
//            scrollTo(x, y);
//        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        mOldScrollY = getScrollY();
        mIsBeingDragged = false;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
//        if (mScrollingCacheEnabled != enabled) {
//            mScrollingCacheEnabled = enabled;
//            if (USE_CACHE) {
//                final int size = getChildCount();
//                for (int i = 0; i < size; ++i) {
//                    final View child = getChildAt(i);
//                    if (child.getVisibility() != GONE) {
//                        child.setDrawingCacheEnabled(enabled);
//                    }
//                }
//            }
//        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if(DEBUG){
            Log.v(LOG_TAG, "onInterceptTouchEvent got action : " + action);
        }
        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            mIsBeingDragged = false;
            mActivePointerId = INVALID_POINTER;
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            return false;
        }
        if (action != MotionEvent.ACTION_DOWN) {
            if (mIsBeingDragged) {
                if(DEBUG){
                    Log.v(LOG_TAG, "onInterceptTouchEvent mIsBeingDragged is already true");
                }
                return true;
            }
        }
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = mLastMotionX = ev.getX();
                mInitialMotionY = mLastMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                mScroller.computeScrollOffset();
                if (mScrollState == SCROLL_STATE_SETTLING &&
                        Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough) {
                    mScroller.abortAnimation();
                    //populate();
                    mIsBeingDragged = true;
                    setScrollState(SCROLL_STATE_DRAGGING);
                } else {
                    completeScroll();
                    mIsBeingDragged = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerId = mActivePointerId;
                if(activePointerId == INVALID_POINTER){
                    if(DEBUG){
                        Log.v(LOG_TAG, "onInterceptTouchEvent got invalid pointer");
                    }
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                final float x = ev.getX(pointerIndex);
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = ev.getY(pointerIndex);
                final float dy = y - mLastMotionY;
                final float yDiff = Math.abs(dy);
                if(yDiff > mTouchSlop && yDiff * 0.5 > xDiff){
                    mIsBeingDragged = true;
                    setScrollState(SCROLL_STATE_DRAGGING);
                    setScrollingCacheEnabled(true);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            default:
                break;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        if(DEBUG){
            Log.v(LOG_TAG, "onInterceptTouchEvent return : " + mIsBeingDragged);
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if(DEBUG){
            Log.v(LOG_TAG, "onTouchEvent got action : " + action);
        }
        if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            // Don't handle edge touches immediately -- they may actually belong to one of our
            // descendants.
            if(DEBUG){
                Log.v(LOG_TAG, "onTouchEvent touch edges");
            }
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mIsBeingDragged = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsBeingDragged) {
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float xDiff = Math.abs(x - mLastMotionX);
                    final float y = ev.getY(pointerIndex);
                    final float yDiff = Math.abs(y - mLastMotionY);
                    if (yDiff > mTouchSlop && yDiff > xDiff) {
                        mIsBeingDragged = true;
                    }
                }
                if (mIsBeingDragged) {
                    final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    final float y = ev.getY(activePointerIndex);
                    scrollBy(0, (int)(mLastMotionY - y));
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                //handleActionUp(ev);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                final int index = ev.getActionIndex();
                mLastMotionY = ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionY = ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void handleActionUp(MotionEvent ev){
        final VelocityTracker velocityTracker = mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        int initialVelocity = (int) velocityTracker.getYVelocity(mActivePointerId);
        int ceilingPage = (mChildHeight + getScrollY())/mChildHeight;
        final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
        final float y = ev.getY(activePointerIndex);
        float motionDy = mInitialMotionY - y;
        mTopShowingPage = determineNextPage(ceilingPage, initialVelocity, motionDy);
        int dy = (mChildHeight)*(mTopShowingPage-1) - getScrollY();
        mScroller.startScroll(0, getScrollY(), 0, dy, 1000);
        endDrag();
    }

    private int determineNextPage(int ceilingPage, int velocity, float deltaY) {
        int nextPage;
        if (Math.abs(deltaY) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
            nextPage = velocity > 0 ? mTopShowingPage-1 : mTopShowingPage + 1;
        }else{
            int pageInc = 0;
            if(Math.abs(deltaY / mChildHeight) > 0.3f){
                if(mOldScrollY > getScrollY()){
                    ;//pageInc = -1;
                }else{
                    pageInc = 1;
                }
            }
            nextPage = ceilingPage + pageInc;
        }
        return nextPage;
    }

    /**
     * Custom per-child layout information.
     */
    public static class LayoutParams extends MarginLayoutParams {
        float heightFactor = 0.8f;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
