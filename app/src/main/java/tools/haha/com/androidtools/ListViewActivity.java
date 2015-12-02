package tools.haha.com.androidtools;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import tools.haha.com.androidtools.ui.ListViewAdapter;

public class ListViewActivity extends Activity{
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity_layout);
        mListView = (ListView)findViewById(R.id.my_list_view);
        mListView.setAdapter(new ListViewAdapter(this));
    }
}
