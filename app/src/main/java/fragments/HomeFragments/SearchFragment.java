package fragments.HomeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anke.vedio.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activity.MovieDetailActivity;
import activity.ShortMovieDeatilActivity;
import activity.TvPlayDetailActivity;
import adapter.HistoryAdapter;
import adapter.MovieAdapter;
import adapter.SearchAdapter;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import fragments.BaseFragment;
import model.Movies;
import model.ShortMovie;
import model.TvPlay;
import model.history;
import model.look;


public class SearchFragment extends BaseFragment {
    private PullLoadMoreRecyclerView mList;
    private ArrayList<BmobObject> mDates=new ArrayList<>();
    private SearchAdapter adapter;
    private Handler mHandler = new Handler();
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private String lastTime;
    private int curPage = 1;		// 当前页的编号，从0开始 CollectFragment
    private int Pages;
    private int count=50;
    private RelativeLayout mRtlNoDate;

    private TextView queryData;
    private ImageView query;


    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_search,null);
        initview(layout);
        initevent();
        return layout;
    }

    private void queryData()
    {
        mList.setVisibility(View.INVISIBLE);
        mRtlNoDate.setVisibility(View.INVISIBLE);
        mDates.clear();
        if(queryData.getText().toString().trim().isEmpty())
        {
            Toast("请输入搜索的关键字");
            return;
        }
        queryMovieData();
        queryTvPlayData();
        queryShortMovie();
    }

    private void queryMovieData(){
        String   moviename= queryData.getText().toString().trim();
        BmobQuery<Movies> query = new BmobQuery<Movies>();
        query.order("-createdAt");
        query.addWhereContains("moviename",moviename);

        // 查找数据
        query.findObjects(new FindListener<Movies>() {
            @Override
            public void done(List<Movies> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mList.setVisibility(View.VISIBLE);
                        for (Movies td : list) {
                            mDates.add(td);
                            adapter.notifyDataSetChanged();
                            mList.setPullLoadMoreCompleted();
                        }
                    } else {
                        Log.i("queryMovieData", "查询成功，无数据返回");
                    }
                    setVisible();
                } else {
                    Toast("获取数据失败,错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });

    }

    private void queryTvPlayData(){
        String   moviename= queryData.getText().toString().trim();
        BmobQuery<TvPlay> query = new BmobQuery<TvPlay>();
        query.order("-createdAt");
        query.addWhereContains("moviename",moviename);

        // 查找数据
        query.findObjects(new FindListener<TvPlay>() {
            @Override
            public void done(List<TvPlay> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mList.setVisibility(View.VISIBLE);
                        for (TvPlay td : list) {
                            mDates.add(td);
                            adapter.notifyDataSetChanged();
                            mList.setPullLoadMoreCompleted();
                        }
                    } else {
                        Log.i("queryTvPlay", "查询成功，无数据返回");
                    }
                    setVisible();
                } else {
                    Toast("获取数据失败,错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    private void queryShortMovie(){
        String   moviename= queryData.getText().toString().trim();
        BmobQuery<ShortMovie> query = new BmobQuery<ShortMovie>();
        query.order("-createdAt");
        query.addWhereContains("moviename",moviename);

        // 查找数据
        query.findObjects(new FindListener<ShortMovie>() {
            @Override
            public void done(List<ShortMovie> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mList.setVisibility(View.VISIBLE);
                        for (ShortMovie td : list) {
                            mDates.add(td);
                            adapter.notifyDataSetChanged();
                            mList.setPullLoadMoreCompleted();
                        }
                    } else {
                        Log.i("queryTvPlay", "查询成功，无数据返回");
                    }
                    setVisible();
                } else {
                    Toast("获取数据失败,错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }



    private void initevent() {
        adapter.setOnItemClickListener(new SearchAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, BmobObject data) {
                if(data instanceof Movies) {
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("data", data);
                    intent.putExtra("his", false);
                    intent.putExtra("type", "history");
                    startActivity(intent);
                }
                else if(data instanceof TvPlay)
                {
                    Intent intent = new Intent(getActivity(), TvPlayDetailActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
                else if(data instanceof ShortMovie)
                {
                    Intent intent = new Intent(getActivity(), ShortMovieDeatilActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
            }
        });
    }

    private void initview(View layout) {
        mRtlNoDate = (RelativeLayout) layout.findViewById(R.id.rtl_nodate);
        queryData = (TextView) layout.findViewById(R.id.et_querydata);
        query = (ImageView) layout.findViewById(R.id.img_search);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryData();
            }
        });

        mList = (PullLoadMoreRecyclerView) layout.findViewById(R.id.list);
        mList.setFooterViewText("没有更多数据");
        mList.setFooterViewTextColor(R.color.black);
        mList.setLinearLayout();
        adapter = new SearchAdapter(getActivity(),mDates);
        mList.setAdapter(adapter);
        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        mList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                new Thread(new getMoreDate()).start();
            }
            @Override
            public void onLoadMore() {
                mList.setPullLoadMoreCompleted();
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
                    queryData();
                    adapter.notifyDataSetChanged();
                    mList.setPullLoadMoreCompleted();
                }
            });
        }
    }

    void setVisible()
    {
        if(mList.getVisibility()==View.VISIBLE)
            mRtlNoDate.setVisibility(View.INVISIBLE);
        else
            mRtlNoDate.setVisibility(View.VISIBLE);
    }



}
