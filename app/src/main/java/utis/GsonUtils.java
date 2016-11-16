package utis;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public final class GsonUtils {

	public static <T> T parseJSON(String json, Class<T> clazz){
		T info = null;
		try {
			Gson gson = new Gson();
			info =  gson.fromJson(json, clazz);
		}catch (Exception e){
			Log.e("解析json异常=======",""+e.toString());
		}
		return info;
	}
	
	/**
	 */
	public static <T> T parseJSONArray(String jsonArr, Type type) {
		Gson gson = new Gson();
		T infos = gson.fromJson(jsonArr, type);
		return infos;
	}
	
	
	
	private GsonUtils(){}
}
