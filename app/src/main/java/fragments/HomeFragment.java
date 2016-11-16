package fragments;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anke.vedio.R;

import java.util.ArrayList;

import fragments.HomeFragments.ChatRoomFragment;
import fragments.HomeFragments.MovieFragment;
import fragments.HomeFragments.ShortMovieFragment;
import fragments.HomeFragments.TvPalyFragment;

public class HomeFragment extends Fragment {
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
    public HomeFragment() {
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
    private void initevent() {

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
    }
}
