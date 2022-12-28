package com.iflytek.application;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.ViewHolder> {

    List<String> filterList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;

        public ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.tv_name);
        }
    }

    public RightAdapter(List<String> data) {
        this.filterList = data;
    }

    private OnMyItemClickListener listener;

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.listener = listener;

    }

    public interface OnMyItemClickListener {
        void myClick(View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String people = filterList.get(position);
        holder.nameView.setText(people);
        holder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.myClick(v, position);
            }
        });
        Log.i("testNg", "getItemName()=" + people);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
}
