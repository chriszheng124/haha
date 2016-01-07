package tools.haha.com.androidtools.hotfix;

public class HotfixException extends Exception{
    public static final int UNKNOWN = 0;
    public static final int LOAD_CLASS_ERROR = 1;
    public static final int LOAD_APK_ERROR = 2;
    public static final int APK_FILE_NOT_EXIST = 3;
    public static final int LOAD_DEX_FILE_ERROR = 4;

    private int mType;

    public HotfixException(String detailMessage) {
        super(detailMessage);
        mType = UNKNOWN;
    }

    public HotfixException(String detailMessage, int type) {
        super(detailMessage);
        mType = type;
    }

    public int getType(){
        return mType;
    }

}
