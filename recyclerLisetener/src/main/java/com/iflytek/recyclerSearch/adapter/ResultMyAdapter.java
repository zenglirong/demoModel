package com.iflytek.recyclerSearch.adapter;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.recyclerSearch.R;
import com.iflytek.recyclerSearch.data.TestItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultMyAdapter extends RecyclerView.Adapter<ResultMyAdapter.ViewHolder> implements Filterable, View.OnClickListener {

    private OnClickItemListener listener;
    private final List<TestItemModel> mDataList;
    private List<TestItemModel> filterList;
    private String charStr;

    public void OnClickItemListener(OnClickItemListener onClickListener) {
        this.listener = onClickListener;
    }

    public interface OnClickItemListener {
        void onClick(View view, int position);
    }

    @Override
    public void onClick(View view) {

        if (listener == null) return;
        listener.onClick(view, (int) view.getTag());
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentView;
        TextView nameView;
        TextView timeView;
        TextView resultView;
        CheckBox cb;

        public ViewHolder(View view) {
            super(view);
            parentView = view.findViewById(R.id.item_parent);
            nameView = view.findViewById(R.id.test_name_id);
            timeView = view.findViewById(R.id.test_time_id);
            resultView = view.findViewById(R.id.test_result_id);
            cb = view.findViewById(R.id.cb);

            timeView.setOnClickListener(ResultMyAdapter.this);
            resultView.setOnClickListener(ResultMyAdapter.this);
        }
    }


    public ResultMyAdapter(List<TestItemModel> data) {
        this.mDataList = data;
        this.filterList = data;
        runOnUiThread(this::notifyDataSetChanged);
    }

    @NonNull
    @Override
    public ResultMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyc_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestItemModel people = filterList.get(position);

        if (!TextUtils.isEmpty(charStr)) {
            SpannableString str = matchSearch(people.getItemName(), charStr);
            Log.i("burgess", "str=" + str);
            holder.nameView.setText(str);
        } else {
            Log.i("burgess", "people.getItemName()=" + people.getItemName());
            holder.nameView.setText(people.getItemName());
        }

        holder.timeView.setTag(position);
        holder.resultView.setTag(position);
        holder.cb.setChecked(people.isCheck());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charsequence) {
                charStr = charsequence.toString();
                List<TestItemModel> filteredList = new ArrayList<>();
                if (TextUtils.isEmpty(charStr)) {
                    filteredList = mDataList;
                } else {
                    for (int i = 0; i < mDataList.size(); i++) {
                        if (mDataList.get(i).getItemName().contains(charStr)) {
                            filteredList.add(mDataList.get(i));
                        }
                    }
                }
                FilterResults filterRes = new FilterResults();
                filterRes.values = filteredList;
                return filterRes;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence p1, FilterResults p2) {
                filterList = (List<TestItemModel>) p2.values;//??????????????????????????????
                runOnUiThread(() -> notifyDataSetChanged());

            }
        };
    }

    private SpannableString matchSearch(String str, String key) {
        SpannableString ss = new SpannableString(str);  // ?????????????????????????????????????????????????????????
        Matcher m = Pattern.compile(key).matcher(ss);

        while (m.find()) {
            ss.setSpan(new ForegroundColorSpan(Color.RED), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }
}