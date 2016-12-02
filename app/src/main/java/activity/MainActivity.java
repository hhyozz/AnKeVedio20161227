package activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anke.vedio.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import app.BaseApp;
import dialog.MainPopMenu;
import fragments.CollectFragment;
import fragments.DownLoadFragment;
import fragments.HistoryFragment;
import fragments.HomeFragment;
import fragments.HomeFragments.SearchFragment;
import fragments.LookFragment;
import fragments.ReQuestFragment;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp.OkHttpUtils;
import okhttp.callback.StringCallback;
import okhttp3.Call;
import rongyun.DemoContext;
import rongyun.MD5;
import rongyun.StateCode;
import rongyun.TokenMod;
import utis.SharePre;
import utis.UILUtils;
import constance.constance;
import utis.GsonUtils;
import utis.utis;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    long waitTime = 2000;
    long touchTime = 0;
    /*
    private ImageView mImgMenu;
    private ImageView mImgSearch;
    private ImageView mImgDownload;
    private ImageView mImgHistory;
    private ImageView mImgHead;
    private TextView mTvSearchCancle;
    private RelativeLayout mRtlSearch;
    private RelativeLayout mRtlMainMenu;
    */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        inidate();
        getToken();
        CreatChatRoom();
    }
    private void getToken() {
        Random r = new Random();
        String Nonce = (r.nextInt(10000) + 10000) + "";
        final String Timestamp = (System.currentTimeMillis() / 1000) + "";
        HashMap<String,String> map=new HashMap<>();
        map.put("userId", utis.GetId(getApplicationContext()));
        map.put("name", utis.GetUser(getApplicationContext()));
        map.put("portraitUri", "head");
        OkHttpUtils
                .post()//
                .url("https://api.cn.ronghub.com/user/getToken.json")//
                .params(map)
                .addHeader("App-Key", constance.KEY.RY_APP_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature",MD5.encryptToSHA( constance.KEY.RY_APP_SECRET + Nonce + Timestamp))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("获取TOken返回值========", "" + response.toString());
                        TokenMod tokenMod = GsonUtils.parseJSON(response, TokenMod.class);
                        if (tokenMod == null) {
                            Toast("数据获取失败");
                            return;
                        }
                        if (tokenMod.getCode().equals("200")) {
                            connect(tokenMod.getToken());
                            SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                            edit.putString("DEMO_TOKEN", tokenMod.getToken());
                            edit.apply();
                            Log.e("获取成功", "success");
                        }
                    }
                });
    }
    /**
     * 建立与融云服务器的连接
     * @param token
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(BaseApp.getCurProcessName(getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("连接聊天服务器成功", "--onSuccess" + userid);
                }
                /**
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("连接聊天融云失败------", "--onError" + errorCode);
                }
            });
        }
    }
    /**
     * 创建聊天室
     */
    private void CreatChatRoom() {
        Random r = new Random();
        String Nonce = (r.nextInt(10000) + 10000) + "";
        String Timestamp = (System.currentTimeMillis() / 1000) + "";
        HashMap<String,String> map=new HashMap<>();
        map.put("chatroom[1]","文艺");
        map.put("chatroom[2]","励志");
        map.put("chatroom[3]","喜剧");
        map.put("chatroom[4]","战争");
        map.put("chatroom[5]","恐怖");
        map.put("chatroom[6]","科幻");
        map.put("chatroom[7]","动作");
        map.put("chatroom[8]","魔幻");
        map.put("chatroom[9]","动画");
        map.put("chatroom[10]","童年");
        map.put("chatroom[11]","爱情");
        map.put("chatroom[12]","夜话");
        OkHttpUtils
                .post()//
                .url("http://api.cn.ronghub.com/chatroom/create.json")//
                .params(map)
                .addHeader("App-Key", constance.KEY.RY_APP_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature", MD5.encryptToSHA(constance.KEY.RY_APP_SECRET + Nonce + Timestamp))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (e instanceof SocketTimeoutException) {
                            Toast("连接超时，加载失败");
                        } else if (e instanceof IOException) {
                            Toast("网络异常，请稍后重试.");
                        } else {
                            Toast("服务器异常，请稍后重试.");
                        }
                    }
                    @Override
                    public void onResponse(String response) {
                        Log.i("创建聊天聊天室返回值========",""+response.toString());
                        StateCode stateCode = GsonUtils.parseJSON(response, StateCode.class);
                        if(stateCode.getCode().equals("200")){
//                            Toast("创建成功");
                            Log.i("创建聊天室成功","success");
                        }else {
                            Log.i("创建聊天室失败","fail");
                        }
                    }
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    //设置头像
    private void inidate() {
        /*
       if(utis.GetImgHeadUrl(getApplicationContext())==null){
          mImgHead.setBackground(getResources().getDrawable(R.mipmap.ic_launcher));
       }else {
           UILUtils.displayImageNoAnim(utis.GetImgHeadUrl(getApplicationContext()),mImgHead);
       }
       */
    }

    private void initview() {
        /*
        mTvSearchCancle = (TextView) findViewById(R.id.tv_search_cancle);
        mRtlSearch = (RelativeLayout) findViewById(R.id.rtl_search);
        mRtlMainMenu = (RelativeLayout) findViewById(R.id.rtl_main_menus);
        mImgHead = (ImageView) findViewById(R.id.img_head);
        mImgSearch = (ImageView) findViewById(R.id.img_search);
        mImgMenu = (ImageView) findViewById(R.id.img_menu);
        mImgHistory = (ImageView) findViewById(R.id.img_history);
        mImgDownload = (ImageView) findViewById(R.id.img_download);
        mImgMenu.setOnClickListener(this);
        mImgSearch.setOnClickListener(this);
        mImgHistory.setOnClickListener(this);
        mImgDownload.setOnClickListener(this);
        mTvSearchCancle.setOnClickListener(this);
        */
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_main,new HomeFragment());
        ft.commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if((currentTime-touchTime)>=waitTime) {
                Toast.makeText(this, "再按一次退出",Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()){
            /*
            case R.id.img_menu://侧滑菜单
                Menues(v);
                break;
            case R.id.img_search://搜索
                mRtlMainMenu.setVisibility(View.GONE);
                mRtlSearch.setVisibility(View.VISIBLE);
                ft.replace(R.id.fl_main,new SearchFragment());
                break;
            case R.id.img_history://搜索
                ft.replace(R.id.fl_main,new HistoryFragment());
                break;
            case R.id.img_download://下载
                ft.replace(R.id.fl_main,new DownLoadFragment());
                break;
            case R.id.tv_search_cancle://取消搜索
                mRtlSearch.setVisibility(View.GONE);
                mRtlMainMenu.setVisibility(View.VISIBLE);
                ft.replace(R.id.fl_main,new HomeFragment());
                break;
            */
            default:break;
        }
        ft.commit();
    }
    /**
     * 菜单
     */
    private void Menues(View v) {
        MainPopMenu mainPopMenu = new MainPopMenu(getApplicationContext());
        mainPopMenu.showAtLocation(v,Gravity.LEFT | Gravity.TOP, 0, 0);
        mainPopMenu.setOnSelectOnclickListner(new MainPopMenu.OnSelecListner() {
            @Override
            public void onSelect(String Type) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (Type) {
                    case "look":
                        ft.replace(R.id.fl_main, new LookFragment());
                        break;
                    case "home":
                        ft.replace(R.id.fl_main, new HomeFragment());
                        break;
                    case "collect":
                        ft.replace(R.id.fl_main, new CollectFragment());
                        break;
                    case "recomand":
                        Intent intent_tj = new Intent(MainActivity.this, RecomandActivity.class);
                        startActivity(intent_tj);
                        break;
                    case "request":
                        ft.replace(R.id.fl_main, new ReQuestFragment());
                        break;
                    case "update":
                        Intent intent = new Intent(MainActivity.this, UpdateUserHeadActivity.class);
                        startActivityForResult(intent, 11);
                        break;
                    case "loginout":
                        SharePre.saveLoginState(MainActivity.this, false);
                        Intent intent_loginout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent_loginout);
                        finish();
                        break;
                }
                ft.commit();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            inidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean dispatchTouchEvent(MotionEvent ev);
    }
}
