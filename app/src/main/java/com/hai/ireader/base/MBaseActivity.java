//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.hai.ireader.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.hwangjr.rxbus.RxBus;
import com.hai.basemvplib.BaseActivity;
import com.hai.basemvplib.impl.IPresenter;
import com.hai.ireader.ReaderApplication;
import com.hai.ireader.R;
import com.hai.ireader.constant.RxBusTag;
import com.hai.ireader.utils.ColorUtil;
import com.hai.ireader.utils.SoftInputUtil;
import com.hai.ireader.utils.bar.ImmersionBar;
import com.hai.ireader.utils.theme.MaterialValueHelper;
import com.hai.ireader.utils.theme.ThemeStore;

import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class MBaseActivity<T extends IPresenter> extends BaseActivity<T> {
    private static final String TAG = MBaseActivity.class.getSimpleName();
    public final SharedPreferences preferences = ReaderApplication.getConfigPreferences();
    protected ImmersionBar mImmersionBar;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
        mImmersionBar = ImmersionBar.with(this);
        initImmersionBar();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
        ImmersionBar.with(this).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    protected void setSupportActionBar(@Nullable androidx.appcompat.widget.Toolbar toolbar, int color,Boolean flag){
        if (toolbar != null) {
            toolbar.setBackgroundColor((flag==true)? color:ThemeStore.primaryColor(this));
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public void setSupportActionBar(@Nullable androidx.appcompat.widget.Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    /**
     * 设置MENU图标颜色
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int primaryTextColor = MaterialValueHelper.getPrimaryTextColor(this, ColorUtil.isColorLight(ThemeStore.primaryColor(this)));
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(primaryTextColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("PrivateApi")
    @SuppressWarnings("unchecked")
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            //展开菜单显示图标
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                    method = menu.getClass().getDeclaredMethod("getNonActionItems");
                    ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) method.invoke(menu);
                    if (!menuItems.isEmpty()) {
                        for (MenuItem menuItem : menuItems) {
                            Drawable drawable = menuItem.getIcon();
                            if (drawable != null) {
                                drawable.mutate();
                                drawable.setColorFilter(getResources().getColor(R.color.tv_text_default), PorterDuff.Mode.SRC_ATOP);
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }

        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 沉浸状态栏
     */
    protected void initImmersionBar() {
        try {
            View actionBar = findViewById(R.id.action_bar);
            if (isImmersionBarEnabled()) {
                if (getSupportActionBar() != null && actionBar != null && actionBar.getVisibility() == View.VISIBLE) {
                    mImmersionBar.statusBarColorInt(ThemeStore.primaryColor(this));
                } else {
                    mImmersionBar.transparentStatusBar();
                }
            } else {
                if (getSupportActionBar() != null && actionBar != null && actionBar.getVisibility() == View.VISIBLE) {
                    mImmersionBar.statusBarColorInt(ThemeStore.statusBarColor(this));
                } else {
                    mImmersionBar.statusBarColor(R.color.status_bar_bag);
                }
            }
        } catch (Exception ignored) {
        }
        try {
            if (isImmersionBarEnabled() && ColorUtil.isColorLight(ThemeStore.primaryColor(this))) {
                mImmersionBar.statusBarDarkFont(true, 0.2f);
            } else if (ColorUtil.isColorLight(ThemeStore.primaryColorDark(this))) {
                mImmersionBar.statusBarDarkFont(true, 0.2f);
            } else {
                mImmersionBar.statusBarDarkFont(false);
            }
            if (!preferences.getBoolean("navigationBarColorChange", false)) {
                mImmersionBar.navigationBarColor(R.color.black);
                mImmersionBar.navigationBarDarkFont(false);
            } else if (ImmersionBar.canNavigationBarDarkFont()) {
                mImmersionBar.navigationBarColorInt(ThemeStore.primaryColorDark(this));
                if (ColorUtil.isColorLight(ThemeStore.primaryColor(this))) {
                    mImmersionBar.navigationBarDarkFont(true);
                } else {
                    mImmersionBar.navigationBarDarkFont(false);
                }
            }
            mImmersionBar.init();
        } catch (Exception ignored) {
        }
    }

    /**
     * @return 是否沉浸
     */
    protected boolean isImmersionBarEnabled() {
        return preferences.getBoolean("immersionStatusBar", false);
    }

    /**
     * 设置屏幕方向
     */
    public void setOrientation(int screenDirection) {
        switch (screenDirection) {
            case 0:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            case 1:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case 2:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case 3:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;
        }
    }

    /**
     * @return 是否夜间模式
     */
    public boolean isNightTheme() {
        return ReaderApplication.getInstance().isNightTheme();
    }

    protected void setNightTheme(boolean isNightTheme) {
        preferences.edit()
                .putBoolean("nightTheme", isNightTheme)
                .apply();
        ReaderApplication.getInstance().initNightTheme();
        ReaderApplication.getInstance().upThemeStore();
        RxBus.get().post(RxBusTag.RECREATE, true);
    }

    protected void initTheme() {
        if (ColorUtil.isColorLight(ThemeStore.primaryColor(this))) {
            setTheme(R.style.CAppTheme);
        } else {
            setTheme(R.style.CAppThemeBarDark);
        }
    }

    public void showSnackBar(View view, String msg) {
        showSnackBar(view, msg, Snackbar.LENGTH_SHORT);
    }

    public void showSnackBar(View view, String msg, int length) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, msg, length);
        } else {
            snackbar.setText(msg);
            snackbar.setDuration(length);
        }
        snackbar.show();
    }

    public void hideSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (ReaderApplication.isEInkMode) {
            overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (ReaderApplication.isEInkMode) {
            overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
        }
    }

    @Override
    public void finish() {
        SoftInputUtil.hideIMM(getCurrentFocus());
        super.finish();
        if (ReaderApplication.isEInkMode) {
            overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
        }
    }
}
