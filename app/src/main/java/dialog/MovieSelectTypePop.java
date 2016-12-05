package dialog;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import com.anke.vedio.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import adapter.SelectTypeAdatpter.MovieAddressAdapter;
import adapter.SelectTypeAdatpter.MovieScoreAdapter;
import adapter.SelectTypeAdatpter.MovieTypeAdapter;
import adapter.SelectTypeAdatpter.MovieYearAdapter;
import model.MovieType;
import views.HorizontalListView; //d   ddsf
/**
 */
public class MovieSelectTypePop extends PopupWindow{
    private ArrayList<String> mMovieType=new ArrayList<>();
    private ArrayList<String> mMovieAddress=new ArrayList<>();
    private ArrayList<String> mMovieYear=new ArrayList<>();
    private ArrayList<String> mMovieScore=new ArrayList<>();
    private final View view;
    private Context context;
    private Button mBtnTake;
    private Button mBtnSelect;
    private OnSelecListner listner;
    private Button mBtnCancle;
    private HorizontalListView mListScore;
    private HorizontalListView mListAddress;
    private HorizontalListView mListType;
    private HorizontalListView mListYear;
    private MovieTypeAdapter movieTypeadapter;
    private MovieAddressAdapter movieAddressAdapter;
    private MovieYearAdapter movieYearAdapter;
    private MovieScoreAdapter movieScoreAdapter;
    //电影类型
    private String MovieType;//类型
    private String MovieAddress;//地区
    private String MovieYear;//年份
    private String MovieScore;//评分
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public MovieSelectTypePop(Context context){
         view = View.inflate(context, R.layout.item_select_type, null);
        this.context = context;
        view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.fade_ins));
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setTouchable(true);
            setContentView(view);
            initViews();
            initdate();
            initListTypeItemSelect();
            initEvent();
            update();
        view.setOnTouchListener(new View.OnTouchListener()// 需要设置，点击之后取消popupview，即使点击外面，也可以捕获事件
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if (isShowing())
                {
                    dismiss();
                }
                return false;
            }
        });
    }
    /**
     * 类型的数据
     */
    private void initdate() {
        mMovieType.clear();
        mMovieType.add("剧情");
        mMovieType.add("爱情");
        mMovieType.add("喜剧");
        mMovieType.add("动画");
        mMovieType.add("科幻");
        mMovieType.add("魔幻");
        mMovieType.add("恐怖");
        mMovieType.add("动作");
        mMovieType.add("青春");
        mMovieType.add("文艺");
        mMovieType.add("励志");
        mMovieType.add("记录");
        mMovieType.add("战争");
        mMovieType.add("传记");
        mMovieType.add("家庭");
        mMovieType.add("犯罪");
        movieTypeadapter.notifyDataSetChanged();
        mMovieYear.clear();
        mMovieYear.add("2016");
        mMovieYear.add("2015");
        mMovieYear.add("2014");
        mMovieYear.add("2013");
        mMovieYear.add("2012");
        mMovieYear.add("2011");
        mMovieYear.add("2010");
        mMovieYear.add("2009");
        mMovieYear.add("2008");
        mMovieYear.add("2007");
        mMovieYear.add("2006");
        mMovieYear.add("2005");
        mMovieYear.add("2004");
        mMovieYear.add("2003");
        mMovieYear.add("2002");
        mMovieYear.add("2001");
        mMovieYear.add("2000");
        mMovieYear.add("90年代");
        mMovieYear.add("80年代");
        mMovieYear.add("70年代");
        mMovieYear.add("60年代");
        movieYearAdapter.notifyDataSetChanged();
        mMovieAddress.clear();
        mMovieAddress.add("中国");
        mMovieAddress.add("韩国");
        mMovieAddress.add("美国");
        mMovieAddress.add("日本");
        mMovieAddress.add("英国");
        mMovieAddress.add("法国");
        mMovieAddress.add("德国");
        mMovieAddress.add("泰国");
        mMovieAddress.add("印度");
        mMovieAddress.add("新加坡");
        mMovieAddress.add("西班牙");
        mMovieAddress.add("意大利");
        mMovieAddress.add("加拿大");
        mMovieAddress.add("澳大利亚");
        mMovieAddress.add("俄罗斯");
        mMovieAddress.add("伊朗");
        mMovieAddress.add("爱尔兰");
        mMovieAddress.add("瑞典");
        mMovieAddress.add("巴西");
        mMovieAddress.add("丹麦");
        mMovieAddress.add("墨西哥");
        mMovieAddress.add("新西兰");
        mMovieAddress.add("荷兰");
        mMovieAddress.add("奥地利");
        mMovieAddress.add("土耳其");
        mMovieAddress.add("匈牙利");
        mMovieAddress.add("以色列");
        mMovieAddress.add("波兰");
        mMovieAddress.add("捷克");
        mMovieAddress.add("阿根廷");
        mMovieAddress.add("比利时");
        movieAddressAdapter.notifyDataSetChanged();
        mMovieScore.clear();
        mMovieScore.add("9.0");
        mMovieScore.add("8.0");
        mMovieScore.add("7.0");
        mMovieScore.add("5.0");
        mMovieScore.add("更低 ");
        mMovieScore.add("枪版");
        mMovieScore.add("今更");
        mMovieScore.add("取消筛选");
        movieScoreAdapter.notifyDataSetChanged();
    }
    public static MovieSelectTypePop getInstance(Context context) {
        if( instance == null ) instance = new MovieSelectTypePop(context);
        return instance;
    }
    private static MovieSelectTypePop instance;
    /**
     * 查找控价
     */
    private void initViews() {
        mListScore = (HorizontalListView) view.findViewById(R.id.list_score);
        mListAddress = (HorizontalListView) view.findViewById(R.id.list_address);
        mListType = (HorizontalListView) view.findViewById(R.id.list_type);
        mListYear = (HorizontalListView) view.findViewById(R.id.list_year);
        movieTypeadapter = new MovieTypeAdapter(context, mMovieType);
        mListType.setAdapter(movieTypeadapter);
        movieAddressAdapter = new MovieAddressAdapter(context, mMovieAddress);
        mListAddress.setAdapter(movieAddressAdapter);
        movieYearAdapter = new MovieYearAdapter(context, mMovieYear);
        mListYear.setAdapter(movieYearAdapter);
        movieScoreAdapter = new MovieScoreAdapter(context, mMovieScore);
        mListScore.setAdapter(movieScoreAdapter);
    }
    /**
     * 事件处理
     */
    private void initEvent() {
        /**
         * 拍照
         */
//        mBtnTake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listner!=null){
//                    listner.onSelect("Take");
//                }
//                dismiss();
//            }
//        });
//        /**
//         * 选择本地图片
//         */
//        mBtnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listner!=null){
//                    listner.onSelect("Select");
//                }
//                dismiss();
//            }
//        });
//        mBtnCancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }
    /**
     * List的行点击事件
     */
    private void initListTypeItemSelect() {
        final model.MovieType movieType = new MovieType();
        mListType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                movieTypeadapter.setSelectIndex(position);
                movieTypeadapter.notifyDataSetChanged();
                MovieType=mMovieType.get(position);
                movieType.setMovieType(MovieType);
                EventBus.getDefault().post(movieType);
            }
        });
        mListYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                movieYearAdapter.setSelectIndex(position);
                movieYearAdapter.notifyDataSetChanged();
                MovieYear=mMovieYear.get(position);
                movieType.setMovieYear(MovieYear);
                EventBus.getDefault().post(movieType);
            }
        });
        mListAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                movieAddressAdapter.setSelectIndex(position);
                movieAddressAdapter.notifyDataSetChanged();
                MovieAddress=mMovieAddress.get(position);
                movieType.setMovieAddress(MovieAddress);
                EventBus.getDefault().post(movieType);
            }
        });
        mListScore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(mMovieScore.get(position).equals("取消筛选")){
                    dismiss();
                }else {
                    movieScoreAdapter.setSelectIndex(position);
                    movieScoreAdapter.notifyDataSetChanged();
                    MovieScore=mMovieScore.get(position);
                    movieType.setMovieScore(MovieScore);
                    EventBus.getDefault().post(movieType);
                }
            }
        });
    }
    public  interface  OnSelecListner{
         void onSelect(String Type);
    }
    public void setOnSelectTypeListner(OnSelecListner listner){
        this.listner = listner;
    }
}
