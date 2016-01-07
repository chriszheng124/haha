package tools.haha.com.androidtools.demo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tools.haha.com.androidtools.R;
import tools.haha.com.androidtools.ui.CircleDrawable;
import tools.haha.com.androidtools.ui.FlowLayout;
import tools.haha.com.androidtools.ui.MyCustomView_1;
import tools.haha.com.androidtools.ui.MyFlowLayout;
import tools.haha.com.androidtools.ui.MyScrollView;
import tools.haha.com.androidtools.ui.MySurfaceView;
import tools.haha.com.androidtools.ui.RoundedBitmapDrawable;
import tools.haha.com.androidtools.ui.RoundedDrawable;
import tools.haha.com.androidtools.utils.CommonUtils;

public class MyScrollViewActivity extends Activity{
    private static final String TEXT_ARR[] = {"abc", "99999999999999999111111111111112", "1", "a",
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
            if(i == 0) {
                v.addView(createWrapper(createCircleImageView()));
            }else if(i == 1) {
                v.addView(createWrapper(createRoundImageView()));
            }else if(i == 2) {
                v.addView(createWrapper(new MyCustomView_1(this)));
            }else if(i == 3) {
                v.addView(createWrapper(createSurfaceView()));
            }else if(i == 4){
                v.addView(createWrapper(new MyCustomView_1(this)));
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

    private ImageView createRoundImageView(){
        ImageView imageView = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        //imageView.setImageDrawable(new RoundedDrawable(bitmap, 35, 0));
        RoundedBitmapDrawable drawable = new RoundedBitmapDrawable(getResources(), bitmap);
        drawable.setRadius(40);
        drawable.setCircle(false);
        imageView.setImageDrawable(drawable);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return imageView;
    }

    private ImageView createCircleImageView(){
        final ImageView imageView = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        imageView.setImageDrawable(new CircleDrawable(bitmap, 35, 5));

        final ViewTreeObserver.OnPreDrawListener observer = new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0, 1);
                objectAnimator.setDuration(1000);
                objectAnimator.start();
                return false;
            }
        };
        imageView.getViewTreeObserver().addOnPreDrawListener(observer);
//        imageView.animate().alpha(1).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//                imageView.animate().alpha(1).setDuration(3000);
//            }
//        });

        return imageView;
    }

    private View createSurfaceView(){
        MySurfaceView view = new MySurfaceView(this);

        return view;
    }

}
