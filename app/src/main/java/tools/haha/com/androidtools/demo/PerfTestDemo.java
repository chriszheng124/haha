package tools.haha.com.androidtools.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import tools.haha.com.androidtools.R;

public class PerfTestDemo extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perf_test);
        findViewById(R.id.btn_long_runnable).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_long_runnable:
                startLongRunnable();
                break;
        }
    }

    private void startLongRunnable(){
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                }
            }
        });
    }
}
