package fragments;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.anke.vedio.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import java.util.ArrayList;
import java.util.List;

import adapter.HistoryAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import model.history;
/**
 * A simple {@link Fragment} subclass.  DownLoadFragment
 */
public class HistoryFragment extends BaseFragment {
    private PullLoadMoreRecyclerView mList;
    private ArrayList<history> mDates=new ArrayList<>();
    private HistoryAdapter adapter;
    private Handler mHandler = new Handler();
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private String lastTime;
    private int curPage = 1;		// 当前页的编号，从0开始 CollectFragment
    private int Pages;
    private int count=12;
    private RelativeLayout mRtlNoDate;

    public HistoryFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_collect,null);
        initview(layout);
        queryData(1, STATE_REFRESH);
        initevent();
        return layout;
    }
    private void initevent() {
//        adapter.setOnItemClickListener(new HistoryAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, history data) {
//                Intent intent=new Intent(getActivity(), MovieDetailActivity.class);
//                intent.putExtra("data",data);
//                intent.putExtra("his",true);
//                intent.putExtra("type","history");
//                startActivity(intent);
//            }
//        });
    }
    /**
     * 分页获取数据
     * @param page	页码
     * @param actionType	ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(int page, final int actionType){
        Pages=page;
        BmobQuery<history> query = new BmobQuery<history>();
        query.order("-createdAt");
        // 如果是加载更多
        if(actionType == STATE_MORE){
            query.setSkip(page * count+1); // 跳过之前页数并去掉重复数据
        }else{
            page=0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(count);
        // 查找数据
        query.findObjects(new FindListener<history>() {
            @Override
            public void done(List<history> list, BmobException e) {
                if(e==null){
                    if(list.size()>0){
                    mRtlNoDate.setVisibility(View.GONE);
                    mList.setVisibility(View.VISIBLE);
                    if(actionType == STATE_REFRESH){
                        curPage = 0;
                        mDates.clear();
                        // 获取最后时间
                        lastTime = list.get(list.size()-1).getCreatedAt();
                    }
                    // 将本次查询的数据添加到bankCards中
                    for (history td : list) {
                        mDates.add(td);
                        adapter.notifyDataSetChanged();
                        mList.setPullLoadMoreCompleted();
                    }
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
//						showToast("第"+(Pages+1)+"页数据加载完成");
                }else if(actionType == STATE_MORE){
                    Toast("没有更多数据了");
                    mList.setPullLoadMoreCompleted();
                }else if(actionType == STATE_REFRESH){
                    Toast("没有数据");
                    mList.setPullLoadMoreCompleted();
                    mList.setVisibility(View.GONE);
                    mRtlNoDate.setVisibility(View.VISIBLE);
                }
                }else {
                    Toast("获取数据失败，请稍后重试");
                }
            }
        });
    }
    private void initview(View layout) {
        mRtlNoDate = (RelativeLayout) layout.findViewById(R.id.rtl_nodate);
        mList = (PullLoadMoreRecyclerView) layout.findViewById(R.id.list);
        mList.setFooterViewText("加载更多...");
        mList.setFooterViewTextColor(R.color.black);
        mList.setLinearLayout();
        adapter = new HistoryAdapter(getActivity(),mDates);
        mList.setAdapter(adapter);
// 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        mList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Thread(new getMoreDate()).start();
            }
            @Override
            public void onLoadMore() {
                new Thread(new LoadMoreDate()).start();
            }
        });
    }
    class getMoreDate implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    queryData(0, STATE_REFRESH);
                    adapter.notifyDataSetChanged();
                    mList.setPullLoadMoreCompleted();
                }
            });
        }
    }
    class LoadMoreDate implements Runnable {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    queryData(curPage, STATE_MORE);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
