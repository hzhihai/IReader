package com.hai.ireader.widget.modialog.smartisan;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hai.ireader.R;
import com.hai.ireader.widget.modialog.smartisan.adapter.OptionListAdapter;
import com.hai.ireader.widget.modialog.smartisan.listener.OnOptionItemSelectListener;

import java.util.List;

/**
 * =======================================================<br>
 * Author: liying - liruoer2008@yeah.net <br>
 * Date: 2017/5/13 16:15 <br>
 * Version: 1.0  <br>
 * Description: TwoOptionsDialog <br>
 * Remark:   <br>
 * =======================================================
 */
public class OptionListDialog extends AlertDialog {
    private Context mContext;
    private TextView mTvTitle;
    private TextView mTvCancel;
    private RecyclerView mRvOptions;
    private OptionListAdapter mOptionListAdapter;

    private String mTitle = "";
    private String mCancel = "";
    private CharSequence[] mOptions;
    /** The last selected option */
    private int mLastSelect;
    @ColorInt
    private int mLastColor = 0xffea8444;
    private int mItemGravity = Gravity.LEFT;

    OptionListDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ddh_sm_dialog_option_list);
        findViews();
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.ddh_sm_BottomDialogStyle);
    }

    private void findViews() {
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvCancel = (TextView) findViewById(R.id.tvCancel);
        mRvOptions = (RecyclerView) findViewById(R.id.rvOptions);
        mRvOptions.setLayoutManager(new LinearLayoutManager(mContext));
    }

    /**
     * Set the dialog title
     *
     * @param title dialog title
     * @return This dialog instance
     */
    public OptionListDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    /**
     * Set cancel button text
     *
     * @param cancel cancel buttton text
     * @return This dialog instance
     */
    public OptionListDialog setCancelButtonText(String cancel) {
        this.mCancel = cancel;
        return this;
    }

    /**
     * Set the list of options
     *
     * @param optionList the list of options
     * @return This dialog instance
     */
    public OptionListDialog setOptionList(List<? extends CharSequence> optionList) {
        int size = optionList.size();
        mOptions = new CharSequence[size];
        for (int i = 0; i < size; i++) {
            mOptions[i] = optionList.get(i);
        }
        return this;
    }

    /**
     * Set the list of options
     *
     * @param optionList options array
     * @return This dialog instance
     */
    public OptionListDialog setOptionList(CharSequence[] optionList) {
        this.mOptions = optionList;
        return this;
    }

    /**
     * Set the last selected option
     *
     * @param lastOption the last selected option
     * @return This dialog instance
     */
    public OptionListDialog setLastOption(int lastOption) {
        this.mLastSelect = lastOption;
        return this;
    }

    /**
     * Set the color of the last selected option
     *
     * @param color The color of the last selected option
     * @return This dialog instance
     */
    public OptionListDialog setLastColor(@ColorInt int color) {
        this.mLastColor = color;
        return this;
    }

    /**
     * Set the gravity of every item
     *
     * @param gravity The gravity of every item
     * @return This dialog instance
     */
    public OptionListDialog setItemGravity(int gravity) {
        this.mItemGravity = gravity;
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mCancel)) {
            mTvCancel.setText(mCancel);
        }

        mOptionListAdapter = new OptionListAdapter(mOptions, mLastSelect, mLastColor);
        mOptionListAdapter.setItemGravity(mItemGravity);
        mRvOptions.setAdapter(mOptionListAdapter);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Set the listener <br>
     * This method must be called after the show() method <br>
     *
     * @param listener
     */
    public void setOnOptionItemSelectListener(OnOptionItemSelectListener listener) {
        mOptionListAdapter.setOnOptionItemSelectListener(listener);
    }
}
