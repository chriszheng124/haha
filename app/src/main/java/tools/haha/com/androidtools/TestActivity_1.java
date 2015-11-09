package tools.haha.com.androidtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestActivity_1 extends Activity{
    private TestThread mThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThread = new TestThread();
        mThread.setPriority(Thread.MIN_PRIORITY);
        mThread.setName("testThread");
        mThread.init(this);
        setContentView(R.layout.test_activity_1_layout);
        Button button_2 = (Button)findViewById(R.id.start_test_activity);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity_1.this, TestActivity.class));
            }
        });
        Button button_3 = (Button)findViewById(R.id.startThread);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThread.start();
            }
        });
        Button button_4 = (Button)findViewById(R.id.stopThread);
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThread.interrupt();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
