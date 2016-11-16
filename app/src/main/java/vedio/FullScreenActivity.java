package vedio;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;

import com.anke.vedio.R;
import com.tencent.smtt.sdk.WebSettings;
/**
 * Created by Administrator on 2016/12/4.
 */
public class FullScreenActivity extends Activity {
    /**
     * 本activity将演示 webview 与 web 之间的函数相互调用
     * 也就是android方法 与 js 函数的相互调用 和传值
     */
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Message.obtain(handler, MSG).sendToTarget();
            sendUrl();
        }
    });

    X5WebView webView;
    Button btn_setfull;
    Button btn_setsmall;
    Button btn_close;
    Button btn_submit;
    private String msg="";
    //Handler handler;
    //private static final int MSG=0;
    private static final int MSG_SUBMIT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.java_to_js_layout);
        FullScreenActivity.this.msg=(String)getIntent().getSerializableExtra("url");
        /*
        this.handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch(msg.what){
                    case MSG:
                        sendUrl();
                        break;

                    case MSG_SUBMIT:
                        FullScreenActivity.this.msg=(String)getIntent().getSerializableExtra("url");
                        break;
                }
                super.handleMessage(msg);

            }
        };
        */
        this.initUI();
        webView=(X5WebView) findViewById(R.id.web_jsjava);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //设置浏览器不需要点击播放触发手势，自动播放
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        //清除缓存
        //TODO
//        cywApplication.synCookies(FullScreenActivity.this);


        webView.loadUrl("file:///android_asset/webpage/fullscreenVideo.html");
        //webView.loadUrl("http://182.61.42.3:88/ckplayer6.8/demo2.htm"+"?url="+msg);
        //webView.loadUrl("http://182.61.42.3:88/media/test.html");

        webView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub

            }

            /**
             * java 调用 js方法 并且 传值
             * 步骤：1、调用 js函数  2、js回调一个android方法得到参数  3、js处理函数
             * @return
             */
            @JavascriptInterface
            public String getAndroidMsg() {
                Log.i("jsToAndroid", "onSubmitNum happend!");
                String videoplay = "<video style=\"width:100%; height: auto;\" controls autoplay><source id=\"url\" src=" + FullScreenActivity.this.msg + " ></video>";
                //ToastUtils.showLongToast(videoplay);
                return videoplay;
            }
        }, "Android");
        thread.start();
    }

    @Override
    protected void onDestroy()
    {
        if(webView!=null)
            webView.destroy();
        super.onDestroy();
    }

    private void sendUrl() {
        FullScreenActivity.this.webView.loadUrl("javascript:returnMsg()");
    }

    private void initUI(){
        this.btn_setfull = (Button) findViewById(R.id.bt_set_full);
        this.btn_setsmall = (Button) findViewById(R.id.bt_set_small);
        this.btn_close = (Button) findViewById(R.id.bt_close);
        this.btn_submit=(Button) findViewById(R.id.bt_jsjava_edit);
        this.btn_setfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableX5FullscreenFunc();
                //Message.obtain(handler, MSG_SUBMIT).sendToTarget();
                //TODO
//                cywApplication.synCookies(FullScreenActivity.this);
                FullScreenActivity.this.webView.loadUrl("javascript:returnMsg()");
            }
        });


        this.btn_setsmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enablePageVideoFunc();
                //Message.obtain(handler, MSG_SUBMIT).sendToTarget();
//                cywApplication.synCookies(FullScreenActivity.this);
                //TODO
                FullScreenActivity.this.webView.loadUrl("javascript:returnMsg()");
            }
        });

        this.btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除缓存
                //TODO
//                cywApplication.synCookies(FullScreenActivity.this);
                webView.clearHistory();
                webView.clearFormData();
                finish();
            }
        });

        this.btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Message.obtain(handler, MSG_SUBMIT).sendToTarget();
                FullScreenActivity.this.webView.loadUrl("javascript:returnMsg()");
            }
        });

    }


    private void enableX5FullscreenFunc(){
        Log.i("jsToAndroid", "enableX5FullscreenFunc happend!");

        if(webView.getX5WebViewExtension()!=null){
            //Toast.makeText(this, "开启全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);//true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);//false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);//1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }


    private void enablePageVideoFunc(){
        Log.i("jsToAndroid","enablePageVideoFunc happend!");
        if(webView.getX5WebViewExtension()!=null){
            //Toast.makeText(this, "开启小屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);//true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);//false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);//1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }
    }
    //////////////////////////////////////////////////
    //java to javascript methods

















}
