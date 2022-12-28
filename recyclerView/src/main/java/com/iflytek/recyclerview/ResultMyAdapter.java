package com.iflytek.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class ResultMyAdapter extends RecyclerView.Adapter<ResultMyAdapter.ViewHolder>  {
    private  List<TestItemModel> mDataList = new ArrayList<>();
    private  List<TestItemModel> filterList = new ArrayList<>();
    private Context mContext;


    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentView;
        TextView nameView;
        CheckBox cb;

        public ViewHolder(View view) {
            super(view);
            parentView = view.findViewById(R.id.item_parent);
            nameView = view.findViewById(R.id.name);
            cb = view.findViewById(R.id.cb);
        }
    }


    public  ResultMyAdapter(Context context, List<TestItemModel> data) {
        this.mContext = context;
        this.mDataList = data;
        this.filterList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyc_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(filterList.get(position).getItemName());
        holder.cb.setChecked(filterList.get(position).isCheck());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
}