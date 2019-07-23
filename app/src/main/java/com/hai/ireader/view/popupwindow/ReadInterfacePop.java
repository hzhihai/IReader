//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.hai.ireader.view.popupwindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hai.ireader.R;
import com.hai.ireader.base.BasePopFrame;
import com.hai.ireader.help.ReadBookControl;
import com.hai.ireader.help.permission.Permissions;
import com.hai.ireader.help.permission.PermissionsCompat;
import com.hai.ireader.utils.theme.ATH;
import com.hai.ireader.view.activity.ReadBookActivity;
import com.hai.ireader.view.activity.ReadStyleActivity;
import com.hai.ireader.widget.font.FontSelector;
import com.hai.ireader.widget.modialog.smartisan.OptionListDialog;
import com.hai.ireader.widget.modialog.smartisan.SmartisanDialog;
import com.hai.ireader.widget.modialog.smartisan.listener.OnOptionItemSelectListener;
import com.hai.ireader.widget.page.animation.PageAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

public class ReadInterfacePop extends BasePopFrame {
    @BindView(R.id.vw_bg)
    View vwBg;
    @BindView(R.id.fl_text_Bold)
    TextView flTextBold;
    @BindView(R.id.fl_text_font)
    TextView fl_text_font;
    @BindView(R.id.civ_bg_white)
    CircleImageView civBgWhite;
    @BindView(R.id.civ_bg_yellow)
    CircleImageView civBgYellow;
    @BindView(R.id.civ_bg_green)
    CircleImageView civBgGreen;
    @BindView(R.id.civ_bg_black)
    CircleImageView civBgBlack;
    @BindView(R.id.civ_bg_blue)
    CircleImageView civBgBlue;
    @BindView(R.id.tv0)
    TextView tv0;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tvPageMode)
    TextView tvPageMode;
    @BindView(R.id.fl_indent)
    TextView tvIndent;
    @BindView(R.id.tvFontsizeMinus)
    TextView tvFontsizeMinus;
    @BindView(R.id.txtFontSize)
    TextView txtFontSize;
    @BindView(R.id.tvFontsizePlus)
    TextView tvFontsizePlus;

    private ReadBookActivity activity;
    private ReadBookControl readBookControl = ReadBookControl.getInstance();
    private Callback callback;

    public ReadInterfacePop(Context context) {
        super(context);
        init(context);
    }

    public ReadInterfacePop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReadInterfacePop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_read_interface, this);
        ButterKnife.bind(this, view);
        vwBg.setOnClickListener(null);
    }

    public void setListener(ReadBookActivity readBookActivity, @NonNull Callback callback) {
        this.activity = readBookActivity;
        this.callback = callback;
        initData();
        bindEvent();
    }

    private int textSize;

    private void updateTextSize(String id){
        if (id==null||id==""){
            return;
        }
        switch (id){
            case "+":
                if (textSize<50)textSize+=2;
                break;
            case "-":
                if (textSize>10)textSize-=2;
                break;
        }
        txtFontSize.setText(Integer.toString(textSize));
        readBookControl.setTextSize(textSize);
        callback.upTextSize();
    }

    private void initData() {
        setBg();
        updateBg(readBookControl.getTextDrawableIndex());
        updateBoldText(readBookControl.getTextBold());
        updatePageMode(readBookControl.getPageMode());
        textSize = readBookControl.getTextSize();
        txtFontSize.setText(Integer.toString(textSize));
        tvFontsizeMinus.setOnClickListener((v) ->{
            updateTextSize("-");
        });
        tvFontsizePlus.setOnClickListener((v)->{
            updateTextSize("+");
        });
    }

    /**
     *显示单选列表框
     * @param setTite
     * @param array
     * @param select
     * @param name
     */
    @Override
    protected void showDialog(Context context, String setTite, String[] array,int select, String name) {
        if (name==null||name=="")
        {
            return;
        }
        OptionListDialog dialog = SmartisanDialog.createOptionListDialog(context);
        dialog.setTitle(setTite)
                .setOptionList(array) //设置列表数据
                .setLastOption(select)  //设置选中项
                .setItemGravity(Gravity.CENTER) //设置显示位置
                .setLastColor(context.getResources().getColor(R.color.md_pink_600)) //设置选中字体颜色
                .show();
        dialog.setOnOptionItemSelectListener(new OnOptionItemSelectListener() {
            @Override
            public void onSelect(int position, int option) {
                switch (name){
                    case "indent":  //屏幕超时设置
                        readBookControl.setIndent(option);
                        callback.refresh();
                        break;
                    case "pagemode":   //字体转换设置
                        readBookControl.setPageMode(option);
                        updatePageMode(option);
                        callback.upPageMode();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        ATH.setAlertDialogTint(dialog);
    }


    /**
     * 控件事件
     */
    private void bindEvent() {
        //缩进
        tvIndent.setOnClickListener(v -> {
            showDialog(activity, activity.getResources().getString(R.string.indent),activity.getResources().getStringArray(R.array.indent),readBookControl.getIndent(),"indent");
        });
        //翻页模式
        tvPageMode.setOnClickListener(view -> {
            showDialog(activity, activity.getResources().getString(R.string.page_mode),PageAnimation.Mode.getAllPageMode(),readBookControl.getPageMode(),"pagemode");
        });
        //加粗切换
        flTextBold.setOnClickListener(view -> {
            readBookControl.setTextBold(!readBookControl.getTextBold());
            updateBoldText(readBookControl.getTextBold());
            callback.refresh();
        });
        //背景选择
        civBgWhite.setOnClickListener(v -> {
            updateBg(0);
            callback.bgChange();
        });
        civBgYellow.setOnClickListener(v -> {
            updateBg(1);
            callback.bgChange();
        });
        civBgGreen.setOnClickListener(v -> {
            updateBg(2);
            callback.bgChange();
        });
        civBgBlue.setOnClickListener(v -> {
            updateBg(3);
            callback.bgChange();
        });
        civBgBlack.setOnClickListener(v -> {
            updateBg(4);
            callback.bgChange();
        });
        //自定义阅读样式
        civBgWhite.setOnLongClickListener(view -> customReadStyle(0));
        civBgYellow.setOnLongClickListener(view -> customReadStyle(1));
        civBgGreen.setOnLongClickListener(view -> customReadStyle(2));
        civBgBlue.setOnLongClickListener(view -> customReadStyle(3));
        civBgBlack.setOnLongClickListener(view -> customReadStyle(4));

        //选择字体
        fl_text_font.setOnClickListener(view -> {
            new PermissionsCompat.Builder(activity)
                    .addPermissions(Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE)
                    .rationale(R.string.get_storage_per)
                    .onGranted((requestCode) -> {
                        new FontSelector(activity, readBookControl.getFontPath())
                                .setListener(new FontSelector.OnThisListener() {
                                    @Override
                                    public void setDefault() {
                                        clearFontPath();
                                    }

                                    @Override
                                    public void setFontPath(String fontPath) {
                                        setReadFonts(fontPath);
                                    }
                                })
                                .create()
                                .show();
                        return Unit.INSTANCE;
                    })
                    .request();
        });

        //长按清除字体
        fl_text_font.setOnLongClickListener(view -> {
            clearFontPath();
            activity.toast(R.string.clear_font);
            return true;
        });

    }

    //自定义阅读样式
    private boolean customReadStyle(int index) {
        Intent intent = new Intent(activity, ReadStyleActivity.class);
        intent.putExtra("index", index);
        activity.startActivity(intent);
        return false;
    }

    //设置字体
    public void setReadFonts(String path) {
        readBookControl.setReadBookFont(path);
        callback.refresh();
    }

    //清除字体
    private void clearFontPath() {
        readBookControl.setReadBookFont(null);
        callback.refresh();
    }

    private void updatePageMode(int pageMode) {
        tvPageMode.setText(String.format(activity.getString(R.string.page_mode) + ":%s", PageAnimation.Mode.getPageMode(pageMode)));
    }

    private void updateBoldText(Boolean isBold) {
        flTextBold.setSelected(isBold);
    }

    public void setBg() {
        tv0.setTextColor(readBookControl.getTextColor(0));
        tv1.setTextColor(readBookControl.getTextColor(1));
        tv2.setTextColor(readBookControl.getTextColor(2));
        tv3.setTextColor(readBookControl.getTextColor(3));
        tv4.setTextColor(readBookControl.getTextColor(4));
        civBgWhite.setImageDrawable(readBookControl.getBgDrawable(0, activity, 100, 180));
        civBgYellow.setImageDrawable(readBookControl.getBgDrawable(1, activity, 100, 180));
        civBgGreen.setImageDrawable(readBookControl.getBgDrawable(2, activity, 100, 180));
        civBgBlue.setImageDrawable(readBookControl.getBgDrawable(3, activity, 100, 180));
        civBgBlack.setImageDrawable(readBookControl.getBgDrawable(4, activity, 100, 180));
    }

    private void updateBg(int index) {
        civBgWhite.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        civBgYellow.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        civBgGreen.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        civBgBlack.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        civBgBlue.setBorderColor(activity.getResources().getColor(R.color.tv_text_default));
        switch (index) {
            case 0:
                civBgWhite.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 1:
                civBgYellow.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 2:
                civBgGreen.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 3:
                civBgBlue.setBorderColor(Color.parseColor("#F3B63F"));
                break;
            case 4:
                civBgBlack.setBorderColor(Color.parseColor("#F3B63F"));
                break;
        }
        readBookControl.setTextDrawableIndex(index);
    }

    public interface Callback {
        void upPageMode();

        void upTextSize();

        void upMargin();

        void bgChange();

        void refresh();
    }

}