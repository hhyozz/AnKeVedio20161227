package utis;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/3/2.
 */
public class SharePre {
    public  static void saveLoginState(Context context,boolean logined) {
        SharedPreferences sp = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        sp.edit().putBoolean("Login", logined).commit();
    }
    public static boolean islogined(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        boolean logined = sp.getBoolean("Login", false);
        return logined;
    }
}

