package tools.haha.com.androidtools.demo;

import android.util.Log;

import java.util.List;

public class JavaDemo {
    public void fun(){
        Log.v("test_11", "fun got called");
    }

    public static void fun1(List<? extends List<String>> param){
        Log.v("test_11", "classLoader in fun1: " + JavaDemo.class.getClassLoader().getParent());
    }

}
