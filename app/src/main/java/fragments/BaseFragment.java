package fragments;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import dialog.CustomProgressDialog;

public class BaseFragment extends Fragment {
	public final static int RESULT_CLOSE_ACTIVITY = -9999;
	private static ArrayList<BaseFragment> activityList = new ArrayList<BaseFragment>();
	private CustomProgressDialog progressDialog = null;
	private boolean background = false;
	private Toast tip;
	private ProgressDialog loading;
	private int loadingCount = 0; // loading��������ʱ�ж���
	private Map<View, Runnable> touchOutsideListenerMap = new Hashtable<>();
	private Map<View, Runnable> touchListenerMap = new Hashtable<>();
	private boolean containVisiableStartActivity = false;
	public boolean IsConnectNet=true;
	/**
	 */
	public static boolean isAllActivityBackground() {
		for(BaseFragment a : activityList) {
			if( !a.isBackground() ) return false;
		}
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityList.add(this);
	}
	public void startProgressDialog(String msg){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(getActivity());
			if(!"".equals(msg)) {
				progressDialog.setMessage(msg);
			}
		}
		progressDialog.show();
	}
	public void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	/**
	 */
	public boolean isBackground() {
		return background;
	}
	/**
	 */
	public void setViewOutsideClickListener(View v, Runnable l) {
		if( v != null ) {
			if( l != null ) touchOutsideListenerMap.put(v, l);
			else touchOutsideListenerMap.remove(v);
		}
	}
	/**
	 */
	public void setViewClickListener(View v, Runnable r) {
		if( v != null ) {
			if( r != null ) touchListenerMap.put(v, r);
			else  touchListenerMap.remove(v);
		}
	}
	public void onDestroy() {
		background = true;
		activityList.remove(this);
		super.onDestroy();
	}
	public void onResume() {
		background = false;
//		AndroidSystem.getInstance().clearNotification();
		super.onResume();
	}
	public void onStop() {
		background = true;
		super.onStop();
	}
	public  void ShowDialogMessage(String msg){
		new android.support.v7.app.AlertDialog.Builder(getActivity()).setTitle("提示").setMessage(msg).setNegativeButton(
				"确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
	public void Toast(String msg){
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
	}
}
