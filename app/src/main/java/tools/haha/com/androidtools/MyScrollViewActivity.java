package tools.haha.com.androidtools;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tools.haha.com.androidtools.ui.FlowLayout;
import tools.haha.com.androidtools.ui.MyCustomView_1;
import tools.haha.com.androidtools.ui.MyFlowLayout;
import tools.haha.com.androidtools.ui.MyScrollView;
import tools.haha.com.androidtools.utils.CommonUtils;

public class MyScrollViewActivity extends Activity{
    private static final String TEXT_ARR[] = {"a", "99999999999999999111111111111112", "1", "a",
                "b999", "sssssss", "dfdfdfdfdfd", "dddddddddd333333333", "eeeeeeeee",
                "dk99999ssssssssss", "d780sssssssss", "dfdfdfdfdfdf", "333333333", "eeeeeeeeeeeeeeeeeeee",
                "dk99999", "d780sssssssss", "dfdfdfdfdf", "3333", "eeeee", "w", "q", "s", "e", "xs", "12w", "end"};

    protected  String NORMAL_COLOR_ARR[] ={"#FF5B64", "#FFC411", "#8074DD", "#4AD37F",
            "#528EFA", "#F4F4F4", "#FF6D01", "#11D6C8", "#3E50B4", "#2DCAFF", "#FF6398", "#A7D049"};

    private String PRESSED_COLOR_ARR[] = {"#CC4850", "#CC9C0D", "#665CB0", "#3BA865",
            "#4171C8", "#C3C3C3", "#CC5700", "#0DABA0", "#314090", "#24A1CC", "#F55F92", "#85A63A"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscroll_view_layout);
        MyScrollView v = (MyScrollView)findViewById(R.id.root);
        for (int i = 0; i<10; i++){
            if(i%2 == 0) {
                MyCustomView_1 child_1 = new MyCustomView_1(this);
                v.addView(createWrapper(child_1));
            }else {
                v.addView(createWrapper(createFlowLayout()));
            }
        }
    }

    private FrameLayout createWrapper(View view_1){
        FrameLayout f1 = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        f1.setLayoutParams(params);
        f1.setPadding(50, 100, 50, 100);
        f1.addView(view_1);

        return f1;
    }

    private MyFlowLayout createFlowLayout(){
        MyFlowLayout flowLayout= new MyFlowLayout(this);
        flowLayout.setRowCount(4)
                .setSpaceBetweenItem(10)
                .setMaxCountEveryRow(3)
                .setMinCountEveryRow(2)
                .create();
        int padding = CommonUtils.dp2px(this, 10);
        flowLayout.setPadding(padding, 0, padding, padding);
        for (int i = 0; i < 12; ++i){
            TextView view = createTextView(i);
            if(view != null) {
                flowLayout.addTextView(view);
            }
        }
        return flowLayout;
    }

    protected TextView createTextView(int index) {
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText(TEXT_ARR[index]);
        tv.setSingleLine();
        //tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setPadding(CommonUtils.dp2px(this, 10),
                0,
                CommonUtils.dp2px(this, 10),
                0);
        tv.setBackgroundDrawable(CommonUtils.getStateListDrawable(NORMAL_COLOR_ARR[index],
                PRESSED_COLOR_ARR[index]));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        return tv;
    }

}
