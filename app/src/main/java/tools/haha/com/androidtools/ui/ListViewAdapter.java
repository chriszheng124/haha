package tools.haha.com.androidtools.ui;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> mDataList;

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
}
