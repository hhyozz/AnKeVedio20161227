package activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.anke.vedio.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import dialog.CustomProgressDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends FragmentActivity {
	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public static final int NETTYPE_WIFI = 11;
	public static final int NETTYPE_CMWAP = 12;
	public static final int NETTYPE_CMNET = 13;
	public final static int RESULT_CLOSE_ACTIVITY = -9999;
	private static final int READ_PHONE_STATE = 0;
	private static ArrayList<BaseActivity> activityList = new ArrayList<BaseActivity>();
	private boolean background = false;
	private Toast tip;
	private ProgressDialog loading;
	private Map<View, Runnable> touchOutsideListenerMap = new Hashtable<>();
	private CustomProgressDialog progressDialog = null;
	private int loadingCount = 0; // loading��������ʱ�ж���
	private Map<View, Runnable> touchListenerMap = new Hashtable<>();
	private boolean containVisiableStartActivity = false;
	private boolean IsConnectNet=true;
	private FrameLayout group;
	private View statusBarView;
	private View content;
	/**
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}
	public  void IsNewConnect(boolean IsConnectNet){
		Toast("连接网络网站"+IsConnectNet);
	}
	public void onDestroy() {
		background = true;
		activityList.remove(this);
		super.onDestroy();
		if (this.mCompositeSubscription != null) {
			this.mCompositeSubscription.unsubscribe();
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStatus();
	}
	private void initStatus() {
		group = new FrameLayout(getApplicationContext());
		// 创建一个statusBarView 占位，填充状态栏的区域，以后设置状态栏背景效果其实是设置这个view的背景。
		group.addView(statusBarView = createStatusBar());
		super.setContentView(group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		// 设置activity的window布局从状态栏开始
		setTranslucentStatus(true);
		setStatusBarColorBg(R.color.white);
	}
	/**
	 * 解决Subscription内存泄露问题
	 * @param s
	 */
	private CompositeSubscription mCompositeSubscription;
	protected void addSubscription(Subscription s) {
		if (this.mCompositeSubscription == null) {
			this.mCompositeSubscription = new CompositeSubscription();
		}
		this.mCompositeSubscription.add(s);
	}

	/**
	 * 设置状态栏的颜色
	 *
	 * @param color
	 */
	public void setStatusBarColorBg(int color) {
		statusBarView.setBackgroundColor(color);
	}
	public void setTranslucentStatus(boolean isTranslucentStatus) {
		if (isTranslucentStatus) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
			}
			statusBarView.setVisibility(View.VISIBLE);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
				localLayoutParams.flags = ((~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) & localLayoutParams.flags);
			}
			statusBarView.setVisibility(View.GONE);
		}
	}
	/**
	 * 创建状态栏填充的 statusBarView
	 *
	 * @return
	 */
	private View createStatusBar() {
		int height = getStatusBarHeight();
		View statusBarView = new View(this);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		statusBarView.setBackgroundResource(R.color.white);
		statusBarView.setLayoutParams(lp);
		return statusBarView;
	}
	/**
	 * 获取状态的高度
	 * @return
	 */
	public int getStatusBarHeight() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int result = 0;
			int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = getResources().getDimensionPixelSize(resourceId);
			}
			return result;
		}
		return 0;
	}
	public void onResume() {
		background = false;
		super.onResume();
	}
	public void Toast(String msg){
		Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	}
	public void startProgressDialog(String msg){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
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
	public boolean isTelNumber(String tel){
		String telRegex = "[1][3758]\\d{9}";
		if(!tel.matches(telRegex)){
			return false;
		}
		return true;
	}


}
