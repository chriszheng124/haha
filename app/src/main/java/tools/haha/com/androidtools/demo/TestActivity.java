package tools.haha.com.androidtools.demo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import tools.haha.com.androidtools.R;
import tools.haha.com.androidtools.ui.MyCustomView_1;

public class TestActivity extends Activity{
    private final static String PREF_1 = "pref_1";
    private final static String PREF_2 = "pref_2";

    private SharedPreferences mPref1;
    private SharedPreferences mPref2;

    private ViewFlipper mFlipper;

    private Animator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref1 = getSharedPreferences(PREF_1, MODE_PRIVATE);
        mPref1.edit().putBoolean("enabled", true).apply();

        mPref2 = getSharedPreferences(PREF_2, MODE_PRIVATE);
        mPref2.edit().putBoolean("enabled", true).apply();

        setContentView(R.layout.test_activity_layout);

        final MyCustomView_1 view_1 = (MyCustomView_1)findViewById(R.id.my_custom_view_1);

        Button button_1 = (Button)findViewById(R.id.start_test_activity_1);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(TestActivity.this, TestActivity_1.class.getName());
                intent.putExtra("qqq", "123");
                startActivityForResult(intent, 10);
                //startActivity(TestActivity_1.class);
                //view_1.animate().withLayer().alpha(1).alpha(0).setDuration(1000).start();
            }
        });
        //-------------------------------------------
        mFlipper = (ViewFlipper)findViewById(R.id.flipper);
        init();
        Button button = (Button)findViewById(R.id.switch_page_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipper.showNext();
            }
        });
        //-------------------------------------------
        FrameLayout container = (FrameLayout)findViewById(R.id.page_container);
        final TextView tv1 = new TextView(this);
        tv1.setGravity(Gravity.CENTER);
        tv1.setText("11111111");
        tv1.setBackgroundColor(Color.parseColor("#FF5B64"));
        container.addView(tv1);
        Button button2 = (Button)findViewById(R.id.switch_page_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change(tv1);
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimator.setDuration(0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void change(View view){
//        Animation animation = createAnimation(false, 3000);
//        view.startAnimation(animation);

        mAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, view.getHeight());
        mAnimator.setDuration(3000);
        mAnimator.start();
    }

    private void init(){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv1 = new TextView(this);
        tv1.setGravity(Gravity.CENTER);
        tv1.setText("11111111");
        tv1.setBackgroundColor(Color.parseColor("#FF5B64"));
        mFlipper.addView(tv1, layoutParams);

        TextView tv2 = new TextView(this);
        tv2.setText("22222222");
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundColor(Color.parseColor("#FFC411"));
        mFlipper.addView(tv2, layoutParams);

        TextView tv3 = new TextView(this);
        tv3.setText("33333333");
        tv3.setGravity(Gravity.CENTER);
        tv3.setBackgroundColor(Color.parseColor("#CC9C0D"));
        mFlipper.addView(tv3, layoutParams);

        TextView tv4 = new TextView(this);
        tv4.setGravity(Gravity.CENTER);
        tv4.setText("44444444");
        tv4.setBackgroundColor(Color.parseColor("#4AD37F"));
        mFlipper.addView(tv4, layoutParams);

        mFlipper.setInAnimation(createAnimation(true, 2000));
        mFlipper.setOutAnimation(createAnimation(false, 2000));
    }

    public static Animation createAnimation(boolean inAnimator, long duration){
        AnimationSet animation = new AnimationSet(true);
        TranslateAnimation translateAnimation;
        AlphaAnimation alphaAnimation;
        if(inAnimator){
            translateAnimation = new TranslateAnimation(
                    Animation.ABSOLUTE, 0f,
                    Animation.ABSOLUTE, 0f,
                    Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0f);

            alphaAnimation = new AlphaAnimation(0, 1);
            //animation.addAnimation(alphaAnimation);
            animation.addAnimation(translateAnimation);

        }else{
            translateAnimation = new TranslateAnimation(
                    Animation.ABSOLUTE, 0f,
                    Animation.ABSOLUTE, 0f,
                    Animation.RELATIVE_TO_PARENT, 0f,
                    Animation.RELATIVE_TO_PARENT, 1.0f);

            alphaAnimation = new AlphaAnimation(1, 0);
            animation.addAnimation(translateAnimation);
        }
        animation.setDuration(duration);
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        animation.setInterpolator(interpolator);

        return animation;
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
