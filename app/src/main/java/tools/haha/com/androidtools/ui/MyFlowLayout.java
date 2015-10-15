package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tools.haha.com.androidtools.utils.CommonUtils;

public class MyFlowLayout extends ViewGroup{
    private static final float MIN_ITEM_WIDTH_RATIO = 0.333f;
    private int mRowCount;
    private int mMaxCountEveryRow;
    private int mMinCountEveryRow;
    private int mMargin;
    private List<TextView> mTmpList;
    private List<List<TextView>> mViewList;
    private LayoutAnimationController mController;
    private int mRowHeight;
    private boolean mHasAllocateRow;

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode;
        heightMode = MeasureSpec.EXACTLY;
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        widthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

        if(mRowHeight == 0) {
            mRowHeight = parentHeight / mRowCount - mMargin * (mRowCount - 1);
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mRowHeight, heightMode);

        int leftWidth;
        if(!mHasAllocateRow){
            for (int row = 0; row < mRowCount; ){
                leftWidth = allocateRow(widthMeasureSpec, heightMeasureSpec, row);
                if(leftWidth > 0) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(leftWidth, widthMode);
                }else{
                    //begin to measure next row
                    leftWidth = parentWidth - getPaddingLeft() - getPaddingRight();
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(leftWidth, widthMode);

                    measureRow(widthMeasureSpec, heightMeasureSpec, row);
                    row++;
                }
            }
        }
        mHasAllocateRow = true;
        resizeViewInRow(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int oldLeft = left;
        for (List<TextView> list : mViewList){
            left = oldLeft;
            for (TextView tv : list){
                tv.layout(left, top, left + tv.getMeasuredWidth(), top + tv.getMeasuredHeight());
                left += (tv.getMeasuredWidth() + mMargin);
            }
            top += (mRowHeight + mMargin);
        }
    }

    public MyFlowLayout setRowCount(int count){
        mRowCount = count;
        return this;
    }

    public MyFlowLayout setMaxCountEveryRow(int count){
        mMaxCountEveryRow = count;
        return this;
    }

    public MyFlowLayout setMinCountEveryRow(int count){
        mMinCountEveryRow = count;
        return this;
    }

    public MyFlowLayout setSpaceBetweenItem(int margin){
        mMargin = CommonUtils.dp2px(getContext(), margin);
        return this;
    }

    public void setLayoutAnimation(Animation animation){
        mController = new LayoutAnimationController(animation);
        mController.setDelay(0);
    }

    public void create(){
        mTmpList = new ArrayList<>();
        mViewList = new ArrayList<>();
        for (int i = 0; i < mRowCount; ++i){
            List<TextView> viewList = new ArrayList<>();
            mViewList.add(viewList);
        }
    }

    public void addTextView(TextView tv){
        mTmpList.add(tv);
        addView(tv);
    }

    private int allocateRow(int widthMeasureSpec, int heightMeasureSpec, int row){
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        //int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        TextView tv = mTmpList.get(0);
        List<TextView> listView = mViewList.get(row);
        tv.measure(widthMeasureSpec, heightMeasureSpec);

        if(listView.size() < mMinCountEveryRow){
            listView.add(tv);
            mTmpList.remove(0);
            if(parentWidth - tv.getMeasuredWidth() - mMargin <= 0
                    && listView.size() < mMinCountEveryRow){
                return 1;
            }
        }else if(listView.size() < mMaxCountEveryRow){
            int usedWidth = 0;
            for (TextView v : listView){
                usedWidth += v.getMeasuredWidth();
                usedWidth += mMargin;
            }
            if(parentWidth - usedWidth > 0){
                listView.add(tv);
                mTmpList.remove(0);
            }else {
                return -1;
            }
        }else {
            return -1;
        }
        return parentWidth - tv.getMeasuredWidth() - mMargin;
    }

    /**
     * ensure the width of every child view exceeds 1/3 of parent width
     */
    private void resizeViewInRow(int widthMeasureSpec, int heightMeasureSpec){
//        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//        for (List<TextView> listView : mViewList){
//            for (TextView tv : listView){
//                int w = tv.getMeasuredWidth();
//                if(w < (int)(float)parentWidth * MIN_ITEM_WIDTH_RATIO){
//                    w = (int)((float)parentWidth * MIN_ITEM_WIDTH_RATIO);
//                    int widthSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
//                    tv.measure(widthSpec, heightMeasureSpec);
//                }
//            }
//        }
    }

    private void measureRow(int widthMeasureSpec, int heightMeasureSpec, int row){
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

        List<TextView> listView = mViewList.get(row);
        int leftWidth = parentWidth;
        for (int i = 0; i < listView.size(); i++){
            TextView tv = listView.get(i);
            leftWidth -= tv.getMeasuredWidth();
            leftWidth -= mMargin;
            if(leftWidth <= 0){
                if(i == 0){
                    int usedWidth = mMargin + getPaddingLeft() + getPaddingRight();
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((parentWidth - usedWidth)/2, MeasureSpec.EXACTLY);
                    tv.measure(widthMeasureSpec, heightMeasureSpec);
                    TextView secondView = listView.get(1);
                    secondView.measure(widthMeasureSpec, heightMeasureSpec);
                    return;
                }else {
                    int totalWidth = 0;
                    for(int j = 0; j <= i; ++j){
                        totalWidth += listView.get(j).getMeasuredWidth();
                    }
                    int leftParentWidth = parentWidth - ((listView.size() - 1) * mMargin
                            + getPaddingLeft() + getPaddingRight());
                    for(int j = 0; j <= i; ++j){
                        int w = listView.get(j).getMeasuredWidth();
                        w = (int)((float)leftParentWidth * ((float)w / (float)totalWidth));
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
                        TextView v = listView.get(j);
                        v.measure(widthMeasureSpec, heightMeasureSpec);
                    }
                    return;
                }
            }
        }
        int totalWidth = 0;
        for(int j = 0; j < listView.size(); ++j){
            totalWidth += listView.get(j).getMeasuredWidth();
        }
        if(totalWidth < parentWidth){
            int leftParentWidth = parentWidth - ((listView.size() - 1) * mMargin
                    + getPaddingLeft() + getPaddingRight());
            for(int j = 0; j < listView.size(); ++j){
                int w = listView.get(j).getMeasuredWidth();
                w = (int)((float)leftParentWidth * ((float)w / (float)totalWidth));
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
                TextView v = listView.get(j);
                v.measure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

}
