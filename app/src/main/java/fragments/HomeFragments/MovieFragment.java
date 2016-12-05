package fragments.HomeFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anke.vedio.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.exception.BmobException;
import fragments.BaseFragment;
import activity.MovieDetailActivity;
import adapter.MovieAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import model.MovieType;
import model.Movies;
/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends BaseFragment {
    private PullLoadMoreRecyclerView mList;
    private ArrayList<Movies> mDates=new ArrayList<>();
    private MovieAdapter adapter;
    private Handler mHandler = new Handler();
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private String lastTime;
    private int curPage = 1;		// 当前页的编号，从0开始 CollectFragment
    private int Pages;
    private int count=60;
    private MovieType movieTypeDate;

    public MovieFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_movie,null);
        initview(layout);
        queryData(0, STATE_REFRESH,false);
        initevent();
        EventBus.getDefault().register(this);
        return layout;
    }
    @Subscribe
    public void onEvent(MovieType movieTypeDate) {
        this.movieTypeDate = movieTypeDate;
        queryData(0, STATE_REFRESH,true);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void initevent() {
        adapter.setOnItemClickListener(new MovieAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Movies data) {
                Intent intent=new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("data",data);
                intent.putExtra("type","movie");
                startActivity(intent);
            }
        });
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
    /**
     * 分页获取数据
     * @param page	页码
     * @param actionType	ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(int page, final int actionType,boolean IsSelect){
        BmobQuery<Movies> query = new BmobQuery<>();
        if(IsSelect){
          if(!movieTypeDate.getMovieScore().equals("")){
              query.addWhereContains("score",""+movieTypeDate.getMovieScore());
          }
            if(!movieTypeDate.getMovieAddress().equals("")){
                query.addWhereContains("MovieInfo",""+movieTypeDate.getMovieScore());
            }
            if(!movieTypeDate.getMovieType().equals("")){
                query.addWhereContains("MovieInfo",""+movieTypeDate.getMovieType());
            }
            if(!movieTypeDate.getMovieYear().equals("")){
                query.addWhereContains("MovieInfo",""+movieTypeDate.getMovieYear());
            }
        }
        query.setLimit(count);
        if(actionType == STATE_MORE){
            query.setSkip(page * count+1); // 跳过之前页数并去掉重复数据
        }else{
            page=0;
            query.setSkip(page);
        }
        query.order("createdAt");
        query.findObjects(new FindListener<Movies>() {
            @Override
            public void done(List<Movies> list, BmobException e) {
                if(e==null){
                    if(list.size()>0){
                        if(actionType == STATE_REFRESH){
                            curPage = 0;
                            mDates.clear();
                        }
                        // 将本次查询的数据添加到bankCards中
                        for (Movies td : list) {
                            Log.i("获取Movie数据=====",""+list.toString());
                            mDates.add(td);
                            mList.setPullLoadMoreCompleted();
                            adapter.notifyDataSetChanged();
                        }
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
//						showToast("第"+(Pages+1)+"页数据加载完成");
                    }else if(actionType == STATE_MORE){
                        Toast("没有更多数据了");
                        mList.setPullLoadMoreCompleted();
                    }else if(actionType == STATE_REFRESH){
                        mDates.clear();
                        adapter.notifyDataSetChanged();
                        Toast("没有数据");
                        mList.setPullLoadMoreCompleted();
                    }
                }else {
                    Toast("数据获取失败");
                    mList.setPullLoadMoreCompleted();
                    Log.i("获取失败====",""+e.toString());
                }
            }
        });
//        Pages=page;
//        startProgressDialog("加载中...");
//        BmobQuery<Movies> query = new BmobQuery<Movies>();
//        query.order("-createdAt");
//        if(actionType == STATE_MORE){
//            query.setSkip(page * count+1); // 跳过之前页数并去掉重复数据
//        }else{
//            page=0;
//            query.setSkip(page);
//        }
//        query.setLimit(count);
//        query.findObjects(new FindListener<Movies>() {
//            @Override
//            public void done(List<Movies> list, BmobException e) {
//                if(e==null){
//                    stopProgressDialog();
//                    if(list.size()>0){
//                        if(actionType == STATE_REFRESH){
//                            curPage = 0;
//                            mDates.clear();
//                            lastTime = list.get(list.size()-1).getCreatedAt();
//                        }
//                        // 将本次查询的数据添加到bankCards中
//                        for (Movies td : list) {
//                            Log.i("获取Movie数据=====",""+list.toString());
//                            mDates.add(td);
//                            mList.setPullLoadMoreCompleted();
//                            adapter.notifyDataSetChanged();
//                        }
//                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
//                        curPage++;
////						showToast("第"+(Pages+1)+"页数据加载完成");
//                    }else if(actionType == STATE_MORE){
//                        Toast("没有更多数据了");
//                        mList.setPullLoadMoreCompleted();
//                    }else if(actionType == STATE_REFRESH){
//                        Toast("没有数据");
//                        mList.setPullLoadMoreCompleted();
//                    }
//                }else {
//                    Toast("获取数据失败，请稍后重试");
//                    mList.setPullLoadMoreCompleted();
//                }
//            }
//        });
    }
    private void initview(View layout) {
        mList = (PullLoadMoreRecyclerView) layout.findViewById(R.id.list);
        mList.setFooterViewText("加载更多...");
        mList.setFooterViewTextColor(R.color.black);
        mList.setGridLayout(3);//参数为列数
        adapter = new MovieAdapter(getActivity(),mDates);
        mList.setAdapter(adapter);

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
                    queryData(0, STATE_REFRESH,false);
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
                    queryData(curPage, STATE_MORE,false);
                }
            });
        }
    }
}
