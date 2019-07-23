package com.hai.ireader.widget.modialog.smartisan.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.hai.ireader.R;
import com.hai.ireader.widget.modialog.smartisan.listener.OnOptionItemSelectListener;

public class OptionListAdapter extends RecyclerView.Adapter<OptionListAdapter.ViewHolder> {
    private CharSequence[] mOptionList;
    private int mLast = 0;
    @ColorInt
    private int mLastColor;
    private OnOptionItemSelectListener mListener;
    private int mItemGravity;

    public OptionListAdapter(CharSequence[] optionList, int last, @ColorInt int lastColor) {
        this.mOptionList = optionList;
        this.mLastColor = lastColor;
        if (!TextUtils.isEmpty(optionList[last])) {
            this.mLast = last;
        }
    }

    public void setItemGravity(int gravity) {
        this.mItemGravity = gravity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ddh_sm_item_option_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CharSequence option = mOptionList[position];
        holder.mTvItemName.setText(option);
        holder.mTvItemName.setGravity(mItemGravity);
        if (option.toString().equals(this.mOptionList[mLast])) {
            holder.mTvItemName.setTextColor(mLastColor);
        } else {
            holder.mTvItemName.setTextColor(0xff333333);
        }
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSelect(holder.getAdapterPosition(), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mOptionList == null) return 0;
        return mOptionList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlItem;
        private TextView mTvItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            mRlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            mTvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
        }
    }

    public void setOnOptionItemSelectListener(OnOptionItemSelectListener listener) {
        this.mListener = listener;
    }
}
