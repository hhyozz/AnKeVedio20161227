package activity;

import android.os.Bundle;

import com.anke.vedio.R;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.app.*;
import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import android.util.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;

/**
 * Created by Administrator on 2016/10/21.
 */
public class FindPasswordActivity extends BaseActivity {
    private EditText mphone;
    private EditText yzn;
    private EditText newpass;
    private EditText newpass1;
    private RelativeLayout getyzm;
    private RelativeLayout update_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_passoword);
        mphone=(EditText)findViewById(R.id.edit_phone);
        yzn=(EditText)findViewById(R.id.edit_yzm);
        newpass=(EditText)findViewById(R.id.edit_newpass);
        newpass1=(EditText)findViewById(R.id.edit_newpass1);
        update_btn=(RelativeLayout)findViewById(R.id.update_btn);     
        getyzm=(RelativeLayout)findViewById(R.id.get_yzm);
        getyzm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mphone.getText().toString().length()!=11 || mphone.getText().toString().equals("")){
                        Toast.makeText(FindPasswordActivity.this,"您输入的手机号码格式有误",Toast.LENGTH_SHORT).show();
                    }else {
                        BmobSMS.requestSMSCode(getApplication(),mphone.getText().toString(), "安可影音",new RequestSMSCodeListener() {
                                @Override
                                public void done(Integer smsId,BmobException ex) {
                                    if(ex==null){//验证码发送成功
                                        Toast.makeText(FindPasswordActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                                        Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                                    }
                                }
                            });
                    }
                }
            });
        update_btn.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    String newpass_str=newpass.getText().toString().trim(),newpass_str1=newpass1.getText().toString().trim(),phone=mphone.getText().toString().trim();
                    // TODO: Implement this method
                    //三个Edit不能有一项为空,而且两次密码必须相同
                    if(yzn.getText().toString().trim().equals("")||newpass.getText().toString().trim().equals("")||newpass1.getText().toString().trim().equals("")||!newpass_str.equals(newpass_str1))
                        {
                            BmobUser.resetPasswordBySMSCode(phone, newpass_str, new UpdateListener(){

                                    @Override
                                    public void done(cn.bmob.v3.exception.BmobException p1)
                                    {
                                        // TODO: Implement this method
                                        if(p1==null){
                                            Toast("密码重置成功");
                                        }else{
                                            Toast("密码重置失败");
                                        }
                                        
                                    }
                                    

                                    }
                                    
                            );
                        }else
                        {
                            Toast("请输入完整");
                        }
                }
            });
        
    }
}
