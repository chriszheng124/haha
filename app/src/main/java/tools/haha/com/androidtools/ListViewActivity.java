package tools.haha.com.androidtools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import tools.haha.com.androidtools.ui.ListViewAdapter;

public class ListViewActivity extends Activity{
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity_layout);
        mListView = (ListView)findViewById(R.id.my_list_view);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                int extraCount = getRefreshableView().getHeaderViewsCount() + getRefreshableView().getFooterViewsCount();
//                if (getChildCount() != extraCount
//                        && mViewAnimator.getDisplayedChild() != NO_MORE_DATA_VIEW
//                        && mViewAnimator.getDisplayedChild() != LOADING_VIEW
//                        && getRefreshableView().getLastVisiblePosition() == view.getCount() - 1) {
//                    waitForLoadingMoreData();
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int scrollY;
//                int pos = firstVisibleItem - getRefreshableView().getHeaderViewsCount();
//                if (pos >= 0) {
//                    scrollY = mAdapter.getScrollY(pos);
//                    if (scrollY >= SCREEN_HEIGHT3X) {
//                        setUpButtonVisibility(true);
//                    } else {
//                        setUpButtonVisibility(false);
//                    }
//                } else {
//                    setUpButtonVisibility(false);
//                }
            }
        });

        mListView.setAdapter(new ListViewAdapter(this));
    }
}
