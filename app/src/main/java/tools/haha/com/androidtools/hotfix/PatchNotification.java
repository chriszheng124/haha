package tools.haha.com.androidtools.hotfix;

public class PatchNotification {

    public static abstract class PatchEvent{

    }

    public static class PatchSuccess extends PatchEvent{

    }

    public static class PatchFail extends PatchEvent{
        private String mMsg;
        public PatchFail(String msg){
            mMsg = msg;
        }

        public String getMessage(){
            return mMsg;
        }
    }
}
