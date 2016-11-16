package utis;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/21.
 */

public class Util {
    /** 将dp长度转换成px长度，用于代码中设置距离 */
    public static int dip2px(Context context,float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static void InstallSoft(Context context,String packages){
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packages);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
        context.startActivity(intent);
    }
    /**
     * 将字符串转化为集合图片
     * @param img
     * @return
     */
    public  static ArrayList<String> StingToImg(String img){
        ArrayList<String> mImg=new ArrayList<>();
        String[] strarray=img.split(",");
        for (int i = 0; i < strarray.length; i++) {
            mImg.add(strarray[i]);
        }
        return mImg;
    }
}
