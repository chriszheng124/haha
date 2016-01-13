package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import tools.haha.com.androidtools.R;

public class MyDrawerLayout extends LinearLayout{
    private ViewDragHelper mDragHelper;
    private View mTargetView;

    public MyDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDrawerLayout(Context context) {
        super(context);
        init();
    }

    private void init(){
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTargetView = findViewById(R.id.drag_target);
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View view, int pointerId) {
            return mTargetView == view;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(child.getTop() + dy > 0){
                return 0;
            }
            if(child.getTop() + dy < -getHeight()){
                return -getHeight();
            }
            return child.getTop() + dy;
        }

//        @Override
//        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            if(child.getLeft() + dx < -mTargetView.getWidth()){
//                return -mTargetView.getWidth();
//            }
//            if(child.getLeft() + dx > 0){
//                return 0;
//            }
//            return child.getLeft()+dx;
//        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:
                    break;
                case ViewDragHelper.STATE_IDLE:
                    if(mTargetView.getTop() == -getHeight()) {
                        setVisibility(GONE);
                    }
                    break;
                case ViewDragHelper.STATE_SETTLING:
                    break;
            }
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(yvel > 0 || (yvel == 0 && Math.abs(releasedChild.getTop() * 2) < getHeight())){
                mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), 0);
            }else {
                mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), -getHeight());
            }
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //float alpha = 1.f - ((float)Math.abs(top))/(float)getHeight();
            //Log.v("123---", "top = " + top + " getHeight = " + getHeight() + " alpha = " + alpha);
            //changedView.setAlpha(alpha);
            //invalidate();
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mTargetView, pointerId);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        mTargetView.layout(mTargetView.getLeft(), -mTargetView.getHeight(),
//                mTargetView.getRight(), 0);
    }

    @Override
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_DOWN:
                mDragHelper.cancel();
                break;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
