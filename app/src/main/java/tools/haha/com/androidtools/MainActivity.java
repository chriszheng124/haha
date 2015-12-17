package tools.haha.com.androidtools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import tools.haha.com.androidtools.ui.RoundedBitmapDrawable;
import tools.haha.com.dynamicload.Plugin;


public class MainActivity extends Activity{
    private Plugin mPlugin;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        final Button startListViewBtn = (Button)findViewById(R.id.start_list_view);
        startListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ListViewActivity.class);
            }
        });

        Button pluginBtn = (Button)findViewById(R.id.load_plugin_btn);
        pluginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlugin = new Plugin(MainActivity.this, "/sdcard/app-debug.apk");
                mPlugin.load();
                Intent intent = new Intent();
                //intent.setClassName("tools.haha.com.plugin_1", "PluginMainActivity");
                intent.setClassName(MainActivity.this, "tools.haha.com.plugin_1.PluginMainActivity");
                try{
                    startActivity(intent);
                }catch (Exception e){
                    Log.v("test_11", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
