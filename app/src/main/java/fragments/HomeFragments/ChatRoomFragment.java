package fragments.HomeFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.anke.vedio.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Random;

import constance.constance;
import fragments.BaseFragment;
import rongyun.MD5;
import rongyun.StateCode;
import utis.GsonUtils;
import utis.utis;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import okhttp.OkHttpUtils;
import okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatRoomFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mRtlWenYi;
    private RelativeLayout mRtlLiZhi;
    private RelativeLayout mRtlXiJu;
    private RelativeLayout mRtlZhanZheng;
    private RelativeLayout mRtlKongBu;
    private RelativeLayout mRtlKeHuan;
    private RelativeLayout mRtlDongZuo;
    private RelativeLayout mRtlMoHuan;
    private RelativeLayout mRtlDongHua;
    private RelativeLayout mRtlTongNian;
    private RelativeLayout mRtlAiQing;
    private RelativeLayout mRtlYeHua;
    public ChatRoomFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_chat_room,null);
        initview(layout);
        return layout;
    }
    private void initview(View layout) {
        mRtlWenYi = (RelativeLayout) layout.findViewById(R.id.rtl_wenyi);
        mRtlLiZhi = (RelativeLayout) layout.findViewById(R.id.rtl_lizhi);
        mRtlXiJu = (RelativeLayout) layout.findViewById(R.id.rtl_xiju);
        mRtlZhanZheng = (RelativeLayout) layout.findViewById(R.id.rtl_zhanzheng);
        mRtlKongBu = (RelativeLayout) layout.findViewById(R.id.rtl_kongbu);
        mRtlKeHuan = (RelativeLayout) layout.findViewById(R.id.rtl_kehuan);
        mRtlDongZuo = (RelativeLayout) layout.findViewById(R.id.rtl_dongzuo);
        mRtlMoHuan = (RelativeLayout) layout.findViewById(R.id.rtl_mohuan);
        mRtlDongHua = (RelativeLayout) layout.findViewById(R.id.rtl_donghua);
        mRtlTongNian = (RelativeLayout) layout.findViewById(R.id.rtl_tongnian);
        mRtlAiQing = (RelativeLayout) layout.findViewById(R.id.rtl_aiqing);
        mRtlYeHua = (RelativeLayout) layout.findViewById(R.id.rtl_yehua);
        mRtlWenYi.setOnClickListener(this);
        mRtlLiZhi.setOnClickListener(this);
        mRtlXiJu.setOnClickListener(this);
        mRtlZhanZheng.setOnClickListener(this);
        mRtlKongBu.setOnClickListener(this);
        mRtlKeHuan.setOnClickListener(this);
        mRtlDongZuo.setOnClickListener(this);
        mRtlMoHuan.setOnClickListener(this);
        mRtlDongHua.setOnClickListener(this);
        mRtlTongNian.setOnClickListener(this);
        mRtlAiQing.setOnClickListener(this);
        mRtlYeHua.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rtl_wenyi:
                AddToChatRoom("1","文艺聊天室");
                break;
            case R.id.rtl_lizhi:
//                if (RongIM.getInstance() != null)
//                    RongIM.getInstance().startPrivateChat(getActivity(), "lss", "title");
                AddToChatRoom("2","励志聊天室");
                break;
            case R.id.rtl_xiju:
                AddToChatRoom("3","喜剧聊天室");
                break;
            case R.id.rtl_zhanzheng:
                AddToChatRoom("4","战争聊天室");
                break;
            case R.id.rtl_kongbu:
                AddToChatRoom("5","恐怖聊天室");
                break;
            case R.id.rtl_kehuan:
                AddToChatRoom("6","科幻聊天室");
                break;
            case R.id.rtl_dongzuo:
                AddToChatRoom("7","动作聊天室");
                break;
            case R.id.rtl_mohuan:
                AddToChatRoom("8","魔幻聊天室");
                break;
            case R.id.rtl_donghua:
                AddToChatRoom("9","动画聊天室");
                break;
            case R.id.rtl_tongnian:
                AddToChatRoom("10","童话聊天室");
                break;
            case R.id.rtl_aiqing:
                AddToChatRoom("11","爱情聊天室");
                break;
            case R.id.rtl_yehua:
                AddToChatRoom("12","夜话聊天室");
                break;
        }
    }
    private void AddToChatRoom(final String roomId, final String roomTitle){
        Random r = new Random();
        String Nonce = (r.nextInt(10000) + 10000) + "";
        String Timestamp = (System.currentTimeMillis() / 1000) + "";
        HashMap<String,String> map=new HashMap<>();
        map.put("userId", utis.GetId(getActivity()));
        map.put("chatroomId",roomId);
        OkHttpUtils
                .post()//
                .url("http://api.cn.ronghub.com/chatroom/join.json")//
                .params(map)
                .addHeader("App-Key", constance.KEY.RY_APP_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature", MD5.encryptToSHA(constance.KEY.RY_APP_SECRET + Nonce + Timestamp))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (e instanceof SocketTimeoutException) {
                            Toast("连接超时，加载失败");
                        } else if (e instanceof IOException) {
                            Toast("网络异常，请稍后重试.");
                        } else {
                            Toast("服务器异常，请稍后重试.");
                        }
                    }
                    @Override
                    public void onResponse(String response) {
                            Log.i("加入聊天室返回值========",""+response.toString());
                        StateCode stateCode = GsonUtils.parseJSON(response, StateCode.class);
                        if(stateCode.getCode().equals("200")){
                            if(RongIM.getInstance()!=null){
                                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.CHATROOM, roomId, roomTitle);
                            }
                        }else {
                        }
                    }
                });
    }
}
