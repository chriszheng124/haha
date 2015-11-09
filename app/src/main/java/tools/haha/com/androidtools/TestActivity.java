package tools.haha.com.androidtools;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TestActivity extends Activity{
    private final static String PREF_1 = "pref_1";
    private final static String PREF_2 = "pref_2";

    private SharedPreferences mPref1;
    private SharedPreferences mPref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref1 = getSharedPreferences(PREF_1, MODE_PRIVATE);
        mPref1.edit().putBoolean("enabled", true).apply();

        mPref2 = getSharedPreferences(PREF_2, MODE_PRIVATE);
        mPref2.edit().putBoolean("enabled", true).apply();

        setContentView(R.layout.test_activity_layout);
        Button button_1 = (Button)findViewById(R.id.start_test_activity_1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TestActivity_1.class);
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
        Log.v("dd", "dddddddd");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String s = "2s9wl23";
        outState.putString("test_11", s);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
