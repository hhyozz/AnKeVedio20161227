package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anke.vedio.R;

import java.util.ArrayList;

import utis.UILUtils;
import model.Collect;
import model.look;

/**
 * Created by Administrator on 2016/11/1.
 */

public class LookAdapter extends RecyclerView.Adapter<LookAdapter.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private ArrayList<look> mDate;

    public LookAdapter(Context context, ArrayList<look> mDate){
        this.context = context;
        this.mDate = mDate;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout=View.inflate(context, R.layout.item_like,null);
        MyViewHolder mViewHolder = new MyViewHolder(layout);
        layout.setOnClickListener(this);
        return mViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UILUtils.displayImageNoAnim(mDate.get(position).getImageUrl(),holder.mImgIcon);
        holder.mTvName.setText(""+mDate.get(position).getMoviename());
        holder.mTvInfo.setText(""+mDate.get(position).getMovieInfo());
        holder.mTvType.setText("电视剧");
        holder.itemView.setTag(mDate.get(position));
    }
    @Override
    public int getItemCount() {
        return mDate.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Collect) v.getTag());
        }
    }                  ;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTvInfo;
        TextView mTvType;
        TextView mTvName;
        ImageView mImgIcon;
        ImageView mImgPlay;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTvName= (TextView) itemView.findViewById(R.id.tv_name);
            mTvInfo= (TextView) itemView.findViewById(R.id.tv_info);
            mTvType= (TextView) itemView.findViewById(R.id.tv_type);
            mImgIcon= (ImageView) itemView.findViewById(R.id.img_icon);
            mImgPlay= (ImageView) itemView.findViewById(R.id.img_play);
        }
    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Collect data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
