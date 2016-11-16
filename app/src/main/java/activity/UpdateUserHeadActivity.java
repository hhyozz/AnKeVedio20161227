package activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anke.vedio.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.exception.BmobException;
import utis.ImageUtils;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/10/24.
 */

public class UpdateUserHeadActivity extends BaseActivity {
    private TextView mTvTakePhotos;
    private TextView mTvPhotos;
    ArrayList<String> mPhotos=new ArrayList<String>();
    ArrayList<String> mImgUrl=new ArrayList<String>();
    protected static final int TAKE_PICTURE = 100;
    public final String takePhotoPostfix = ".jpg"; // 拍照文件后缀
    protected static final int CHOOSE_PICTURE = 101;
    private RelativeLayout mRtlCancle;
    private File tempFile;
    private EditText mEtNickName;
    private String filename;
    private Uri imageUri;
    private ImageView mImgHead;
    private RelativeLayout mRtlComplite;
    private RelativeLayout mRtlHead;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_userhead);
        initview();
        initevent();
    }
    private void initevent() {
        mRtlCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 相册
         */
        mTvPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PICTURE);
            }
        });
        /**
         * 拍照
         */
        mTvTakePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TakePhotos();
            }
        });
        /**
         * 提交
         */
        mRtlComplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtNickName.getText().toString().trim().equals("")){
                     Toast("请输入昵称");
                }else if(mPhotos.size()==0){
                    Toast("请上传头像");
                }else {
                    Log.i("上传头像的地址===",""+mPhotos.get(0));
                    startProgressDialog("加载中...");
                    final BmobFile bmobFile=new BmobFile(new File(mPhotos.get(0)));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                             if(e==null){
                                 stopProgressDialog();
                            Log.i("上传后的地址=====",""+bmobFile.getFileUrl());
                             } else {
                                 Toast("上传失败"+e.toString());

                             }
                        }
                    })      ;
//                    startProgressDialog("上传中");
//                    final BmobFile bmobFile = new BmobFile(new File(mPhotos.get(0)));
//                    bmobFile.uploadblock(getApplicationContext(), new UploadFileListener() {
//                        @Override
//                        public void onSuccess() {
//                            stopProgressDialog();
//                            final String ImgHeadUrl = bmobFile.getFileUrl(UpdateUserHeadActivity.this);
//                            Log.i("上传的头像--------","ture");
////                            MyUser myUser = new MyUser();
////                            myUser.setNIckName(mEtNickName.getText().toString().trim());
////                            myUser.setImageHead(ImgHeadUrl);
////                            myUser.update(getApplicationContext(),utis.GetId(getApplicationContext()), new UpdateListener() {
////                                @Override
////                                public void onSuccess() {
////                                    stopProgressDialog();
////                                    utis.saveImgHeadUrl(getApplicationContext(),ImgHeadUrl);
////                                   Toast("更新成功");
////                                    setResult(1);
////                                    finish();
////                                }
////                                @Override
////                                public void onFailure(int i, String s) {
////                                    Toast(""+s.toString());
////                                    stopProgressDialog();
////                                }
////                            });
//                        }
//                            @Override
//                        public void onFailure(int i, String s) {
//                                Toast(s.toString()+"上传头像失败");
//                                stopProgressDialog();
//                        }
//                    });
                }
            }
        });
    }

    private void initview() {
        mRtlHead = (RelativeLayout) findViewById(R.id.rtl_head);
        mImgHead = (ImageView) findViewById(R.id.img_head);
        mTvTakePhotos = (TextView) findViewById(R.id.tv_take_photo);
        mTvPhotos = (TextView) findViewById(R.id.tv_photoes);
        mRtlCancle = (RelativeLayout) findViewById(R.id.rtl_cancles);
        mEtNickName = (EditText) findViewById(R.id.et_nickname);
        mRtlComplite = (RelativeLayout) findViewById(R.id.rtl_complite);
    }
    public  void TakePhotos(){
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, filename);
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            tempFile = createTakePhote();
            imageUri = Uri.fromFile(tempFile);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            Toast.makeText(UpdateUserHeadActivity.this, "内存卡不存在", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 生成新的图片文件用于保存拍照照片
     * @return
     */
    private static SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
//    public final  String FILE_SAVE_PATH = getBaseFolder() + "/AnKeMovie/";
//    public File createTakePhote() {
//        File file = new File(FILE_SAVE_PATH + formatTimeForFile(new Date()) + takePhotoPostfix);
//        if( !file.getParentFile().exists() ) {
//            file.getParentFile().mkdirs();
//        }
//        return new File(FILE_SAVE_PATH + formatTimeForFile(new Date()) + takePhotoPostfix);
//    }
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
                mRtlHead.setVisibility(View.VISIBLE);
                mPhotos.clear();
                mPhotos.add(tempFile.getAbsolutePath());
                Toast.makeText(UpdateUserHeadActivity.this, "图片个数"+mPhotos.size(), Toast.LENGTH_SHORT).show();
                ImageUtils.displayLocalImage(mPhotos.get(0),mImgHead);
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
                    mRtlHead.setVisibility(View.VISIBLE);
                    mPhotos.clear();
                    mPhotos.add(fPath);
//                    fileName.add(imgName);
                    Toast.makeText(UpdateUserHeadActivity.this, "图片个数"+mPhotos.size(), Toast.LENGTH_SHORT).show();
                    ImageUtils.displayLocalImage(mPhotos.get(0),mImgHead);
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

    private static File baseFolder;
//    public String getBaseFolder() {
////		return Environment.getExternalStorageDirectory() + "/" + getInstance().getString(R.string.app_folder);
//        if( baseFolder != null ) {
//            return baseFolder.getAbsolutePath();
//        }
//        File file = getExternalFilesDir(null);
//        if( file == null ) {
//            file = getFilesDir();
//        }
//        if(!file.exists()) {
//            file.mkdirs();
//        }
//        baseFolder = file;
//        return file.getAbsolutePath();
//    }
}
