package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> mDataList;
    private HashMap<Integer, Integer> mViewHeightMap;

    public ListViewAdapter(Context context){
        mContext = context;
        mDataList = new ArrayList<>(20);
        for (int i = 0; i < 20; ++i){
            mDataList.add("Name_" + i);
        }
    }

    private View createTextView(int index){
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, 50, 0, 50);
        return tv;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.v("test_11", "" + position);
        if(convertView == null){
            convertView = createTextView(position);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.view = convertView;
            ((TextView)convertView).setText(mDataList.get(position));
            convertView.setTag(viewHolder);
        }else{
            ((TextView)convertView).setText(mDataList.get(position));
        }

//        if(mViewHeightMap.containsKey(position-1)){
//            totalHeight = mViewHeightMap.get(position-1);
//        }
//        if(!mViewHeightMap.containsKey(position)) {
//            mViewHeightMap.put(position, currentItemHeight + totalHeight);
//        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    class ViewHolder{
        View view;
    }

//    public int getScrollY(int position){
//        if(mViewHeightMap.containsKey(position)){
//            return mViewHeightMap.get(position);
//        }
//        int currentItemHeight;
//        NewsData data = mProvider.loadNewsDataByPosition(position);
//        if(data instanceof INewsProxy.News){
//            INewsProxy.News newsItem = (INewsProxy.News)(data);
//            if(TextUtils.isEmpty(newsItem.mUrl)){
//                currentItemHeight = TEXTURE_NEWS_VIEW_HEIGHT;
//            }else if(newsItem.mDisplayType.equalsIgnoreCase(INewsProxy.News.DISPLAY_TYPE_SMALL)){
//                currentItemHeight = NORMAL_NEWS_VIEW_HEIGHT;
//            }else if(newsItem.mDisplayType.equalsIgnoreCase(INewsProxy.News.DISPLAY_TYPE_BIG)){
//                currentItemHeight = LARGE_PIC_NEWS_VIEW_HEIGHT;
//            }else {
//                currentItemHeight = 0;
//            }
//        }else if(data instanceof INativeAd){
//            currentItemHeight = NORMAL_NEWS_VIEW_HEIGHT;
//        }else {
//            currentItemHeight = 0;
//        }
//        return currentItemHeight;
//    }

}
