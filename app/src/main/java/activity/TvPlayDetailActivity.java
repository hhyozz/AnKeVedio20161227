package activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anke.vedio.R;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import utis.UILUtils;
import utis.Util;
import utis.utis;
import adapter.TvListAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import model.TvPlay;
import model.look;
import com.bumptech.glide.*;

import vedio.VideoJJActivity;
import views.FastBlurs;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TvPlayDetailActivity extends  BaseActivity {
    private ArrayList<String> mList=new ArrayList<>();
    private TextView mTvCollect;
    private TextView mTvXz;
    private LinearLayout mLlDownload;
    private TextView mTvDownLoad;
    private TextView mTvInfo;
    private TvPlay tvplay;
    private ImageView mImgIcon;
    private TextView mTvPlot;
    private ImageView mImgCloseDownload;
    private GridView mGdList;
    private TvListAdapter tvListAdapter;
    private TextView mTvLook;
    private ImageView mpoly_bg;//背面毛玻璃
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvplay_detail);
        initview();
        initgdlist();
        initevent();
    }
    private void initevent() {
        mTvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<look> query = new BmobQuery<look>();
                query.addWhereEqualTo("Userid", "" + utis.GetId(getApplicationContext()));
                query.addWhereEqualTo("tvId", "" + tvplay.getObjectId());
                query.findObjects(new FindListener<look>() {
                    @Override
                    public void done(List<look> list, BmobException e) {
                        if(e==null){
                            if (list.size() > 0) {
                                CancleLook(list.get(0).getObjectId());
                            } else {
                                looks();
                            }
                    }
                }
                    /**
                     * 取消关注
                     * @param objectId
                     */
                    private void CancleLook(String objectId) {
                        startProgressDialog("加载中...");
                        look collect = new look();
                        collect.setObjectId(objectId);
                        collect.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                 if(e==null){
                                     stopProgressDialog();
                                Toast("取消关注成功");
                                 }
                            }
                        })      ;
                        }
                    });
                    }
                    /**
                     * 关注
                     */
                    private void looks() {
                        startProgressDialog("加载中...");
                        look collect = new look();
                        collect.setUserid(utis.GetId(getApplicationContext()));
                        collect.setTvId(tvplay.getObjectId() + "");
                        collect.setMoviename(tvplay.getMoviename());
                        collect.setPlot(tvplay.getPlot() + "");
                        collect.setImageUrl(tvplay.getImageUrl() + "");
                        collect.setScore(tvplay.getScore() + "");
                        collect.setMovie_720(tvplay.getMovie_720() + "");
                        collect.setMovie_1080(tvplay.getMovie_1080() + "");
                        collect.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                 if(e==null){
                                     stopProgressDialog();
                                     Toast("关注成功");
                                 }
                            }
                        })      ;
                    }
        });
    }

    private void initgdlist() {
        if(!tvplay.getMovie_720().equals("")){
            mList.clear();
            mList=Util.StingToImg(tvplay.getMovie_720());
            Log.i("视频地址的Url",""+mList.toString());
        }
        tvListAdapter = new TvListAdapter(getApplicationContext(), mList);
        mGdList.setAdapter(tvListAdapter);
        mGdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mList.get(mList.size()-1-position);
                if(url==null||url.equals(""))
                {
                    Toast.makeText(TvPlayDetailActivity.this, "无效的播放地址,请确认片源是否上传", Toast.LENGTH_LONG).show();
                    return ;
                }
                Intent intent=new Intent(getApplicationContext(), VideoJJActivity.class);
                intent.putExtra("url", "" +url);
                startActivity(intent);
            }
        });
    }
    private void initview() {
        mpoly_bg=(ImageView)findViewById(R.id.poly_bg);
        mGdList = (GridView) findViewById(R.id.gv_list);
        mTvCollect = (TextView) findViewById(R.id.tv_collect);
        mTvXz = (TextView) findViewById(R.id.tv_xz);
        mLlDownload = (LinearLayout) findViewById(R.id.ll_download);
        mTvDownLoad = (TextView) findViewById(R.id.tv_download);
        mImgCloseDownload = (ImageView) findViewById(R.id.img_close_download);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mTvPlot = (TextView) findViewById(R.id.tv_plot);
        mImgIcon = (ImageView) findViewById(R.id.img_icon);
        tvplay = (TvPlay) getIntent().getSerializableExtra("data");
        UILUtils.displayImageNoAnim(tvplay.getImageUrl(), mImgIcon);
        mTvPlot.setText("剧情介绍:" + tvplay.getPlot());
        mTvInfo.setText("" + tvplay.getMovieInfo());
        mTvLook = (TextView) findViewById(R.id.tv_look);
        /**
         *毛玻璃背景,基于Glide的毛玻璃
         */
        Glide.with(this).load(tvplay.getImageUrl()).asBitmap()
            .transform(new FastBlurs(this, 200))//控制模糊的程度
            //加载错误时显示的 .error(R.drawable.erro_icon)
            .into(mpoly_bg);
        
    }
}
