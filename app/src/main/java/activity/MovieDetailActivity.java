package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anke.vedio.R;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.UpdateListener;
import fragments.DownLoadFragment;
import utis.UILUtils;
import utis.utis;
import adapter.DownloadFileListAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import model.Collect;
import model.Movies;
import model.history;
import vedio.FullScreenActivity;

/**
 * Created by Administrator on 2016/11/1.
 */

public class MovieDetailActivity extends BaseActivity {
    private DownloadFileListAdapter mDownloadFileListAdapter;
    private Movies movie;
    private Collect collect;
    private history history;
    private ImageView mImgIcon;
    private TextView mTvInfo;
    private TextView mTvPlot;
    private LinearLayout mLlDownload;
    private TextView mTvDownLoad;
    private ImageView mImgCloseDownload;
    private TextView mTvXz;
    private TextView mTvCollect;
    private TextView mTvPlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initdate();
        initview();
        initevent();
//        inithistorymovie();
    }



    private void initdate() {
        movie = (Movies) getIntent().getSerializableExtra("data");
    }

    private void initview() {
        mTvPlay = (TextView) findViewById(R.id.tv_play);
        mTvCollect = (TextView) findViewById(R.id.tv_collect);
        mTvXz = (TextView) findViewById(R.id.tv_xz);
        mLlDownload = (LinearLayout) findViewById(R.id.ll_download);
        mTvDownLoad = (TextView) findViewById(R.id.tv_download);
        mImgCloseDownload = (ImageView) findViewById(R.id.img_close_download);

        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvPlot = (TextView) findViewById(R.id.tv_plot);
        mImgIcon = (ImageView) findViewById(R.id.img_icon);
        movie = (Movies) getIntent().getSerializableExtra("data");
        UILUtils.displayImageNoAnim(movie.getImageUrl(), mImgIcon);
        mTvPlot.setText("" + movie.getPlot());
        mTvInfo.setText("" + movie.getMovieInfo());
    }

    /**
     * 添加播放记录
     */
    private void inithistorymovie() {
        if (!getIntent().getBooleanExtra("his", false)) {
            BmobQuery<history> query = new BmobQuery<>();
            query.addWhereEqualTo("Userid", "" + utis.GetId(getApplicationContext()));
            switch (getIntent().getStringExtra("type")) {
                case "movie":
                    query.addWhereEqualTo("MovieId", "" + movie.getObjectId());
                    break;
                case "shortmovie":
//                    query.addWhereEqualTo("MovieId",""+shortMoive.getObjectId());
                    break;
            }
            query.findObjects(new FindListener<model.history>() {
                @Override
                public void done(List<history> list, BmobException e) {
                     if(e==null){
                         if (list.size() == 0) {
                        AddToHistory();
                        }
                     }else {
                         Toast("数据获取失败，请稍后重试");
                     }
                }
            })      ;
        }
    }

    /**
     * 添加到历史记录里面
     */
    private void AddToHistory() {
        history collect = new history();
        collect.setUserid(utis.GetId(getApplicationContext()));
        collect.setMovieId(movie.getObjectId() + "");
        collect.setName(movie.getMoviename());
        collect.setInfo(movie.getMovieInfo() + "");
        collect.setPlot(movie.getPlot() + "");
        collect.setImageUrl(movie.getImageUrl() + "");
        collect.setScore(movie.getScore() + "");
        collect.setMovie_720(movie.getMovie_720() + "");
        collect.setMovie_1080(movie.getMovie_1080() + "");
        collect.setType("电影");
        collect.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                stopProgressDialog();
            }
        })      ;
    }
    /**
     * 事件处理
     */
    private void initevent() {
        mTvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FullScreenActivity.class);
                intent.putExtra("url",""+movie.getMovie_1080());
                startActivity(intent);
            }
        });

        /**
         //         * 下载
         //         */
        mTvDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFileInfo downloadFile = FileDownloader.getDownloadFile(movie.getMovie_1080());
                if(downloadFile==null){
                    DownLoadFragment.getInstance().CreatDownloadFile(movie.getMovie_1080(),movie.getMoviename());
                    Toast.makeText(getApplicationContext(), "已加入下载列表中", Toast.LENGTH_SHORT).show();
                    mLlDownload.setVisibility(View.GONE);
                }else {
                    Toast("已添加");
                    mLlDownload.setVisibility(View.GONE);
                }
            }
        });
        mTvXz.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mLlDownload.setVisibility(View.VISIBLE);
           }
       });
        mImgCloseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlDownload.setVisibility(View.GONE);
            }
        });
        mTvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Collect> query = new BmobQuery<>();
                query.addWhereEqualTo("Userid", "" + utis.GetId(getApplicationContext()));
                query.addWhereEqualTo("MovieId", "" + movie.getObjectId());
                query.findObjects(new FindListener<Collect>() {
                    @Override
                    public void done(List<Collect> list, BmobException e) {
                        if(e==null) {
                            if (list.size() > 0) {
                            delete(list.get(0).getObjectId());
                        } else {
                            Collects();
                        }
                        }
                    }
                    /**
                     //             * 收藏
                     //             */
            private void Collects() {
                startProgressDialog("加载中...");
                Collect collect = new Collect();
                collect.setUserid(utis.GetId(getApplicationContext()));
                collect.setMovieId(movie.getObjectId() + "");
                collect.setName(movie.getMoviename());
                collect.setInfo(movie.getMovieInfo() + "");
                collect.setPlot(movie.getPlot() + "");
                collect.setImageUrl(movie.getImageUrl() + "");
                collect.setScore(movie.getScore() + "");
                collect.setMovie_720(movie.getMovie_720() + "");
                collect.setType("电影");
                collect.setMovie_1080(movie.getMovie_1080() + "");
                collect.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                            if(e==null) {
                                stopProgressDialog();
                                Toast("收藏成功");
                            }
                    }
                });
            }
//            /**
//             * 取消收藏
//             */
            private void delete(String id) {
                startProgressDialog("加载中...");
                Collect collect = new Collect();
                collect.setObjectId(id);
                collect.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        stopProgressDialog();
                        Toast("取消收藏成功");
                        if (getIntent().getStringExtra("type").equals("collect")) {
                            setResult(1);
                            finish();
                        }
                    }
                });
            }
                });
            }
        });
    }
}


