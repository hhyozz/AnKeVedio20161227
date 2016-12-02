package activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anke.vedio.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import rongyun.MD5;
import rongyun.TokenMod;
import utis.GsonUtils;
import app.BaseApp;
import rongyun.DemoContext;
import utis.NetUtils;
import utis.SharePre;
import utis.utis;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import model.MyUser;
import okhttp.OkHttpUtils;
import okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/21.
 */

public class LoginActivity extends BaseActivity {
    private EditText mEtPhone;
    private EditText mEtPassowrd;
    private RelativeLayout  mRtlLogin;
    private RelativeLayout mRtlRegist;
    private TextView mTvFindPassword;
    private static final String RY_APP_KEY = "qd46yzrf4yyaf";
    private static final String RY_APP_SECRET = "OCBC9HEfvS8wK";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login);
        initview();
        initevent();
    }
    private void initevent() {
        //登录
        mRtlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtPassowrd.getText().toString().trim().equals("")||mEtPhone.getText().toString().trim().equals("")){
                        Toast("请输入完整");
                }else {
                    Login();
                }
            }
        });
        //注册
        mRtlRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegistActivity.class);
                startActivity(intent);
            }
        });
        //找回密码
        mTvFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),FindPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 登录
     */
    private void Login() {
        startProgressDialog("登录中...");
        final BmobUser user = new BmobUser();
        user.setUsername(mEtPhone.getText().toString().trim());
        user.setPassword(mEtPassowrd.getText().toString().trim());
        addSubscription(user.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    stopProgressDialog();
                    utis.saveUser(getApplicationContext(), mEtPhone.getText().toString().trim());
                    Toast(user.getUsername() + "登陆成功");
                    utis.saveId(getApplicationContext(), myUser.getObjectId());
                    utis.saveImgHeadUrl(getApplicationContext(),myUser.getImgHeadUrl());
                    utis.saveNickName(getApplicationContext(),myUser.getNIckName());
                    SharePre.saveLoginState(LoginActivity.this,true);
//                LoginRongyunChat();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        }));
//        user.login(new SaveListener<MyUser>() {
//            @Override
//            public void done(MyUser myUser, BmobException e) {
//                if(e==null){
//                    stopProgressDialog();
//                    utis.saveUser(getApplicationContext(), mEtPhone.getText().toString().trim());
//                    Toast(user.getUsername() + "登陆成功");
//                    utis.saveId(getApplicationContext(), myUser.getObjectId());
//                    utis.saveImgHeadUrl(getApplicationContext(),myUser.getImgHeadUrl());
//                    utis.saveNickName(getApplicationContext(),myUser.getNIckName());
//                    SharePre.saveLoginState(LoginActivity.this,true);
////                LoginRongyunChat();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                }
//            }
//        });
    }
    /**
     * 获得token
     */
    private String token;
    private void getToken() {
        Random r = new Random();
        String Nonce = (r.nextInt(10000) + 10000) + "";
        String Timestamp = (System.currentTimeMillis() / 1000) + "";
        HashMap<String,String> map=new HashMap<>();
        map.put("userId",utis.GetId(getApplicationContext()));
        map.put("name",utis.GetUser(getApplicationContext()));
        map.put("portraitUri","head");
        OkHttpUtils
                .post()//
                .url("https://api.cn.ronghub.com/user/getToken.json")//
                .params(map)
                .addHeader("App-Key", RY_APP_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature",MD5.encryptToSHA(RY_APP_SECRET + Nonce + Timestamp))
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
                        Log.i("获取TOken返回值========",""+response.toString());
                        TokenMod tokenMod = GsonUtils.parseJSON(response, TokenMod.class);
                        if(tokenMod.getCode().equals("200")){
                          //  connect(tokenMod.getToken());
                            SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                            edit.putString("DEMO_TOKEN", tokenMod.getToken());
                            edit.apply();
                        }
                    }
                });
    }
    /**
     * 建立与融云服务器的连接
     * @param token
     *
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(BaseApp.getCurProcessName(getApplicationContext()))) {
          //  RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 *
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 *
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("连接聊天融云失败------", "--onError" + errorCode);
                }
            });
        }
    }*/
    private void initview() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassowrd = (EditText) findViewById(R.id.et_password);
        mRtlLogin = (RelativeLayout) findViewById(R.id.rtl_login);
        mRtlRegist = (RelativeLayout) findViewById(R.id.rtl_regist);
        mTvFindPassword = (TextView) findViewById(R.id.tv_find_password);
    }
}
