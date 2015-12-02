package tools.haha.com.androidtools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import tools.haha.com.androidtools.ui.RoundedBitmapDrawable;


public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pri = Looper.getMainLooper().getThread().getPriority();
        Log.v("test_11", ""+pri);
        setContentView(R.layout.main_activity_layout);
        final ImageView imageView = (ImageView)findViewById(R.id.img_view_1);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        RoundedBitmapDrawable roundedBitmapDrawable = new RoundedBitmapDrawable(getResources(), bitmap);
        roundedBitmapDrawable.setRadius(55);
        imageView.setImageDrawable(roundedBitmapDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TestActivity.class);
            }
        });
        final Button button_1 = (Button)findViewById(R.id.scroll_view_button);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyScrollViewActivity.class);
            }
        });

//        Button button_2 = (Button)findViewById(R.id.send_broadcast);
//        button_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        Button startListViewBtn = (Button)findViewById(R.id.start_list_view);
        startListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ListViewActivity.class);
            }
        });

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
