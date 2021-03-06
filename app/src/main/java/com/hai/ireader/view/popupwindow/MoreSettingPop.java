//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.hai.ireader.view.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hai.ireader.base.BasePopFrame;
import com.hai.ireader.widget.modialog.smartisan.OptionListDialog;
import com.hai.ireader.widget.modialog.smartisan.SmartisanDialog;
import com.hai.ireader.widget.modialog.smartisan.listener.OnOptionItemSelectListener;
import com.hwangjr.rxbus.RxBus;
import com.hai.ireader.R;
import com.hai.ireader.constant.RxBusTag;
import com.hai.ireader.help.ReadBookControl;
import com.hai.ireader.utils.theme.ATH;
import com.hai.ireader.widget.views.ATESwitch;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoreSettingPop extends BasePopFrame {
    @BindView(R.id.vw_bg)
    View vwBg;
    @BindView(R.id.sb_click_all_next)
    Switch sbClickAllNext;
    @BindView(R.id.sb_click)
    Switch sbClick;
    @BindView(R.id.sb_show_title)
    Switch sbShowTitle;
    @BindView(R.id.sb_showTimeBattery)
    Switch sbShowTimeBattery;
    @BindView(R.id.sb_hideStatusBar)
    Switch sbHideStatusBar;
    @BindView(R.id.ll_hideStatusBar)
    LinearLayout llHideStatusBar;
    @BindView(R.id.ll_showTimeBattery)
    LinearLayout llShowTimeBattery;
    @BindView(R.id.sb_hideNavigationBar)
    Switch sbHideNavigationBar;
    @BindView(R.id.ll_hideNavigationBar)
    LinearLayout llHideNavigationBar;
    @BindView(R.id.sb_showLine)
    Switch sbShowLine;
    @BindView(R.id.llScreenTimeOut)
    LinearLayout llScreenTimeOut;
    @BindView(R.id.tv_screen_time_out)
    TextView tvScreenTimeOut;
    @BindView(R.id.tvJFConvert)
    TextView tvJFConvert;
    @BindView(R.id.llJFConvert)
    LinearLayout llJFConvert;
    @BindView(R.id.tv_screen_direction)
    TextView tvScreenDirection;
    @BindView(R.id.ll_screen_direction)
    LinearLayout llScreenDirection;
    @BindView(R.id.sw_volume_next_page)
    Switch swVolumeNextPage;
    @BindView(R.id.sw_read_aloud_key)
    Switch swReadAloudKey;
    @BindView(R.id.ll_read_aloud_key)
    LinearLayout llReadAloudKey;
    @BindView(R.id.ll_click_all_next)
    LinearLayout llClickAllNext;
    @BindView(R.id.reNavBarColor)
    TextView reNavBarColor;
    @BindView(R.id.reNavBarColor_val)
    TextView reNavBarColorVal;
    @BindView(R.id.llNavigationBarColor)
    LinearLayout llNavigationBarColor;
    @BindView(R.id.sbImmersionStatusBar)
    ATESwitch sbImmersionStatusBar;
    @BindView(R.id.llImmersionStatusBar)
    LinearLayout llImmersionStatusBar;
    @BindView(R.id.sb_select_text)
    Switch switchSelectText;

    private Context context;
    private ReadBookControl readBookControl = ReadBookControl.getInstance();
    private Callback callback;

    public MoreSettingPop(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public MoreSettingPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public MoreSettingPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_more_setting, this);
        ButterKnife.bind(this, view);
        vwBg.setOnClickListener(null);
    }

    public void setListener(@NonNull Callback callback) {
        this.callback = callback;
        initData();
        bindEvent();
    }

    private void bindEvent() {
        this.setOnClickListener(view -> this.setVisibility(GONE));
        sbImmersionStatusBar.setOnCheckedChangeListener(((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                readBookControl.setImmersionStatusBar(b);
                callback.upBar();
                RxBus.get().post(RxBusTag.RECREATE, true);
            }
        }));
        sbHideStatusBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setHideStatusBar(isChecked);
                callback.recreate();
            }
        });
        sbHideNavigationBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setHideNavigationBar(isChecked);
                initData();
                callback.recreate();
            }
        });
        swVolumeNextPage.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                readBookControl.setCanKeyTurn(b);
                upView();
            }
        });
        swReadAloudKey.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                readBookControl.setAloudCanKeyTurn(b);
            }
        });
        sbClick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setCanClickTurn(isChecked);
                upView();
            }
        });
        sbClickAllNext.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setClickAllNext(isChecked);
            }
        });

        sbShowTitle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setShowTitle(isChecked);
                callback.refreshPage();
            }
        });
        sbShowTimeBattery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setShowTimeBattery(isChecked);
                callback.refreshPage();
            }
        });
        sbShowLine.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setShowLine(isChecked);
                callback.refreshPage();
            }
        });
        llScreenTimeOut.setOnClickListener(view -> {
            showDialog(context,context.getResources().getString(R.string.keep_light),
                    context.getResources().getStringArray(R.array.screen_time_out),
                    readBookControl.getScreenTimeOut(),
                    "keeplight");
        });
        llJFConvert.setOnClickListener(view -> {
            showDialog(context,context.getResources().getString(R.string.jf_convert),
                    context.getResources().getStringArray(R.array.convert_s),
                    readBookControl.getTextConvert(),
                    "convert");
        });
        llScreenDirection.setOnClickListener(view -> {
            showDialog(context,context.getResources().getString(R.string.screen_direction),
                    context.getResources().getStringArray(R.array.screen_direction_list_title),
                    readBookControl.getScreenDirection(),
                    "screen");
        });
        llNavigationBarColor.setOnClickListener(view -> {
            showDialog(context,context.getResources().getString(R.string.re_navigation_bar_color),
                    context.getResources().getStringArray(R.array.NavBarColors),
                    readBookControl.getNavBarColor(),
                    "navbarcolor");
        });
        switchSelectText.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setCanSelectText(isChecked);
            }
        });
    }

    /**
     *显示单选列表框
     * @param tite
     * @param array
     * @param select
     * @param id
     */
    @Override
    protected void showDialog(Context context,String tite,String[] array,int select,String id) {
        if (id==null||id=="")
        {
            return;
        }
        OptionListDialog dialog = SmartisanDialog.createOptionListDialog(context);
        dialog.setTitle(tite)
                .setOptionList(array) //设置列表数据
                .setLastOption(select)  //设置选中项
                .setItemGravity(Gravity.CENTER) //设置显示位置
                .setLastColor(getResources().getColor(R.color.md_pink_600)) //设置选中字体颜色
                .show();
        dialog.setOnOptionItemSelectListener(new OnOptionItemSelectListener() {
            @Override
            public void onSelect(int position, int option) {
                switch (id){
                    case "keeplight":  //屏幕超时设置
                        readBookControl.setScreenTimeOut(option);
                        upScreenTimeOut(option);
                        callback.keepScreenOnChange(option);
                        break;
                    case "convert":   //字体转换设置
                        readBookControl.setTextConvert(option);
                        upFConvert(option);
                        callback.refreshPage();
                        break;
                    case "screen":   //屏幕方向设置
                        readBookControl.setScreenDirection(option);
                        upScreenDirection(option);
                        callback.recreate();
                        break;
                    case "navbarcolor":  //导航栏背景色设置
                        readBookControl.setNavBarColor(option);
                        upNavBarColor(option);
                        callback.recreate();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        ATH.setAlertDialogTint(dialog);
    }

    private void initData() {
        upScreenDirection(readBookControl.getScreenDirection());
        upScreenTimeOut(readBookControl.getScreenTimeOut());
        upFConvert(readBookControl.getTextConvert());
        upNavBarColor(readBookControl.getNavBarColor());
        sbImmersionStatusBar.setChecked(readBookControl.getImmersionStatusBar());
        swVolumeNextPage.setChecked(readBookControl.getCanKeyTurn());
        swReadAloudKey.setChecked(readBookControl.getAloudCanKeyTurn());
        sbHideStatusBar.setChecked(readBookControl.getHideStatusBar());
        sbHideNavigationBar.setChecked(readBookControl.getHideNavigationBar());
        sbClick.setChecked(readBookControl.getCanClickTurn());
        sbClickAllNext.setChecked(readBookControl.getClickAllNext());
        sbShowTitle.setChecked(readBookControl.getShowTitle());
        sbShowTimeBattery.setChecked(readBookControl.getShowTimeBattery());
        sbShowLine.setChecked(readBookControl.getShowLine());
        switchSelectText.setChecked(readBookControl.isCanSelectText());
        upView();
    }

    private void upView() {
        if (readBookControl.getHideStatusBar()) {
            sbShowTimeBattery.setEnabled(true);
        } else {
            sbShowTimeBattery.setEnabled(false);
        }
        if (readBookControl.getCanKeyTurn()) {
            swReadAloudKey.setEnabled(true);
        } else {
            swReadAloudKey.setEnabled(false);
        }
        if (readBookControl.getCanClickTurn()) {
            sbClickAllNext.setEnabled(true);
        } else {
            sbClickAllNext.setEnabled(false);
        }
        if (readBookControl.getHideNavigationBar()) {
            llNavigationBarColor.setEnabled(false);
            reNavBarColorVal.setEnabled(false);
        } else {
            llNavigationBarColor.setEnabled(true);
            reNavBarColorVal.setEnabled(true);
        }
    }

    private void upScreenTimeOut(int screenTimeOut) {
        tvScreenTimeOut.setText(context.getResources().getStringArray(R.array.screen_time_out)[screenTimeOut]);
    }

    private void upFConvert(int fConvert) {
        tvJFConvert.setText(context.getResources().getStringArray(R.array.convert_s)[fConvert]);
    }

    private void upScreenDirection(int screenDirection) {
        String[] screenDirectionListTitle = context.getResources().getStringArray(R.array.screen_direction_list_title);
        if (screenDirection >= screenDirectionListTitle.length) {
            tvScreenDirection.setText(screenDirectionListTitle[0]);
        } else {
            tvScreenDirection.setText(screenDirectionListTitle[screenDirection]);
        }
    }

    private void upNavBarColor(int nColor) {
        reNavBarColorVal.setText(context.getResources().getStringArray(R.array.NavBarColors)[nColor]);
    }

    public interface Callback {
        void upBar();

        void keepScreenOnChange(int keepScreenOn);

        void recreate();

        void refreshPage();
    }

}
