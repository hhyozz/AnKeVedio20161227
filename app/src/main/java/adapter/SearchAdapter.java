package adapter;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anke.vedio.R;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

import model.Movies;
import model.ShortMovie;
import model.TvPlay;
import utis.UILUtils;

/**
 * Created by Administrator on 2016/12/24.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private ArrayList<BmobObject> mDate;


    public SearchAdapter(Context context, ArrayList<BmobObject> mDate){
        this.context = context;
        this.mDate = mDate;
    }


    public int getListCount(){return mDate.size();}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout=View.inflate(context, R.layout.item_like,null);
        MyViewHolder mViewHolder = new MyViewHolder(layout);
        layout.setOnClickListener(this);
        return mViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        System.out.println("position:"+position+"mDate.size:"+mDate.size());
        if(mDate.get(position) instanceof Movies)
        {
            Movies movies = (Movies)mDate.get(position);
            UILUtils.displayImageNoAnim(movies.getImageUrl(),holder.mImgIcon);
            holder.mTvName.setText(""+movies.getMoviename());
            holder.mTvInfo.setText(""+movies.getMovieInfo());
            holder.mTvType.setText("电影");
            holder.itemView.setTag(mDate.get(position));
        }
        else if(mDate.get(position) instanceof TvPlay)
        {
            TvPlay tvPlay = (TvPlay)mDate.get(position);
            UILUtils.displayImageNoAnim(tvPlay.getImageUrl(),holder.mImgIcon);
            holder.mTvName.setText("" + tvPlay.getMoviename());
            holder.mTvInfo.setText("" + tvPlay.getMovieInfo());
            holder.mTvType.setText("电视剧");
            holder.itemView.setTag(mDate.get(position));
        }
        else if(mDate.get(position) instanceof ShortMovie)
        {
            ShortMovie shortMovie = (ShortMovie)mDate.get(position);
            UILUtils.displayImageNoAnim(shortMovie.getImageUrl(),holder.mImgIcon);
            holder.mTvName.setText("" + shortMovie.getMoviename());
            holder.mTvInfo.setText("" + shortMovie.getPlot());
            holder.mTvType.setText("短片");
            holder.itemView.setTag(mDate.get(position));
        }

    }
    @Override
    public int getItemCount() {
        return mDate.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (BmobObject) v.getTag());
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
        void onItemClick(View view, BmobObject data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
