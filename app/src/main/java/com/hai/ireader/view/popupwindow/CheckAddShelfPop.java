//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.hai.ireader.view.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hai.ireader.R;

public class CheckAddShelfPop extends PopupWindow {
    private Context mContext;
    private View view;
    private OnItemClickListener itemClick;
    private String bookName;

    @SuppressLint("InflateParams")
    public CheckAddShelfPop(Context context, @NonNull String bookName, @NonNull OnItemClickListener itemClick) {
        super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContext = context;
        this.bookName = bookName;
        this.itemClick = itemClick;
        view = LayoutInflater.from(mContext).inflate(R.layout.mo_dialog_two, null);
        this.setContentView(view);

        initView();
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
    }

    private void initView() {
        TextView tvBookName = view.findViewById(R.id.tv_msg);
        tvBookName.setText(mContext.getString(R.string.check_add_bookshelf, bookName));

        //退出阅读
        TextView tvExit = view.findViewById(R.id.tv_cancel);
        tvExit.setText("不了");
        tvExit.setOnClickListener(v -> {
            dismiss();
            itemClick.clickExit();
        });

        //放入书架并退出阅读
        TextView tvAddShelf = view.findViewById(R.id.tv_done);
        tvAddShelf.setText("放入书架");
        tvAddShelf.setOnClickListener(v -> {
            itemClick.clickAddShelf();
            dismiss();
            itemClick.clickExit();
        });
    }

    public interface OnItemClickListener {
        void clickExit();

        void clickAddShelf();
    }
}
