package activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anke.vedio.R;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import model.MyUser;


/**
 * Created by Administrator on 2016/10/21.
 */
public class RegistActivity extends BaseActivity {

    private EditText mEtUser;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private EditText mEtPassword1;
    private EditText mEtYqm;
    private EditText mEtYzm;
    private RelativeLayout mRtlGetYzm;
    private RelativeLayout mRtlRegist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        BmobSMS.initialize(getApplicationContext(),"6e33c5d8aa716849ba1e3fe944fa628d");
        initview();
        initevnet();
    }

    private void initevnet() {
        /**
         * 获取验证码
         */
        mRtlGetYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mEtPhone.getText().toString().length()!=11 || mEtPhone.getText().toString().equals("")){
                    Toast("您输入的手机号码格式有误");
               }else {
                   BmobSMS.requestSMSCode(getApplicationContext(),mEtPhone.getText().toString(), "安可影音",new RequestSMSCodeListener() {
                       @Override
                       public void done(Integer smsId,BmobException ex) {
                           if(ex==null){//验证码发送成功
                               Toast("验证码发送成功");
                               Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                           }
                       }
                   });
               }
            }
        });
        /**
         * 确认注册
         */
        mRtlRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.verifySmsCode(getApplicationContext(),mEtPhone.getText().toString().trim(),mEtYzm.getText().toString().trim(), new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//短信验证码已验证成功
                            Log.i("bmob", "验证通过");
                            if(mEtUser.getText().toString().trim().equals("")||mEtPhone.getText().toString().trim().equals("")||mEtPassword.getText().toString().trim().equals("")||mEtPassword1.getText().toString().trim().equals("")||
                                    mEtYzm.getText().toString().trim().equals("")){
                                 Toast("请填写完整");
                            }else if(!mEtPassword.getText().toString().trim().equals(mEtPassword1.getText().toString().trim())) {
                                Toast("两次输入的密码不一致");
                            }else {
                                regist();
                            }
                        }else{
                            Toast("验证码填写错误");
                            Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                        }
                    }
                });
            }
        });
    }
    private void regist() {
            startProgressDialog("注册中...");
            final MyUser myUser = new MyUser();
            myUser.setUsername(mEtPhone.getText().toString().trim());
            myUser.setNIckName(mEtUser.getText().toString().trim());
            myUser.setPassword(mEtPassword.getText().toString().trim());
            myUser.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser myUser, cn.bmob.v3.exception.BmobException e) {
                    if(e==null){
                        stopProgressDialog();
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                        finish();
                    }
                }
            })                                     ;
    }
    private void initview() {
        mEtUser = (EditText) findViewById(R.id.et_user);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPassword1 = (EditText) findViewById(R.id.et_password1);
        mEtYqm = (EditText) findViewById(R.id.et_yqm);
        mEtYzm = (EditText) findViewById(R.id.et_yzm);
        mRtlGetYzm = (RelativeLayout) findViewById(R.id.rtl_get_yzm);
        mRtlRegist = (RelativeLayout) findViewById(R.id.rtl_regist);
    }
}
