package tools.haha.com.androidtools.utils;

public class DumpStackTrace {

    public static String dump(StackTraceElement[] stackTraceElements){
        if(stackTraceElements == null || stackTraceElements.length == 0){
            return "";
        }
        String sep= System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTraceElements){
            sb.append(element.toString()).append(sep);
        }
        return sb.toString();
    }
}
