package tools.haha.com.androidtools.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tools.haha.com.androidtools.R;

public class TestActivity_1 extends Activity{
    private TestThread mThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.v("123---", intent.toString());
        mThread = new TestThread();
        mThread.setPriority(Thread.MIN_PRIORITY);
        mThread.setName("testThread");
        mThread.init(this);
        setContentView(R.layout.test_activity_1_layout);

        final View view = findViewById(R.id.tv_3);
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
                //mThread.start();
            }
        });
        Button button_4 = (Button)findViewById(R.id.stopThread);
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mThread.interrupt();

                //view.setVisibility(View.VISIBLE);

                Intent intent = new Intent();
                intent.setClass(TestActivity_1.this, TestActivity.class);
                intent.putExtra("aaaaaaaaaa", "123aws");
                setResult(10000, intent);
                finish();
            }
        });
        final ViewDemo viewDemo = (ViewDemo)findViewById(R.id.view_demo);
        viewDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDemo.setVisibility(View.INVISIBLE);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
