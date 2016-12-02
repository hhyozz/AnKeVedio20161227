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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.widget.*;

import android.view.*;
import com.anke.vedio.*;
import views.*;
import utis.SharePre;
import java.util.Timer;
import android.os.*;
import java.util.*;
import android.view.View.*;


/**
 * Created by Administrator on 2016/10/20.
 */

public class WelcomeAcitvity extends Activity {
    private int recLen = 6;    
    private RelativeLayout mRtlSave;
    private RelativeLayout mRtlBuy;
    private LinearLayout menter;
    private TextView mji;
    private ImageView logo;//海报图片
    private LinearLayout poly;
    Timer timer = new Timer();    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initview();
        initlogin();
        applyBlur();
    }
    final Handler handler = new Handler(){    
        @Override    
        public void handleMessage(Message msg){    
            switch (msg.what) {    
                case 1:    
                    mji.setText(""+recLen+"S");    
                    if(recLen < 0){    
                        timer.cancel();    

                    }    
            }    
        }    
    };    

    TimerTask task = new TimerTask() {    
        @Override    
        public void run() {    
            recLen--;    
            Message message = new Message();    
            message.what = 1;    
            handler.sendMessage(message);    
        }    
    };    
    
    private void initview() {
        mRtlSave = (RelativeLayout) findViewById(R.id.rtl_save);
        mRtlBuy = (RelativeLayout) findViewById(R.id.rtl_buy);
        logo=(ImageView)findViewById(R.id.big_logo);
        poly=(LinearLayout)findViewById(R.id.poly);
        mji=(TextView)findViewById(R.id.ji);
        menter=(LinearLayout)findViewById(R.id.enter);
        timer.schedule(task, 1000, 1000);       // timeTask    
        menter.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
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
            }, 5000);
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
            bigImg.compress(Bitmap.CompressFormat.PNG, 10, fos);
            fos.flush();
            fos.close();
//          Toast.makeText(getActivity(), "图片已保存到本地文件夹" + savePath, Toast.LENGTH_LONG).show();
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
    //毛玻璃
    private void applyBlur() {
        logo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    logo.getViewTreeObserver().removeOnPreDrawListener(this);
                    logo.buildDrawingCache();

                    Bitmap bmp = logo.getDrawingCache();
                    blur(bmp, poly);
                    return true;
                }
            });
    }

    private void blur(Bitmap bkg, View view) {

        //毛玻璃参数设置
        float scaleFactor = 1;
        float radius = 60;
        scaleFactor = 8;
        radius = 2;


        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                                             (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

    
    
}}


