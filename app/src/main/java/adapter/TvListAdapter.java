package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anke.vedio.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/8.
 */

public class TvListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mDate;
    private ViewHold viewHold;

    private TvListAdapter(){
    }
    public TvListAdapter(Context context, ArrayList<String> mDate){
        this.context = context;
        this.mDate = mDate;
    }
    @Override
    public int getCount() {
        return mDate.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_tvlist, null);
            viewHold = new ViewHold();
            viewHold.TvNum= (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(viewHold);
        }else {
            viewHold= (ViewHold) convertView.getTag();
        }
        viewHold.TvNum.setText(""+(position+1));
        return convertView;
    }
    class ViewHold{
       TextView TvNum;
    }
}
