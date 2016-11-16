package activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anke.vedio.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utis.SharePre;
import utis.Util;

/**
 * Created by Administrator on 2016/10/20.
 */

public class WelcomeAcitvity extends Activity {

    private RelativeLayout mRtlSave;
    private RelativeLayout mRtlBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initview();
        initlogin();
    }

    private void initview() {
        mRtlSave = (RelativeLayout) findViewById(R.id.rtl_save);
        mRtlBuy = (RelativeLayout) findViewById(R.id.rtl_buy);
        mRtlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImgs(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logo));
            }
        });
        mRtlBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", "复制的内容");
                myClipboard.setPrimaryClip(myClip);
                Util.InstallSoft(getApplicationContext(),"com.taobao.taobao");
            }
        });
    }

    private void initlogin() {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(SharePre.islogined(getApplicationContext())){
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            //这里的数字为延时时长
        }, 1000);
    }

    /**
     * 保存图片
     *
     * @param bigImg
     */
    private void saveImgs(Bitmap bigImg) {
        if (getSDPath() == null) {
            Toast.makeText(getApplicationContext(), "没有内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getAppFolder());
        if (!file.exists()) {
            file.mkdir();
        }
        File imageFile = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            imageFile.getParentFile().mkdirs();
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bigImg.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
//			Toast.makeText(getActivity(), "图片已保存到本地文件夹" + savePath, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
            MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), imageFile.getPath(), "title", "description");
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(file);
//            intent.setData(uri);
//            getActivity().sendBroadcast(intent);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageFile.getPath())));

        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "图片保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "图片保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public static String getAppFolder() {
        String path = Environment.getExternalStorageDirectory() + "/" +"AnkeMovie" + "/";
        File f = new File(path);
        if(!f.exists()) f.mkdir();
        return path;
    }
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
