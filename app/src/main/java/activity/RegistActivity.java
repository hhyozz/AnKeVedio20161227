package activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import com.anke.vedio.R;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import model.MyUser;
import android.widget.ImageView;
import android.view.View.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.database.*;
import utis.*;
import com.bumptech.glide.*;
import views.*;
import cn.bmob.v3.listener.UpdateListener;
import model.MyUser;
import utis.ImageUtils;
import utis.utis;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.*;
import android.app.*;
import cn.com.videopls.venvy.client.mqttv3.internal.*;
import android.text.*;

/**
 * Created by Administrator on 2016/10/21.
 */
public class RegistActivity extends BaseActivity {

    private ImageView mUserIcon;
    private EditText mEtUser;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private EditText mEtPassword1;
    private EditText mEtYqm;
    private EditText mEtYzm;
    private RelativeLayout mRtlGetYzm;//验证码
    private RelativeLayout mRtlRegist;//确认注册
    private static final int man = 0;
    private static final int woman = 1;
    protected static final int TAKE_PICTURE = 100;
    public final String takePhotoPostfix = ".jpg"; // 拍照文件后缀
    protected static final int CHOOSE_PICTURE = 101;
    private File tempFile;
    private String filename;
    private Uri imageUri;
    ArrayList<String> mPhotos=new ArrayList<String>();
    ArrayList<String> mImgUrl=new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Bmob.initialize(RegistActivity.this,"6e33c5d8aa716849ba1e3fe944fa628d6e33c5d8aa716849ba1e3fe944fa628d");
        initview();
        initevnet();
        initusericon();
        
    }
    private void initusericon(){
        mUserIcon.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    builder.setTitle("上传头像");
                    String[] items = { "拍照","相册"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case man: // 拍照
                                   TakePhotos();
                                    break;
                                    case woman: // 相册
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(intent, CHOOSE_PICTURE);

                                    break;


                                }
                            }
                        });
                    builder.create().show();
                }
            });
                
            
        }
    public  void TakePhotos(){
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, filename);
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            tempFile = createTakePhote();
            imageUri = Uri.fromFile(tempFile);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            Toast.makeText(RegistActivity.this, "内存卡不存在", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 生成新的图片文件用于保存拍照照片
     * @return
     */
    private static SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    public final  String FILE_SAVE_PATH = getAppFolder() ;
    public File createTakePhote() {
        File file = new File(FILE_SAVE_PATH + formatTimeForFile(new Date()) + takePhotoPostfix);
        if( !file.getParentFile().exists() ) {
            file.getParentFile().mkdirs();
        }
        return new File(FILE_SAVE_PATH + formatTimeForFile(new Date()) + takePhotoPostfix);
    }
    /**
     * 用于文件存储的格式,yyyyMMdd_HHmmss
     * @param date
     * @return
     */
    public static String formatTimeForFile(Date date) {
        return fileDateFormat.format(date);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== TAKE_PICTURE){
            if (resultCode == -1) {
                
                mPhotos.clear();
                mPhotos.add(tempFile.getAbsolutePath());
//                Toast.makeText(UpdateUserHeadActivity.this, "图片个数"+mPhotos.size(), Toast.LENGTH_SHORT).show();
                ImageUtils.displayLocalImage(mPhotos.get(0),mUserIcon);
            }
            else{
            }
        }else if(requestCode== CHOOSE_PICTURE){
            if(data == null){
            }
            else
            {
                Uri uri = data.getData(); // 得到Uri
                String fPath = uri2filePath(uri); // 转化为路径
                Cursor cursor = getContentResolver().query(uri, null, null, null,
                                                           null);
                cursor.moveToFirst();
                String imgPath = cursor.getString(1); // 图片文件路径
                String imgName = cursor.getString(3); // 图片文件名
                if (resultCode == -1) {
                    
                    mPhotos.clear();
                    mPhotos.add(fPath);
//                    fileName.add(imgName);
                    //使用Glide加载图片
                    ImageUtils.displayLocalImage(mPhotos.get(0),mUserIcon);
                    Glide
                        .with(RegistActivity.this)
                        .load(mPhotos.get(0))
                        .transform(new GlideCircleTransform(RegistActivity.this))        
                        .into(mUserIcon);
                }
            }
        }
    }
    /** 把Uri转化成文件路径 */
    public String uri2filePath(Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri,proj,null,null,null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }

    /**
     * 返回app文件夹，在内存卡的一级目录下，以该应用名称建立的文件夹
     */
    public static String getAppFolder() {
        boolean sdCardExist = Environment.getExternalStorageState()
            .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            String path = Environment.getExternalStorageDirectory() + "/" +"安可影音" + "/";
            File f = new File(path);
            if(!f.exists()) f.mkdir();
            return  path;
        }else {
            return  null;
        }
    }
    private void initevnet() {
        /**
         * 获取验证码
         */
        mRtlGetYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mEtPhone.getText().toString().length()!=11 || mEtPhone.getText().toString().equals("")){
                    Toast.makeText(RegistActivity.this,"您输入的手机号码格式有误",Toast.LENGTH_SHORT).show();
               }else {
                   BmobSMS.requestSMSCode(getApplicationContext(),mEtPhone.getText().toString(), "安可影音",new RequestSMSCodeListener() {
                       @Override
                       public void done(Integer smsId,BmobException ex) {
                           if(ex==null){//验证码发送成功
                               Toast.makeText(RegistActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
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
                //所有edittext不可以有一项为空
                if(TextUtils.isEmpty(mEtUser.getText())||TextUtils.isEmpty(mEtPassword.getText())||TextUtils.isEmpty(mEtPassword1.getText())||TextUtils.isEmpty(mEtYqm.getText())||TextUtils.isEmpty(mEtYzm.getText())||TextUtils.isEmpty(mEtPhone.getText())){
                Log.i("上传头像的地址===",""+mPhotos.get(0));
                
                final BmobFile bmobFile=new BmobFile(new File(mPhotos.get(0)));
                bmobFile.uploadblock(new UploadFileListener() {

                        
                        @Override
                        public void done(cn.bmob.v3.exception.BmobException e) {
                            
                            if(e==null){
                                String ImgHeadUrl = bmobFile.getFileUrl();
                                  final MyUser myUser = new MyUser();
                                myUser.setImageHead(ImgHeadUrl);
                                myUser.setUsername(mEtPhone.getText().toString().trim());
                                myUser.setNIckName(mEtUser.getText().toString().trim());
                                myUser.setPassword(mEtPassword.getText().toString().trim());
                                myUser.signUp(new SaveListener<MyUser>() {
                                        @Override
                                        public void done(MyUser myUser, cn.bmob.v3.exception.BmobException e) {
                                            if(e==null){
                                                
                                                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                                                finish();
                                            }
                                        }
                                    });      
            
        }
                        }});
                        }else
                        {
                            Toast.makeText(getApplicationContext(),"不可以有一项为空",Toast.LENGTH_SHORT).show();
                        }
                        }
                        }
                      );
                     }
    
    private void initview() {
        mUserIcon=(ImageView)findViewById(R.id.user_icon);
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
