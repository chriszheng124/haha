package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tools.haha.com.androidtools.utils.CommonUtils;

@SuppressWarnings("unused")
public class FlowLayout extends LinearLayout {
    private int mRowCount;
    private int mMaxCountEveryRow;
    private int mMinCountEveryRow;
    private int mMargin;
    private List<View> mViewList;
    private LayoutAnimationController mController;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout setRowCount(int count){
        mRowCount = count;
        return this;
    }

    public FlowLayout setMaxCountEveryRow(int count){
        mMaxCountEveryRow = count;
        return this;
    }

    public FlowLayout setMinCountEveryRow(int count){
        mMinCountEveryRow = count;
        return this;
    }

    public FlowLayout setSpaceBetweenItem(int margin){
        mMargin = margin;
        return this;
    }

    public void setLayoutAnimation(Animation animation){
        mController = new LayoutAnimationController(animation);
        mController.setDelay(0);
    }

    public void build(){
        mViewList = new ArrayList<>();
        LinearLayout.LayoutParams firstRowParam = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams layoutParams= new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        int margin = CommonUtils.dp2px(getContext(), mMargin);
        layoutParams.setMargins(0, margin, 0, 0);
        layoutParams.weight = 1;
        firstRowParam.weight = 1;
        setOrientation(VERTICAL);
        for (int i = 0; i < mRowCount; ++i){
            LinearLayout linearLayout = new LinearLayout(getContext());
            if(i == 0){
                linearLayout.setLayoutParams(firstRowParam);
            }else {
                linearLayout.setLayoutParams(layoutParams);
            }
            addView(linearLayout);
        }
    }

    public boolean addChild(View view){
        if(mViewList.size() < mRowCount * mMaxCountEveryRow) {
            mViewList.add(view);
            return true;
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height;
        if(getChildCount() <= 0){
            return;
        }
        View v0 = getChildAt(0);
        height = v0.getMeasuredHeight();
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

        int viewIndex = 0;
        LinearLayout linearLayout;
        reset();
        for(View view : mViewList){
            int widthSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST);
            view.measure(widthSpec, heightSpec);
            linearLayout = (LinearLayout)getChildAt(viewIndex);
            if(mController != null) {
                linearLayout.setLayoutAnimation(mController);
            }
            if(!addViewInLayout(linearLayout, view)){
                if(viewIndex == mRowCount-1){
                    break;
                }
                viewIndex++;
                linearLayout = (LinearLayout)getChildAt(viewIndex);
                if(mController != null) {
                    linearLayout.setLayoutAnimation(mController);
                }
                addViewInLayout(linearLayout, view);
            }
        }
        reMeasure(widthMeasureSpec, heightMeasureSpec);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private boolean addViewInLayout(LinearLayout linearLayout, View view){
        LinearLayout.LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        int margin = CommonUtils.dp2px(getContext(), mMargin);
        layoutParams.setMargins(margin, 0, 0, 0);

        LinearLayout.LayoutParams firstLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        firstLayoutParams.weight = 1;

        if(linearLayout.getChildCount() < mMinCountEveryRow){
            if(0 == linearLayout.getChildCount()){
                linearLayout.addView(view, firstLayoutParams);
            }else {
                linearLayout.addView(view, layoutParams);
            }
            return true;
        }else if(linearLayout.getChildCount() >= mMaxCountEveryRow){
            return false;
        }else {
            int width = view.getMeasuredWidth();
            int usedWidth = 0;
            for(int i = 0; i < linearLayout.getChildCount(); ++i){
                View v = linearLayout.getChildAt(i);
                usedWidth += v.getMeasuredWidth();
                usedWidth += (margin*i);
            }
            usedWidth += (getPaddingLeft() + getPaddingRight());
            if(width + usedWidth + margin < linearLayout.getMeasuredWidth()){
                linearLayout.addView(view, layoutParams);
                return true;
            }
            return false;
        }
    }

    private void reMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int count = getChildCount();
        if(count <= 0){
            return;
        }
        View v0 = getChildAt(0);
        int height = v0.getMeasuredHeight();
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        int parentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        float limit = 1.f/(float)mMaxCountEveryRow;
        for (int i = 0; i < count; ++i){
            LinearLayout linearLayout = (LinearLayout)getChildAt(i);
            int childCount = linearLayout.getChildCount();
            int leftWidth = parentWidth;
            for (int j = 0; j < childCount; ++j){
                TextView tv = (TextView)linearLayout.getChildAt(j);
                int w = tv.getMeasuredWidth();
                if(w < (int)Math.floor((float)parentWidth * limit)){
                    int actualWidth = (int)Math.ceil((float)parentWidth * limit);
                    int widthSpec = MeasureSpec.makeMeasureSpec(actualWidth, MeasureSpec.EXACTLY);
                    tv.measure(widthSpec, heightSpec);
                    int margin = 0;
                    if(j != 0){
                        margin = CommonUtils.dp2px(getContext(), mMargin);
                    }
                    leftWidth -= (tv.getMeasuredWidth() + margin);
                    reMeasure(widthMeasureSpec, heightMeasureSpec);
                }else{
                    int widthSpec = MeasureSpec.makeMeasureSpec(leftWidth, MeasureSpec.AT_MOST);
                    tv.measure(widthSpec, heightSpec);
                    int margin = 0;
                    if(j != 0){
                        margin = CommonUtils.dp2px(getContext(), mMargin);
                    }
                    leftWidth -= (tv.getMeasuredWidth() + margin);
                }
            }
        }
    }

    private void reset(){
        for (int i = 0; i < getChildCount(); ++i){
            LinearLayout linearLayout = (LinearLayout)getChildAt(i);
            linearLayout.removeAllViews();
        }
    }

}