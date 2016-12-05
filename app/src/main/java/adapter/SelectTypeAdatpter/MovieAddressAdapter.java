package adapter.SelectTypeAdatpter;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anke.vedio.R;

public class MovieAddressAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<String> mDate;
	private ViewHold viewHold;
	private OnSetRedListner listner;
	private int selectIndex=-1;
	public MovieAddressAdapter(Context context,ArrayList<String> mDate) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item_movie_type_text, null);
			viewHold = new ViewHold();
            viewHold.TvTitle=(TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(viewHold);
        }else {
            viewHold= (ViewHold) convertView.getTag();
        }
		if(listner!=null){
			listner.onSetRed(viewHold.TvTitle);
		}
		if(position == selectIndex){
			convertView.setSelected(true);
			viewHold.TvTitle.setTextColor(Color.GREEN);
		}else{
			convertView.setSelected(false);
		}    
        viewHold.TvTitle.setText(mDate.get(position));;
        return convertView;
    }
	
	public void SetRed(){
		viewHold.TvTitle.setTextColor(Color.RED);
	}
	private class  ViewHold{
	    TextView TvTitle;
	}
	
	 public  interface  OnSetRedListner{
         void onSetRed(TextView mTextView);
    }
    public void setOnSetRedListner(OnSetRedListner listner){
		this.listner = listner;
    }
    public void setSelectIndex(int i){
		selectIndex = i;
	}
}
