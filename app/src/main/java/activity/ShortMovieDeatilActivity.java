package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anke.vedio.R;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import utis.UILUtils;
import utis.utis;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import model.Collect;
import model.ShortMovie;
import vedio.FullScreenActivity;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.*;

import vedio.VideoJJActivity;
import views.FastBlurs;

/**
 * Created by Administrator on 2016/11/6.
 */

public class ShortMovieDeatilActivity extends BaseActivity {

    private ImageView mImgIcon;
    private TextView mTvName;
    private TextView mTvInfo;
    private ShortMovie shortMovie;
    private TextView mTvCollect;
    private TextView mTvPlay;
    private ImageView mpoly_bg;//背面毛玻璃
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_movie_detail);
        getDate();
        initview();
        initevent();
    }
    private void initevent() {
        mTvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shortMovie.getMovie_720()==null||shortMovie.getMovie_720().equals(""))
                {
                    Toast.makeText(ShortMovieDeatilActivity.this, "无效的播放地址,请确认片源是否上传", Toast.LENGTH_LONG).show();
                    return ;
                }
                Intent intent=new Intent(getApplicationContext(), VideoJJActivity.class);
                intent.putExtra("url","" +shortMovie.getMovie_720());
                startActivity(intent);

            }
        });
        /**
         * 收藏
         */
        mTvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Collect> query = new BmobQuery<>();
                query.addWhereEqualTo("Userid", "" + utis.GetId(getApplicationContext()));
                query.addWhereEqualTo("MovieId", "" + shortMovie.getObjectId());
                query.findObjects(new FindListener<Collect>() {
                    @Override
                    public void done(List<Collect> list, BmobException e) {
                         if(e==null){
                             if (list.size() > 0) {
                            delete(list.get(0).getObjectId());
                        } else {
                            Collects();
                        }
                    } }
                    /**
                     //             * 收藏
                     //             */
                    private void Collects() {
                        startProgressDialog("加载中...");
                        Collect collect = new Collect();
                        collect.setUserid(utis.GetId(getApplicationContext()));
                        collect.setMovieId(shortMovie.getObjectId() + "");
                        collect.setName(shortMovie.getMoviename());
                        collect.setPlot(shortMovie.getPlot() + "");
                        collect.setImageUrl(shortMovie.getImageUrl() + "");
                        collect.setMovie_720(shortMovie.getMovie_720() + "");
                        collect.setType("短片");
                        collect.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                 if(e==null){
                                     stopProgressDialog();
                                Toast("收藏成功");
                                 }
                            }
                        })      ;
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
                                 if(e==null){
                                     stopProgressDialog();
                                Toast("取消收藏成功");
                                if (getIntent().getStringExtra("type").equals("collect")) {
                                    setResult(1);
                                    finish();
                                }
                                 }
                            }
                        })      ;
                    }
                });
            }
        });
    }

    private void getDate() {
       shortMovie= (ShortMovie) getIntent().getSerializableExtra("data");
    }
    private void initview() {
        mTvPlay = (TextView) findViewById(R.id.tv_play);
        mImgIcon = (ImageView) findViewById(R.id.img_icon);
        mTvCollect = (TextView) findViewById(R.id.tv_collect);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        UILUtils.displayImageNoAnim(shortMovie.getImageUrl(),mImgIcon);
        mTvName.setText(shortMovie.getMoviename());
        mTvInfo.setText("剧情介绍s:"+shortMovie.getPlot());
        mpoly_bg=(ImageView)findViewById(R.id.poly_bg);
        /**
         *毛玻璃背景,基于Glide的毛玻璃
         */
        Glide.with(this).load(shortMovie.getImageUrl()).asBitmap()
            .transform(new FastBlurs(this, 200))//控制模糊的程度
            //加载错误时显示的 .error(R.drawable.erro_icon)
            .into(mpoly_bg);
        
    }
}
