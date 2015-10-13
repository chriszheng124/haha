package tools.haha.com.androidtools.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

@SuppressWarnings("unused")
public class SdcardLogger {
    private Handler mHandler;
    private static SdcardLogger mThis;
    private FileOutputStream mFile;

    private SdcardLogger(Context context, String filename){
        HandlerThread thr = new HandlerThread("logger");
        thr.start();
        mHandler = new Handler(thr.getLooper());
//        String path = CommonUtils.getInternalSdcardPath(context);
//        if (TextUtils.isEmpty(path)) {
//            path = CommonUtils.getExternalSdcardPath(context);
//        }
//        try {
//            mFile = new FileOutputStream(new File(path, filename));
//        } catch (FileNotFoundException e) {
//        }
    }

    synchronized public static SdcardLogger getInstance(Context context, String filename) {
        if (mThis == null)
            mThis = new SdcardLogger(context, filename);
        return mThis;
    }

    public void Log(final String str){
        mHandler.post(new Runnable(){

            @Override
            public void run() {
                doPrint(str);
            }

        });
    }

    private void doPrint(String str){
        if(mFile != null)
            try {
                StringBuilder sb = new StringBuilder();

                Date date=new Date();
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time=df.format(date);
                sb.append(time);
                sb.append("     ");
                sb.append(str);
                sb.append(System.getProperty("line.separator"));
                mFile.write(sb.toString().getBytes());
            } catch (IOException e) {
            }
    }

}
