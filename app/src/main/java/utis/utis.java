package utis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/3/5.
 */
public class utis {
	
	/**
	 * @param context
	 * @param
	 */
	public  static void saveUser(Context context,String UserId) {
		SharedPreferences sp = context.getSharedPreferences("JGId", Context.MODE_PRIVATE);
		sp.edit().putString("JGId", UserId).commit();
	}
	public static String  GetUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences("JGId", Context.MODE_PRIVATE);
		return sp.getString("JGId",null);
	}
	
	
	
	public  static void saveId(Context context,String UserId) {
		SharedPreferences sp = context.getSharedPreferences("Id", Context.MODE_PRIVATE);
		sp.edit().putString("Id", UserId).commit();
	}
	public static String  GetId(Context context) {
		SharedPreferences sp = context.getSharedPreferences("Id", Context.MODE_PRIVATE);
		return sp.getString("Id",null);
	}

	public  static void saveImgHeadUrl(Context context,String ImgHeadUrl) {
		SharedPreferences sp = context.getSharedPreferences("ImgHeadUrl", Context.MODE_PRIVATE);
		sp.edit().putString("ImgHeadUrl", ImgHeadUrl).commit();
	}
	public static String  GetImgHeadUrl(Context context) {
		SharedPreferences sp = context.getSharedPreferences("ImgHeadUrl", Context.MODE_PRIVATE);
		return sp.getString("ImgHeadUrl",null);
	}
	public  static void saveNickName(Context context,String NickName) {
		SharedPreferences sp = context.getSharedPreferences("NickName", Context.MODE_PRIVATE);
		sp.edit().putString("NickName", NickName).commit();
	}
	public static String  GetNickName(Context context) {
		SharedPreferences sp = context.getSharedPreferences("NickName", Context.MODE_PRIVATE);
		return sp.getString("NickName",null);
	}
    /**
     * 转换时间
     * @param date
     * @return
     */
    public static int  ChangDate(String date) {
        long time = 0;
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //得到指定模范的时间
        try {
            time = sdf.parse(date).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (int) time;
    }
    /**
     * 获取当前的时间
     * @return
     */
    public static String  getTime(){
        Date dt=new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyyMMdd");
        return matter1.format(dt);
    }
    public static boolean Compare(String Serverdate){
    	if(Math.abs((ChangDate(getTime()) -ChangDate(Serverdate) )/(24*3600*1000)) >=1) {
    		return true;
    		}else {
				return false;
			}
    }
	public static String  getThisTime(){
		Date dt=new Date();
		SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return matter1.format(dt);
	}
}
