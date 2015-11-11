package tools.haha.com.androidtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class JavaDemo {
    public static void fun1(List<? extends List<String>> param){
        List<String> s = param.get(0);
        List<String> l1 = new ArrayList<>(10);
        Queue<String> q1 = new LinkedBlockingQueue<>();
    }
}
