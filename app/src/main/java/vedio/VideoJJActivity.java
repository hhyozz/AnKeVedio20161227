package vedio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.anke.vedio.R;

import java.lang.reflect.Field;

import cn.com.video.venvy.param.JjVideoRelativeLayout;
import cn.com.video.venvy.param.JjVideoView;
import cn.com.video.venvy.param.OnJjBufferCompleteListener;
import cn.com.video.venvy.param.OnJjBufferStartListener;
import cn.com.video.venvy.param.OnJjBufferingUpdateListener;
import cn.com.video.venvy.param.OnJjOpenStartListener;
import cn.com.video.venvy.param.OnJjOpenSuccessListener;
import cn.com.video.venvy.param.OnJjOutsideLinkClickListener;
import cn.com.video.venvy.param.VideoJjMediaContoller;

/**
 * Created by Administrator on 2016/12/18.
 */
public class VideoJJActivity extends Activity {
    private JjVideoView mVideoView;//  video
    private View mLoadBufferView;//buff
    private TextView mLoadBufferTextView;// 缓冲区的值
    private View mLoadView;// /
    private TextView mLoadText;// /
    private String payUrl="";
    private VideoJjMediaContoller videoJjMediaContoller;
    //private Button bt_close;
    //private Button bt_refresh;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_videojj);

        payUrl = (String)(getIntent().getSerializableExtra("url"));
        payUrl = payUrl.replaceAll("https","http");
        payUrl = payUrl.replaceAll("\n","");
        payUrl = payUrl.replaceAll("\r","");
        payUrl = payUrl.replaceAll("\t","");

        videoJjMediaContoller = new VideoJjMediaContoller(this, true);

        mVideoView = (JjVideoView) findViewById(R.id.video);
        mLoadView = findViewById(R.id.sdk_ijk_progress_bar_layout);
        mLoadText = (TextView) findViewById(R.id.sdk_ijk_progress_bar_text);
        mLoadBufferView = findViewById(R.id.sdk_load_layout);
        mLoadBufferTextView = (TextView) findViewById(R.id.sdk_sdk_ijk_load_buffer_text);
        mVideoView.setMediaController(videoJjMediaContoller);
        mLoadBufferTextView.setTextColor(Color.RED);
        /*
        bt_close = (Button)findViewById(R.id.bt_close);
        bt_refresh = (Button)findViewById(R.id.bt_refresh);


        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView != null)
                    mVideoView.onDestroy();
                finish();
            }
        });

        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setVideoJjResetState();  //重新加载时候需要
                mVideoView.setVideoJjType(3);
                mVideoView.setResourceVideo(payUrl);
            }
        });
        */
        /***
         * 用户自定义的外链 可 获取外链点击时间
         */
        mVideoView.setOnJjOutsideLinkClick(new OnJjOutsideLinkClickListener() {

            @Override
            public void onJjOutsideLinkClose() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onJjOutsideLinkClick(String arg0) {
                // TODO Auto-generated method stub

            }
        });

        /***
         * 设置缓冲
         */
        mVideoView.setMediaBufferingView(mLoadBufferView);
        /***
         * 视频开始加载数据
         */
        mVideoView.setOnJjOpenStart(new OnJjOpenStartListener() {
            @Override
            public void onJjOpenStart(String arg0) {
                mLoadText.setText(arg0);
            }
        });
        /***
         * 视频开始播放
         */
        mVideoView.setOnJjOpenSuccess(new OnJjOpenSuccessListener() {
            @Override
            public void onJjOpenSuccess() {
                mLoadView.setVisibility(View.GONE);

                //设置全屏
                try {
                    vedioFullScreen();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        // 缓冲开始
        mVideoView.setOnJjBufferStart(new OnJjBufferStartListener() {
            @Override
            public void onJjBufferStartListener(int arg0) {
                Log.e("Video++", "====================缓冲值=====" + arg0);
            }
        });
        //缓存中
        mVideoView .setOnJjBufferingUpdateListener(new OnJjBufferingUpdateListener() {
            @Override
            public void onJjBufferingUpdate(int arg1) {
                // TODO Auto-generated method stub
                if (mLoadBufferView.getVisibility() == View.VISIBLE) {
                    mLoadBufferTextView.setText(String
                            .valueOf(mVideoView.getBufferPercentage())
                            + "%");
                    Log.e("Video++", "====================缓冲值2====="
                            + arg1);
                }
            }
        });
        // 缓冲完成
        mVideoView.setOnJjBufferComplete(new OnJjBufferCompleteListener() {

            @Override
            public void onJjBufferCompleteListener(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        /***
         * 注意VideoView 要调用下面方法 配置你用户信息
         */
        mVideoView.setVideoJjAppKey("BJku1MJMe");//请替换为您申请的AppKey
        mVideoView.setVideoJjPageName("com.anke.vedio");//请替换成您的包名，此处为本demo的包名
        mVideoView.setMediaCodecEnabled(true);// 是否开启 硬解 硬解对一些手机有限制
        // 判断是否源 0 代表 8大视频网站url 3代表自己服务器的视频源 2代表直播地址 1代表本地视频(手机上的视频源),4特殊需求
        /***
         * 视频标签显示的时间 默认显示5000毫秒 可设置 传入值 long类型 毫秒
         */
        // 参数代表是否记录视频播放位置 默认false不记录 true代表第二次或多次进入，直接跳转到上次退出的时间点开始播放
        // mVideoView.setVideoJjSaveExitTime(false);
        /***
         * 指定时间开始播放 毫秒
         */

       // mVideoView.setVideoJjResetState();  //重新加载时候需要
        mVideoView.setVideoJjType(3);
        mVideoView.setResourceVideo(payUrl);
        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.root);
        JjVideoRelativeLayout mJjVideoRelativeLayout = (JjVideoRelativeLayout) findViewById(R.id.jjlayout);
        mJjVideoRelativeLayout.setJjToFront(mLayout);// 设置此方法
        // 重新排序视图层级JjVideoRelativeLayout，避免横屏其它遮挡
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 必须调用 要不直播有问题
        if (mVideoView != null)
            mVideoView.onDestroy();
    }

    private void vedioFullScreen() throws NoSuchFieldException, IllegalAccessException {
        //通过反射获取 全屏按钮的点击事件
        Field fullScreenImageButton = VideoJjMediaContoller.class.getDeclaredField("mDirectionView");
        // 设置访问权限（这点对于有过android开发经验的可以说很熟悉）
        fullScreenImageButton.setAccessible(true);
        Object fullScreenObject = fullScreenImageButton.get(videoJjMediaContoller);
        ImageButton fullScreen = (ImageButton)fullScreenObject;
        // 模拟点击事件
        fullScreen.performClick();
    }
}
