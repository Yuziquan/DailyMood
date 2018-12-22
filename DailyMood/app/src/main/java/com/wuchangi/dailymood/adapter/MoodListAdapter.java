package com.wuchangi.dailymood.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuchangi.dailymood.R;
import com.wuchangi.dailymood.activity.MoodDetailActivity;
import com.wuchangi.dailymood.bean.Mood;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by WuchangI on 2018/12/22.
 */

public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.ViewHolder> {

    private Context mContext;
    private List<Mood> mMoodList;

    public MoodListAdapter(Context context, List<Mood> moodList){
        mContext = context;
        mMoodList = moodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Mood mood = mMoodList.get(position);

        Glide.with(mContext).load(mood.getPicturePath())
                .apply(bitmapTransform(new BlurTransformation(5, 3)))
                .into(holder.mIvCover);
        holder.mTvDate.setText(mood.getDate());

        holder.mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoodDetailActivity.actionStart(mContext, mood.getDate());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMoodList.size();
    }

    public void replaceMood(Mood mood){
        for(Mood m: mMoodList){
            if(m.getDate().equals(mood.getDate())){
                m.setDescription(mood.getDescription());
                m.setPicturePath(mood.getPicturePath());
                break;
            }
        }

        notifyDataSetChanged();
    }

    public void addMood(Mood mood){
        mMoodList.add(mood);
        notifyItemInserted(mMoodList.size() - 1);
    }

    public void removeAllMoods(){
        mMoodList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_cover)
        ImageView mIvCover;

        @BindView(R.id.tv_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
