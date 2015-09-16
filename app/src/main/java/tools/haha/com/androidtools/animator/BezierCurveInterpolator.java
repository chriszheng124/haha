package tools.haha.com.androidtools.animator;

import android.view.animation.Interpolator;

/**
 * Bezier curve interpolator
 * #import from https//chromium.googlesource.com/chromium/src/+/22a0fbd00117465e9d451cc4d1758e8050052661/cc/animation/timing_function.cc
 */

@SuppressWarnings("unused")
public class BezierCurveInterpolator implements Interpolator {
    private static final int MAX_STEPS = 30;
    private static final double BEZIER_EPSILON = 1e-7;

    private float mX1;
    private float mY1;
    private float mX2;
    private float mY2;

    public BezierCurveInterpolator(float x1, float y1, float x2, float y2){
        mX1 = Math.min(Math.max(x1, 0.0f), 1.0f);
        mX2 = Math.min(Math.max(x2, 0.0f), 1.0f);
        mY1 = y1;
        mY2 = y2;
    }

    @Override
    public float getInterpolation(float x) {
        x = Math.min(Math.max(x, 0.0f), 1.0f);
        // Step 1. Find the t corresponding to the given x. I.e., we want t such that
        // eval_bezier(x1, x2, t) = x. There is a unique solution if x1 and x2 lie
        // within (0, 1).
        //
        // We're just going to do bisection for now (for simplicity), but we could
        // easily do some newton steps if this turns out to be a bottleneck.
        double t = 0.0;
        double step = 1.0;
        for (int i = 0; i < MAX_STEPS; ++i, step *= 0.5) {
            final double error = evalBezier(t, mX1, mX2) - x;
            if (Math.abs(error) < BEZIER_EPSILON) {
                break;
            }
            t += error > 0.0 ? -step : step;
        }
        // Step 2. Return the interpolated y values at the t we computed above.
        return (float)evalBezier(t, mY1, mY2);
    }

    private double evalBezier(double t, float v1, float v2) {
        final double x1_times_3 = 3.0 * v1;
        final double x2_times_3 = 3.0 * v2;
        final double h1 = x1_times_3 - x2_times_3 + 1.0;
        final double h2 = x2_times_3 - 6.0 * v1;
        return t * (t * (t * h1 + h2) + x1_times_3);
    }
}
