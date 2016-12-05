package fragments;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anke.vedio.R;

import java.util.ArrayList;

import activity.LoginActivity;
import activity.MainActivity;
import activity.RecomandActivity;
import activity.UpdateUserHeadActivity;
import dialog.MainPopMenu;
import dialog.MovieSelectTypePop;
import fragments.HomeFragments.ChatRoomFragment;
import fragments.HomeFragments.MovieFragment;
import fragments.HomeFragments.SearchFragment;
import fragments.HomeFragments.ShortMovieFragment;
import fragments.HomeFragments.TvPalyFragment;
import utis.SharePre;

public class HomeFragment extends BaseFragment  implements Animator.AnimatorListener , View.OnClickListener {
    private ArrayList<String> mDate=new ArrayList<>();
    private Handler mHandler = new Handler();
    private ViewPager viewPager;
    private MovieFragment movieFragment;
    private ShortMovieFragment movieTypeFragment;
    private ChatRoomFragment chatRoomFragment;
    private TvPalyFragment tvPalyFragment;
    private ArrayList<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    private int Page=10000;
    private ImageView mImgMenu;
    private ImageView mImgSearch;
    private ImageView mImgDownload;
    private ImageView mImgHistory;
    private ImageView mImgHead;
    private TextView mTvSearchCancle;
   // private RelativeLayout mRtlSearch;
    private RelativeLayout mRtlMainMenu;


    private View mLayout;
    private View mSearchlayout;
    private MainActivity.MyOnTouchListener myOnTouchListener;
    private boolean mIsTitleHide = false;
    private boolean mIsAnim = false;
    private float lastX = 0;
    private float lastY = 0;
    private ImageView mImgSelect;
    private int Pos=0;

    public HomeFragment() {
        super();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_home,null);
        initview(layout);
        initPage();
        initevent();
        return layout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayout = getActivity().findViewById(R.id.layout);
        mSearchlayout = getActivity().findViewById(R.id.search_layout);
        initListener();
    }
    private void initevent() {
        mImgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast("第"+Pos+"页");
                MovieSelectTypePop movieSelectTypePop = new MovieSelectTypePop(getActivity());
                movieSelectTypePop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });
    }
    /**
     * viewpage
     */
    private void initPage() {
        viewPager.setOffscreenPageLimit(2);
        mFragments=new ArrayList<Fragment>();
        if(movieFragment==null){
            mFragments.add(new MovieFragment());
        }else {
            mFragments.add(movieFragment);
        }
        if(tvPalyFragment==null){
            mFragments.add(new TvPalyFragment());
        }else {
            mFragments.add(tvPalyFragment);
        }
        if(movieTypeFragment==null){
            mFragments.add(new ShortMovieFragment());
        }else {
            mFragments.add(movieTypeFragment);
        }
        if(chatRoomFragment==null){
            mFragments.add(new ChatRoomFragment());
        }else {
            mFragments.add(chatRoomFragment);
        }
        mAdapter=new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Log.i("滑动页面的position",""+position);
                Pos=position;
                if(position>4){
                    viewPager.setCurrentItem(0);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
    }

    private void initview(View layout) {
        viewPager = (ViewPager) layout.findViewById(R.id.viewpage);
        mImgSelect = (ImageView) layout.findViewById(R.id.img_select);

        mTvSearchCancle = (TextView) layout.findViewById(R.id.tv_search_cancle);
        //mRtlSearch = (RelativeLayout) layout.findViewById(R.id.rtl_search);
        mRtlMainMenu = (RelativeLayout) layout.findViewById(R.id.rtl_main_menus);
        mImgHead = (ImageView) layout.findViewById(R.id.img_head);
        mImgSearch = (ImageView) layout.findViewById(R.id.img_search);
        mImgMenu = (ImageView) layout.findViewById(R.id.img_menu);
        mImgHistory = (ImageView) layout.findViewById(R.id.img_history);
        mImgDownload = (ImageView) layout.findViewById(R.id.img_download);

        mImgMenu.setOnClickListener(this);
        mImgSearch.setOnClickListener(this);
        mImgHistory.setOnClickListener(this);
        mImgDownload.setOnClickListener(this);
        mTvSearchCancle.setOnClickListener(this);
    }

    private void initListener() {
        myOnTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                return dispathTouchEvent(ev);
            }
        };
        ((MainActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
    }

    private boolean isDown = false;
    private boolean isUp = false;
    private boolean dispathTouchEvent(MotionEvent event){
        if (mIsAnim) {
            return false;
        }
        final int action = event.getAction();

        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                lastX = x;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dY = Math.abs(y - lastY);
                float dX = Math.abs(x - lastX);
                boolean down = y > lastY ? true : false;
                lastY = y;
                lastX = x;
                isUp = dX < 8 && dY > 8 && !mIsTitleHide && !down ;
                isDown = dX < 8 && dY > 8 && mIsTitleHide && down;
                if (isUp) {
                    View view = this.mLayout;
                    float[] f = new float[2];
                    f[0] = 0.0F;
                    f[1] = -mSearchlayout.getHeight();
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", f);
                    animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator1.setDuration(200);
                    animator1.start();
                    animator1.addListener(this);
                    setMarginTop(mLayout.getHeight() - mSearchlayout.getHeight());
                } else if (isDown) {
                    View view = this.mLayout;
                    float[] f = new float[2];
                    f[0] = -mSearchlayout.getHeight();
                    f[1] = 0F;
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationY", f);
                    animator1.setDuration(200);
                    animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator1.start();
                    animator1.addListener(this);
                } else {
                    return false;
                }
                mIsTitleHide = !mIsTitleHide;
                mIsAnim = true;
                break;
            default:
                return false;
        }
        return false;

    }

    @Override
    public void onAnimationCancel(Animator arg0) {

    }


    @Override
    public void onAnimationEnd(Animator arg0) {
        if(isDown){
            setMarginTop(mLayout.getHeight());
        }
        mIsAnim = false;
    }
    @Override
    public void onAnimationRepeat(Animator arg0) {

    }
    @Override
    public void onAnimationStart(Animator arg0) {

    }
    public void setMarginTop(int page){
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParam.setMargins(0, page, 0, 0);
        viewPager.setLayoutParams(layoutParam);
        viewPager.invalidate();
    }
    @Override
    public void onClick(View v) {
       // FragmentManager fm =getSupportFragmentManager();
        FragmentManager fm =getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()){
            case R.id.img_menu://侧滑菜单
                Menues(v);
                break;
            case R.id.img_search://搜索
                mRtlMainMenu.setVisibility(View.GONE);
                //mRtlSearch.setVisibility(View.VISIBLE);
                ft.replace(R.id.fl_main,new SearchFragment());
                break;
            case R.id.img_history://历史记录
                ft.replace(R.id.fl_main,new HistoryFragment());
                break;
            case R.id.img_download://下载
                ft.replace(R.id.fl_main,new DownLoadFragment());
                break;

        }
        ft.commit();
    }

    private void Menues(View v) {
        MainPopMenu mainPopMenu = new MainPopMenu(getActivity());
        mainPopMenu.showAtLocation(v, Gravity.LEFT | Gravity.TOP, 0, 0);
        mainPopMenu.setOnSelectOnclickListner(new MainPopMenu.OnSelecListner() {
            @Override
            public void onSelect(String Type) {
               // FragmentManager fm = getSupportFragmentManager();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (Type) {
                    case "look":
                        ft.replace(R.id.fl_main, new LookFragment());
                        break;
                    case "home":
                        ft.replace(R.id.fl_main, new HomeFragment());
                        break;
                    case "collect":
                        ft.replace(R.id.fl_main, new CollectFragment());
                        break;
                    case "recomand":
                        Intent intent_tj = new Intent(getActivity(), RecomandActivity.class);
                        startActivity(intent_tj);
                        break;
                    case "request":
                        ft.replace(R.id.fl_main, new ReQuestFragment());
                        break;
                    case "update":
                        Intent intent = new Intent(getActivity(), UpdateUserHeadActivity.class);
                        startActivityForResult(intent, 11);
                        break;
                    case "loginout":
                        SharePre.saveLoginState(getActivity(), false);
                        Intent intent_loginout = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent_loginout);
                        getActivity().finish();
                        break;
                }
                ft.commit();
            }
        });
    }
}
