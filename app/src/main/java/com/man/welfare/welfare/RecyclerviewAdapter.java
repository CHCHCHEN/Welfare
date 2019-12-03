package com.man.welfare.welfare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<fuliBeen.ResultsBean> data;

    public RecyclerviewAdapter(Context context, List<fuliBeen.ResultsBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //预先加载固定图片和加载超时图片
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.preloading)
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);//让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）
                //.override(Target.SIZE_ORIGINAL);//加载图片的原始尺寸,会面临着更高的OOM风险
        GlideUtil.load(context, data.get(position).getUrl(), holder.ivImageView, options);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImageView = itemView.findViewById(R.id.img);
        }
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<fuliBeen.ResultsBean> addMessageList) {
        //增加数据
        int position = data.size();
        data.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<fuliBeen.ResultsBean> newList) {
        //刷新数据
        data.removeAll(data);
        data.addAll(newList);
        notifyDataSetChanged();
    }
}
