package fragments.HomeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anke.vedio.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import fragments.BaseFragment;
import activity.ShortMovieDeatilActivity;
import adapter.ShortMovieAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import model.ShortMovie;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShortMovieFragment extends BaseFragment {
    private PullLoadMoreRecyclerView mList;
    private ArrayList<ShortMovie> mDates=new ArrayList<>();
    private ShortMovieAdapter adapter;
    private Handler mHandler = new Handler();
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private String lastTime;
    private int curPage = 1;		// 当前页的编号，从0开始
    private int Pages;
    private int count=12;
    public ShortMovieFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_movie_type,null);
        initview(layout);
        queryData(1, STATE_REFRESH);
        initevent();
        return layout;
    }

    private void initevent() {
        adapter.setOnItemClickListener(new ShortMovieAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, ShortMovie data) {
                Intent intent=new Intent(getActivity(), ShortMovieDeatilActivity.class);
                intent.putExtra("data",data);
                intent.putExtra("type","shortmovie");
                startActivity(intent);
            }
        });
    }
    /**
     * 分页获取数据
     * @param page	页码
     * @param actionType	ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(int page, final int actionType){
        Pages=page;
        BmobQuery<ShortMovie> query = new BmobQuery<ShortMovie>();
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
        query.findObjects(new FindListener<ShortMovie>() {
            @Override
            public void done(List<ShortMovie> list, BmobException e) {
                 if(e==null){
                     if(list.size()>0){
                    if(actionType == STATE_REFRESH){
                        curPage = 0;
                        mDates.clear();
                        // 获取最后时间
                        lastTime = list.get(list.size()-1).getCreatedAt();
                    }
                    // 将本次查询的数据添加到bankCards中
                    for (ShortMovie td : list) {
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
                }
                 }else {
                     Toast("获取数据失败，请稍后重试");
                 }
            }
        });
    }
    private void initview(View layout) {
        mList = (PullLoadMoreRecyclerView) layout.findViewById(R.id.list);
        mList.setFooterViewText("加载更多...");
        mList.setFooterViewTextColor(R.color.black);
        mList.setLinearLayout();
        adapter = new ShortMovieAdapter(getActivity(),mDates);
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
