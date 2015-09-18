package tools.haha.com.androidtools;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import tools.haha.com.androidtools.ui.MyCustomView_1;
import tools.haha.com.androidtools.ui.MyScrollView;

public class MyScrollViewActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscroll_view_layout);
        MyScrollView v = (MyScrollView)findViewById(R.id.root);
        for (int i = 0; i<10; i++){
            MyCustomView_1 child_1 = new MyCustomView_1(this);
            v.addView(createWrapper(child_1));
        }
    }

    FrameLayout createWrapper(MyCustomView_1 view_1){
        FrameLayout f1 = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        f1.setLayoutParams(params);
        f1.setPadding(100, 100, 100, 100);
        f1.addView(view_1);

        return f1;
    }

}
