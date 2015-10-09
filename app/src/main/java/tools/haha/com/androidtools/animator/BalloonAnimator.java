package tools.haha.com.androidtools.animator;

import android.animation.ValueAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.Interpolator;


import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BalloonAnimator {
    private static final float BALL_ROTATION = 360;
    private static final float ROPE_ROTATION = 540;
    private static final long ROTATION_TIME = 1600; //ms
    private static final long[] TIME_VALUE = {920, 880, 850, 810, 750, 700, 500, 500}; //ms
    private static final float[] LINE_PROPERTY_VALUE = {0, 10, -8, 7, -5.5f, 3.5f, -2, 0.6f, 0};
    private static final float[] BALLOON_PROPERTY_VALUE = {0, 3, -2.5f, 2, -1.5f, 1.1f, -0.4f, 0.2f, 0};

    private AnimatorSet mSet = new AnimatorSet();
    private boolean mAlreadySwitched;
    private View /*BalloonView*/ mView;

    public void start(View /*BalloonView*/ view,
                      View container,
                      View balloonView,
                      View ropeView,
                      View handleView) {
        if (mSet.isStarted() || mSet.isRunning()) {
            return;
        }
        mView = view;
        mAlreadySwitched = false;
        mSet = new AnimatorSet();
        Animator animator = createBalloonRotationAnimator(balloonView);
        Animator animator1 = createHandleRotationAnimator(handleView);

        container.setPivotX(balloonView.getX() + balloonView.getWidth() / 2);
        container.setPivotY(0);
        Animator animator2 = createSwingAnimator(container, ropeView);
        mSet.play(animator).with(animator1).with(animator2);
        mSet.start();
    }

    /**
     * 创建气球旋转动画
     */
    private Animator createBalloonRotationAnimator(final View target) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
        animator.setDuration(ROTATION_TIME);
        BezierCurveInterpolator interpolator = new BezierCurveInterpolator(0.2f, 0, 0.25f, 1);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (Float) animation.getAnimatedValue();
                target.setRotationY(fraction * BALL_ROTATION);
                if (!mAlreadySwitched && Float.compare(fraction, 0.75f) >= 0) {
                    mAlreadySwitched = true;
                    //BalloonController.getInstance().switchBulletinContent();
                }
            }
        });

        return animator;
    }

    /**
     * 创建环旋转动画
     */
    private Animator createHandleRotationAnimator(final View target) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
        animator.setDuration(ROTATION_TIME);
        BezierCurveInterpolator interpolator = new BezierCurveInterpolator(0.35f, 0, 0.5f, 1);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (Float) animation.getAnimatedValue();
                target.setRotationY(fraction * ROPE_ROTATION);
            }
        });

        return animator;
    }

    /**
     * 创建绳子摇摆动画
     */
    private Animator createSwingAnimator(View container, View ropeView) {
        ropeView.setPivotX(ropeView.getWidth() / 2);
        ropeView.setPivotY(0);
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animatorList = new ArrayList<>(BALLOON_PROPERTY_VALUE.length);
        for (int i = 0; i < TIME_VALUE.length; ++i) {
            Animator animator = createSwingAnimator(container, ropeView,
                    TIME_VALUE[i], i);
            animatorList.add(animator);
        }
        animatorSet.playSequentially(animatorList);
        return animatorSet;
    }

    private Animator createSwingAnimator(final View container,
                                         final View ropeView,
                                         long duration,
                                         final int index) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(duration);
        Interpolator interpolator = new BezierCurveInterpolator(0.35f, 0, 0.5f, 1);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (Float) animation.getAnimatedValue();
                if (Float.compare(fraction, 1.0f) == 0) {
                    animator.removeAllUpdateListeners();
                }
                float ballFromDegree = BALLOON_PROPERTY_VALUE[index];
                float ballToDegree = BALLOON_PROPERTY_VALUE[index + 1];
                float ballDegree;
                if (Float.compare(0, fraction) == 0) {
                    ballDegree = ballFromDegree;
                } else {
                    ballDegree = ballFromDegree + fraction * (ballToDegree - ballFromDegree);
                }
                container.setRotation(ballDegree);
                float ropeFromDegree = LINE_PROPERTY_VALUE[index];
                float ropeToDegree = LINE_PROPERTY_VALUE[index + 1];
                float ropeDegree;
                if (Float.compare(0, fraction) == 0) {
                    ropeDegree = ropeFromDegree;
                } else {
                    ropeDegree = ropeFromDegree + fraction * (ropeToDegree - ropeFromDegree);
                }
                ropeView.setRotation(ropeDegree - ballDegree);
            }
        });
        return animator;
    }
}