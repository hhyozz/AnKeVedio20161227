package dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.anke.vedio.R;

import utis.UILUtils;
import utis.Util;
import utis.utis;
import views.CircleImageView;

/**
 * Created by Administrator on 2016/10/21.
 */
public class MainPopMenu extends PopupWindow{
    private int menuWindowWidth = 250; //
    private RelativeLayout mRtlLook;
    private RelativeLayout mRtlCollect;
    private RelativeLayout mRtlRecomand;
    private RelativeLayout mRtlReQuest;
    private OnSelecListner listner;
    private LinearLayout mLlUpdateInfo;
    private RelativeLayout mRtlLoginOut;
    private RelativeLayout mRtlHome;
    private Context context;
    private CircleImageView mImgHead;

    public  MainPopMenu(Context context){
        this.context = context;
        View view = View.inflate(context, R.layout.main_menu_popupwindow, null);
        setAnimationStyle(R.style.menu_window_anim);
        setWidth(Util.dip2px(context,menuWindowWidth));
        setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        initViews(view);
        initdate();
        initEvent();
    }

    private void initdate() {
        UILUtils.displayImageNoAnim(utis.GetImgHeadUrl(context),mImgHead);
    }
    private void initEvent() {
        /**
         * 关注
         */
        mRtlLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                   listner.onSelect("look");
                }
                dismiss();
            }
        });
        /**
         * 收藏
         */
        mRtlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("collect");
                }
                dismiss();
            }
        });
        /**
         * 推荐
         */
        mRtlRecomand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("recomand");
                }
                dismiss();
            }
        });
        /**
         * 求片
         */
        mRtlReQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("request");
                }
                dismiss();
            }
        }); /**
         * 求片
         */
        mLlUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("update");
                }
                dismiss();
            }
        }); /**
         * 退出账号
         */
        mRtlLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("loginout");
                }
                dismiss();
            }
        }); /**
         * 首页
         */
        mRtlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onSelect("home");
                }
                dismiss();
            }
        });
    }
    private void initViews(View view) {
        mImgHead = (CircleImageView) view.findViewById(R.id.img_icon);
        mRtlLook = (RelativeLayout) view.findViewById(R.id.rtl_look);
        mRtlCollect = (RelativeLayout) view.findViewById(R.id.rtl_collect);
        mRtlRecomand = (RelativeLayout) view.findViewById(R.id.rtl_recomand);
        mRtlReQuest = (RelativeLayout) view.findViewById(R.id.rtl_request);
        mLlUpdateInfo = (LinearLayout) view.findViewById(R.id.ll_update_info);
        mRtlLoginOut = (RelativeLayout) view.findViewById(R.id.rtl_loginout);
        mRtlHome = (RelativeLayout) view.findViewById(R.id.rtl_home);
    }

    public  interface  OnSelecListner{
        void onSelect(String Type);
    }
    public void setOnSelectOnclickListner(OnSelecListner listner){
        this.listner = listner;
    }
}
