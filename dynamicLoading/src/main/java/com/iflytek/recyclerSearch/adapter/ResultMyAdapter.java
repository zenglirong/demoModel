package com.iflytek.recyclerSearch.adapter;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

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

public class ResultMyAdapter extends RecyclerView.Adapter<ResultMyAdapter.ViewHolder> implements Filterable {
    private List<TestItemModel> tempArraylist = new ArrayList<>();
    private List<TestItemModel> mDataList;
    private List<TestItemModel> filterList;
    private TestItemModel people;
    private String charStr;

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


    public ResultMyAdapter(List<TestItemModel> data) {
        this.mDataList = data;
        this.filterList = data;
        runOnUiThread(() -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public ResultMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyc_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        people = filterList.get(position);

        if (!TextUtils.isEmpty(charStr)) {
            SpannableString str = matchSearch(people.getItemName(), charStr);
            holder.nameView.setText(str);
        } else {
            holder.nameView.setText(people.getItemName());
            setCb(holder, position);
        }
        holder.cb.setChecked(people.isCheck());
    }

    void setCb(ViewHolder holder, int position) {
        holder.cb.setOnClickListener(view -> {
            CheckBox cb = (CheckBox) view;

            tempArraylist.add(new TestItemModel(cb.isChecked(), filterList.get(position).getItemName(), filterList.get(position).getItemClass()));
            filterList.get(position).setCheck(cb.isChecked());

            if (!cb.isChecked())
            for (int i = 0; i < tempArraylist.size(); i++)
            {
                if (tempArraylist.get(i).getItemName().contains(filterList.get(position).getItemName())) {
                  //  tempArraylist.remove(i);
                }
            }
            Log.i("TAG", "remove1 " + tempArraylist);

        });
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

            @Override
            protected void publishResults(CharSequence p1, FilterResults p2) {
                filterList = (List<TestItemModel>) p2.values;//??????????????????????????????
                runOnUiThread(() -> notifyDataSetChanged());
            }
        };
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public ArrayList<TestItemModel> getSelectItem() {
        ArrayList<TestItemModel> storeArraylist = new ArrayList<>();
        for (TestItemModel mSelectItem : tempArraylist) {
            if (mSelectItem.isCheck()) {
//                Log.i("burgess","getSelectItem=mSelectItem.getItemName()="+mSelectItem.getItemName());
                storeArraylist.add(new TestItemModel(mSelectItem.getItemName(), mSelectItem.getItemClass()));
            }
        }
//        Log.i("burgess","getSelectItem=tempArraylist="+tempArraylist);
//        Log.i("burgess","getSelectItem=storeArraylist="+storeArraylist);
        tempArraylist.clear();
        runOnUiThread(() -> notifyDataSetChanged());
        return storeArraylist;
    }

    public void selectAll(boolean isSelectAll) {
//        tempArraylist.clear();
//        for (TestItemModel firstModel : filterList) {
//            firstModel.setCheck(isSelectAll);
//            tempArraylist.add(new TestItemModel(true, firstModel.getItemName(), firstModel.getItemClass()));
//        }
//        runOnUiThread(() -> notifyDataSetChanged());
    }

    public void clearAll() {
        for (TestItemModel clearModel : filterList) {
            clearModel.setCheck(false);
        }
        runOnUiThread(() -> notifyDataSetChanged());
    }
}