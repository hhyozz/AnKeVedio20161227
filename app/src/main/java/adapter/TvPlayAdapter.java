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
import model.TvPlay;

/**
 * Created by Administrator on 2016/11/1.
 */

public class TvPlayAdapter extends RecyclerView.Adapter<TvPlayAdapter.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private ArrayList<TvPlay> mDate;

    public TvPlayAdapter(Context context, ArrayList<TvPlay> mDate){
        this.context = context;
        this.mDate = mDate;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout=View.inflate(context, R.layout.item_movie,null);
        MyViewHolder mViewHolder = new MyViewHolder(layout);
        layout.setOnClickListener(this);
        return mViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UILUtils.displayImageNoAnim(mDate.get(position).getImageUrl(),holder.mImgIcon);
        holder.mTvName.setText(""+mDate.get(position).getMoviename());
        holder.mTvScore.setText(""+mDate.get(position).getScore());
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
            mOnItemClickListener.onItemClick(v, (TvPlay) v.getTag());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        TextView mTvScore;
        ImageView mImgIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTvName= (TextView) itemView.findViewById(R.id.tv_name);
            mTvScore= (TextView) itemView.findViewById(R.id.tv_score);
            mImgIcon= (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, TvPlay data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
