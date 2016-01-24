package tools.haha.com.androidtools.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import tools.haha.com.androidtools.R;

public class DrawerActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        View view = LayoutInflater.from(this).inflate(R.layout.test_inflate_layout, null);
        frameLayout.addView(view);
    }
}
