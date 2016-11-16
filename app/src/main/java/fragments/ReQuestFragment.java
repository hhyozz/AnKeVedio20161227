package fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.anke.vedio.R;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import utis.utis;
import cn.bmob.v3.listener.SaveListener;
import model.ReQuest;

public class ReQuestFragment extends BaseFragment {


    private RelativeLayout mRtlSend;
    private Spinner mSpinner;
    private EditText mEtName;
    private ArrayList<String> mDate=new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;
    private String Type="";
    public ReQuestFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_re_quest,null);
        initview(layout);
        initdate();
        inievent();
        return layout;
    }
    private void initdate() {
        mDate.clear();
        mDate.add("电影");
        mDate.add("短片");
        mDate.add("电视剧");
        arr_adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mDate);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arr_adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Type=mDate.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void inievent() {
        /**
         * send
         */
        mRtlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgressDialog("加载中...");
                ReQuest reQuest = new ReQuest();
                reQuest.setUserid(utis.GetId(getActivity()));
                reQuest.setName(mEtName.getText().toString().trim());
                reQuest.setType(Type);
                reQuest.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                         if(e==null){
                             stopProgressDialog();
                             Toast("发送成功");
                         }else {
                             Toast("数据连接异常，请稍后重试");
                         }
                    }
                })      ;
            }
        });
    }

    private void initview(View layout) {
        mRtlSend = (RelativeLayout) layout.findViewById(R.id.rtl_send);
        mSpinner = (Spinner) layout.findViewById(R.id.spinner);
        mEtName = (EditText) layout.findViewById(R.id.et_name);
    }
}
