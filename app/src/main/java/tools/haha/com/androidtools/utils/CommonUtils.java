package tools.haha.com.androidtools.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public final class CommonUtils {
    public static final int NET_OFF = 0;    //无网络
    public static final int NET_UNKNOWN = 1;    //未知网络
    public static final int NET_WIFI = 2;    //WIFI网络
    public static final int NET_2G = 4;    //2G网络
    public static final int NET_3G = 8;    //3G网络
    public static final int NET_4G = 16;   //4G网络
    public static final int NET_EXCEPTION = 32;   //获取网络状态失败
    public static final int NET_DEFAULT = NET_WIFI + NET_3G + NET_4G;

    private static int sNavigationBarHeight = Integer.MIN_VALUE;

    public static class OperatorInfo {
        public String mSimMcc;
        public String mSimMnc;
        public String mNetMcc;
        public String mNetMnc;
    }

    public static OperatorInfo getOperatorInfo(Context context) {
        if (context == null)
            return null;
        OperatorInfo mccMncInfo = null;
        try {
            final TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            mccMncInfo = new OperatorInfo();
            {
                String mcc_mnc = tm.getSimOperator();
                StringBuilder mcc;
                StringBuilder mnc;
                if (null != mcc_mnc && mcc_mnc.length() >= 3) {
                    mcc = new StringBuilder();
                    mcc.append(mcc_mnc, 0, 3);
                    mccMncInfo.mSimMcc = mcc.toString();
                    mnc = new StringBuilder();
                    mnc.append(mcc_mnc, 3, mcc_mnc.length());
                    mccMncInfo.mSimMnc = mnc.toString();
                }
            }

            {
                String mcc_mnc = tm.getNetworkOperator();
                StringBuilder mcc;
                StringBuilder mnc;
                if (null != mcc_mnc && mcc_mnc.length() >= 3) {
                    mcc = new StringBuilder();
                    mcc.append(mcc_mnc, 0, 3);
                    mccMncInfo.mNetMcc = mcc.toString();
                    mnc = new StringBuilder();
                    mnc.append(mcc_mnc, 3, mcc_mnc.length());
                    mccMncInfo.mNetMnc = mnc.toString();
                }
            }

        } catch (Throwable e) {
            return mccMncInfo;
        }
        return mccMncInfo;
    }

    public static int dp2px(Context context, float value) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float value) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
                context.getResources().getDisplayMetrics());
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getNetworkType(Context context) {
        String ns = "none";
        int type = _getNetworkType(context);
        switch (type) {
            case NET_2G:
                ns = "2G";
                break;
            case NET_3G:
                ns = "3G";
                break;
            case NET_4G:
                ns = "4G";
                break;
            case NET_WIFI:
                ns = "wifi";
                break;
            case NET_OFF:
            case NET_EXCEPTION:
            case NET_UNKNOWN:
        }
        return ns;
    }

    private static int _getNetworkType(Context context) {
        if (context == null) {
            return NET_UNKNOWN;
        }
        int nReturn = NET_OFF;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                int type = info.getType();
                int subType = info.getSubtype();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    if (isWifiConnected(context)) {
                        nReturn = NET_WIFI;
                    }
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_CDMA: // = 4 ~ 14-64 kbps
                        case TelephonyManager.NETWORK_TYPE_IDEN: // = 11 ~ 25 kbps
                        case TelephonyManager.NETWORK_TYPE_1xRTT: // = 7 2.5G
                            // 2.75G ~
                            // 50-100 kbps
                        case TelephonyManager.NETWORK_TYPE_GPRS: // = 1 2.5G ~ 171.2
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_EDGE: // = 2 2.75G ~
                            // 384-473.6
                            // kbps
                            nReturn = NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0: // = 5 ~ 400-1000
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_UMTS: // = 3 ~ 400-7000
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // = 6 ~ 600-1400
                            // kbps
                        case TelephonyManager.NETWORK_TYPE_HSPA: // = 10 3G ~
                            // 700-1700 kbps
                        case TelephonyManager.NETWORK_TYPE_EHRPD: // = 14 3.75g ~
                            // 1-2 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSUPA: // = 9 ~ 1-23 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSDPA: // = 8 ~ 2-14 Mbps
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: // = 12 ~ 9 Mbps
                        case TelephonyManager.NETWORK_TYPE_HSPAP: // = 15 ~ 10-20
                            // Mbps
                            nReturn = NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE: // = 13 4G ~ 10+
                            // Mbps
                            nReturn = NET_4G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:// = 0
                        default:
                            nReturn = NET_UNKNOWN;
                            break;
                    }
                } else {
                    nReturn = NET_UNKNOWN;
                }
            }
        } catch (Exception e) {
            nReturn = NET_EXCEPTION;
        }
        return nReturn;
    }

    public static boolean isWifiConnected(Context context){
        boolean ret = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } finally {
            if(networkInfo != null){
                ret = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return ret;
    }

    public static int getStatusBarHeight(){
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static int getNavigationBarHeight(Context context) {
        if (sNavigationBarHeight != Integer.MIN_VALUE) {
            return sNavigationBarHeight;
        }
        return (sNavigationBarHeight = getNavigationHeightFromResource(context));
    }

    private static int getNavigationHeightFromResource(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int navigationBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (resourceId > 0) {
            boolean hasNav = resources.getBoolean(resourceId);
            if (hasNav) {
                resourceId = resources.getIdentifier("navigation_bar_height",
                        "dimen", "android");
                if (resourceId > 0) {
                    navigationBarHeight = resources
                            .getDimensionPixelSize(resourceId);
                }
            }
        }

        if (navigationBarHeight <= 0) {
            DisplayMetrics dMetrics = new DisplayMetrics();
            display.getMetrics(dMetrics);
            int screenHeight = Math.max(dMetrics.widthPixels, dMetrics.heightPixels);
            int realHeight = 0;
            try {
                Method mt = display.getClass().getMethod("getRealSize", Point.class);
                Point size = new Point();
                mt.invoke(display, size);
                realHeight = Math.max(size.x, size.y);
            } catch (NoSuchMethodException e) {
                Method mt;
                try {
                    mt = display.getClass().getMethod("getRawHeight");
                } catch (NoSuchMethodException e2) {
                    try {
                        mt = display.getClass().getMethod("getRealHeight");
                    } catch (NoSuchMethodException e3) {
                        return 0;
                    }
                }
                if (mt != null) {
                    try {
                        realHeight = (Integer) mt.invoke(display);
                    } catch (Exception e1) {
                        return 0;
                    }
                }
            } catch (Exception e) {
                return 0;
            }
            navigationBarHeight = realHeight - screenHeight;
        }
        return navigationBarHeight;
    }

    public static Drawable getStateListDrawable(String normalColor, String pressedColor){
        StateListDrawable drawable = new StateListDrawable();
        int pressed = android.R.attr.state_pressed;

        drawable.addState(new int[]{pressed}, new ColorDrawable(Color.parseColor(pressedColor)));
        drawable.addState(new int[]{}, new ColorDrawable(Color.parseColor(normalColor)));

        return drawable;
    }

    public static String getProcessName() {
        File f = new File("/proc/self/cmdline");
        InputStream reader = null;
        String name = "";
        try {
            reader = new FileInputStream(f);
            byte[] buffer = new byte[256];
            int length = reader.read(buffer);
            if (length > 0) {
                name = new String(buffer, 0, length).trim();
            }
        } catch (Exception e) {
            name = "";
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    name = "";
                }
        }
        return name;
    }

    /**
     * Multiplies the color with the given alpha.
     * @param color color to be multiplied
     * @param alpha value between 0 and 255
     * @return multiplied color
     */
    public static int multiplyColorAlpha(int color, int alpha) {
        if (alpha == 255) {
            return color;
        }
        if (alpha == 0) {
            return color & 0x00FFFFFF;
        }
        alpha = alpha + (alpha >> 7); // make it 0..256
        int colorAlpha = color >>> 24;
        int multipliedAlpha = colorAlpha * alpha >> 8;
        return (multipliedAlpha << 24) | (color & 0x00FFFFFF);
    }

}
