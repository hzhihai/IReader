//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.hai.ireader.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.navigation.NavigationView;
import com.hai.ireader.model.BookSourceManager;
import com.hwangjr.rxbus.RxBus;
import com.hai.ireader.BuildConfig;
import com.hai.ireader.DbHelper;
import com.hai.ireader.ReaderApplication;
import com.hai.ireader.R;
import com.hai.ireader.base.BaseTabActivity;
import com.hai.ireader.constant.RxBusTag;
import com.hai.ireader.help.FileHelp;
import com.hai.ireader.help.ProcessTextHelp;
import com.hai.ireader.help.permission.Permissions;
import com.hai.ireader.help.permission.PermissionsCompat;
import com.hai.ireader.model.UpLastChapterModel;
import com.hai.ireader.presenter.MainPresenter;
import com.hai.ireader.presenter.contract.MainContract;
import com.hai.ireader.service.WebService;
import com.hai.ireader.utils.StringUtils;
import com.hai.ireader.utils.theme.ATH;
import com.hai.ireader.utils.theme.NavigationViewUtil;
import com.hai.ireader.utils.theme.ThemeStore;
import com.hai.ireader.view.fragment.BookListFragment;
import com.hai.ireader.view.fragment.FindBookFragment;
import com.hai.ireader.widget.modialog.InputDialog;
import com.hai.ireader.widget.modialog.MoDialogHUD;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;

import static com.hai.ireader.utils.NetworkUtils.isNetWorkAvailable;

public class MainActivity extends BaseTabActivity<MainContract.Presenter> implements MainContract.View, BookListFragment.CallbackValue {
    private static final int BACKUP_RESULT = 11;
    private static final int RESTORE_RESULT = 12;
    private static final int FILE_SELECT_RESULT = 13;
    private final int requestSource = 14;
    private String[] mTitles;

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_view)
    CoordinatorLayout mainView;

    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    private AppCompatImageView vwNightTheme;
    private int group;
    private boolean viewIsList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MoDialogHUD moDialogHUD;
    private long exitTime = 0;
    private boolean resumed = false;
    private Handler handler = new Handler();

    @Override
    protected MainContract.Presenter initInjector() {
        return new MainPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            resumed = savedInstanceState.getBoolean("resumed");
        }
        group = preferences.getInt("bookshelfGroup", 0);
        super.onCreate(savedInstanceState);

        //初始化书源库
        BookSourceManager.InitBookSource(this,"OnLine");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resumed", resumed);
    }

    @Override
    protected void onCreateActivity() {
        getWindow().getDecorView().setBackgroundColor(ThemeStore.backgroundColor(this));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        String shared_url = preferences.getString("shared_url", "");
        assert shared_url != null;
        if (shared_url.length() > 1) {
            InputDialog.builder(this)
                    .setTitle(getString(R.string.add_book_url))
                    .setDefaultValue(shared_url)
                    .setCallback(inputText -> {
                        inputText = StringUtils.trim(inputText);
                        mPresenter.addBookUrl(inputText);
                    }).show();
            preferences.edit()
                    .putString("shared_url", "")
                    .apply();
        }
    }

    /**
     * 沉浸状态栏
     */
    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
    }

    @Override
    protected void initData() {
        viewIsList = preferences.getBoolean("bookshelfIsList", true);
        mTitles = new String[]{getString(R.string.bookshelf), getString(R.string.find)};
    }

    @Override
    public boolean isRecreate() {
        return isRecreate;
    }

    @Override
    public int getGroup() {
        return group;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected List<Fragment> createTabFragments() {
        BookListFragment bookListFragment = null;
        FindBookFragment findBookFragment = null;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BookListFragment) {
                bookListFragment = (BookListFragment) fragment;
            } else if (fragment instanceof FindBookFragment) {
                findBookFragment = (FindBookFragment) fragment;
            }
        }
        if (bookListFragment == null)
            bookListFragment = new BookListFragment();
        if (findBookFragment == null)
            findBookFragment = new FindBookFragment();
        return Arrays.asList(bookListFragment, findBookFragment);
    }

    @Override
    protected List<String> createTabTitles() {
        return Arrays.asList(mTitles);
    }

    //设置Toolbar左上的返回箭头
    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void bindView() {
        super.bindView();
        setSupportActionBar(toolbar,0,false); //设置Toolbar
        setupActionBar();
        setActionBar();
        initDrawer();
        initViewPager();
        upGroup(group);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        moDialogHUD = new MoDialogHUD(this);
        /*if (!preferences.getBoolean("behaviorMain", true)) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(0);
        }*/
    }
    //初始化页面
    private void initViewPager(){
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem!=null){
                    menuItem.setChecked(true);
                }else {
                    navView.getMenu().getItem(0).setChecked(false);
                }
                menuItem=navView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public ViewPager getViewPager() {
        return mVp;
    }

    public BookListFragment getBookListFragment() {
        try {
            return (BookListFragment) mFragmentList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public FindBookFragment getFindFragment() {
        try {
            return (FindBookFragment) mFragmentList.get(1);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 这个必须要，没有的话进去的默认是个箭头。。正常应该是三横杠的
        //mDrawerToggle.syncState();
        if (vwNightTheme != null) {
            upThemeVw();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem pauseMenu = menu.findItem(R.id.action_list_grid);
        if (viewIsList) {
            pauseMenu.setTitle(R.string.action_grid);
        } else {
            pauseMenu.setTitle(R.string.action_list);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // 添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_local:
                new PermissionsCompat.Builder(this)
                        .addPermissions(Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE)
                        .rationale(R.string.import_per)
                        .onGranted((requestCode) -> {
                            startActivity(new Intent(MainActivity.this, ImportBookActivity.class));
                            return Unit.INSTANCE;
                        })
                        .request();
                break;
            case R.id.action_add_url:
                InputDialog.builder(this)
                        .setTitle(getString(R.string.add_book_url))
                        .setCallback(inputText -> {
                            inputText = StringUtils.trim(inputText);
                            mPresenter.addBookUrl(inputText);
                        }).show();
                break;
            case R.id.action_download_all:
                if (!isNetWorkAvailable())
                    toast(R.string.network_connection_unavailable);
                else
                    RxBus.get().post(RxBusTag.DOWNLOAD_ALL, 10000);
                break;
            case R.id.action_list_grid:
                editor.putBoolean("bookshelfIsList", !viewIsList).apply();
                recreate();
                break;
            case R.id.action_arrange_bookshelf:
                if (getBookListFragment() != null) {
                    getBookListFragment().setArrange(true);
                }
                break;
            case R.id.action_web_start:
                WebService.startThis(this);
                break;
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)
                ) {
                    drawer.closeDrawers();
                } else {
                    drawer.openDrawer(GravityCompat.START, !ReaderApplication.isEInkMode);
                }
                break;
            case R.id.action_search: //打开搜索界面
                startActivityByAnim(new Intent(this,SearchBookActivity.class),
                        toolbar,"sharedView", android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //设置ToolBar
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //初始化侧边栏
    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();
        drawer.addDrawerListener(mDrawerToggle);

        setUpNavigationView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    private void upGroup(int group) {
        if (this.group != group) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bookshelfGroup", group);
            editor.apply();
        }
        this.group = group;
        RxBus.get().post(RxBusTag.UPDATE_GROUP, group);
        RxBus.get().post(RxBusTag.REFRESH_BOOK_LIST, false);

    }

    /**
     * 侧边栏按钮
     */
    private void setUpNavigationView() {
        navigationView.setBackgroundColor(ThemeStore.backgroundColor(this));
        NavigationViewUtil.setItemTextColors(navigationView, getResources().getColor(R.color.tv_text_default), ThemeStore.accentColor(this));
        NavigationViewUtil.setItemIconColors(navigationView, getResources().getColor(R.color.tv_text_default), ThemeStore.accentColor(this));
        NavigationViewUtil.disableScrollbar(navigationView);
        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        AppCompatImageView imageView = headerView.findViewById(R.id.iv_read);
        imageView.setColorFilter(ThemeStore.accentColor(this));
        navigationView.addHeaderView(headerView);
        Menu drawerMenu = navigationView.getMenu();
        vwNightTheme = drawerMenu.findItem(R.id.action_theme).getActionView().findViewById(R.id.iv_theme_day_night);
        upThemeVw();
        vwNightTheme.setOnClickListener(view -> setNightTheme(!isNightTheme()));
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawer.closeDrawer(GravityCompat.START, !ReaderApplication.isEInkMode);
            switch (menuItem.getItemId()) {
                case R.id.action_book_source_manage:
                    handler.postDelayed(() -> BookSourceActivity.startThis(this, requestSource), 200);
                    break;
                case R.id.action_replace_rule:
                    handler.postDelayed(() -> ReplaceRuleActivity.startThis(this, null), 200);
                    break;
                case R.id.action_download:
                    handler.postDelayed(() -> DownloadActivity.startThis(this), 200);
                    break;
                case R.id.action_setting:
                    handler.postDelayed(() -> SettingActivity.startThis(this), 200);
                    break;
                case R.id.action_about:
                    handler.postDelayed(() -> AboutActivity.startThis(this), 200);
                    break;
                case R.id.action_donate:
                    handler.postDelayed(() -> DonateActivity.startThis(this), 200);
                    break;
                case R.id.action_backup:
                    handler.postDelayed(this::backup, 200);
                    break;
                case R.id.action_restore:
                    handler.postDelayed(this::restore, 200);
                    break;
                case R.id.action_theme:
                    handler.postDelayed(() -> ThemeSettingActivity.startThis(this), 200);
                    break;
            }
            return true;
        });
    }

    /**
     * 更新主题切换按钮
     */
    private void upThemeVw() {
        if (isNightTheme()) {
            vwNightTheme.setImageResource(R.drawable.ic_daytime);
            vwNightTheme.setContentDescription(getString(R.string.click_to_day));
        } else {
            vwNightTheme.setImageResource(R.drawable.ic_brightness);
            vwNightTheme.setContentDescription(getString(R.string.click_to_night));
        }
        vwNightTheme.getDrawable().mutate().setColorFilter(ThemeStore.accentColor(this), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * 备份
     */
    private void backup() {
        new PermissionsCompat.Builder(this)
                .addPermissions(Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE)
                .rationale(R.string.backup_permission)
                .onGranted((requestCode) -> {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.backup_confirmation)
                            .setMessage(R.string.backup_message)
                            .setPositiveButton(R.string.ok, (dialog, which) -> mPresenter.backupData())
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    ATH.setAlertDialogTint(alertDialog);
                    return Unit.INSTANCE;
                }).request();
    }

    /**
     * 恢复
     */
    private void restore() {
        new PermissionsCompat.Builder(this)
                .addPermissions(Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE)
                .rationale(R.string.restore_permission)
                .onGranted((requestCode) -> {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.restore_confirmation)
                            .setMessage(R.string.restore_message)
                            .setPositiveButton(R.string.ok, (dialog, which) -> mPresenter.restoreData())
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    ATH.setAlertDialogTint(alertDialog);
                    return Unit.INSTANCE;
                }).request();
    }

    /**
     * 新版本运行
     */
    private void versionUpRun() {
        if (preferences.getInt("versionCode", 0) != ReaderApplication.getVersionCode()) {
            //保存版本号
            preferences.edit()
                    .putInt("versionCode", ReaderApplication.getVersionCode())
                    .apply();
            //更新日志
            moDialogHUD.showAssetMarkdown("updateLog.md");
        }
    }

    @Override
    protected void firstRequest() {
        if (!isRecreate) {
            versionUpRun();
        }
        if (!Objects.equals(ReaderApplication.downloadPath, FileHelp.getFilesPath())) {
            new PermissionsCompat.Builder(this)
                    .addPermissions(Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE)
                    .rationale(R.string.get_storage_per)
                    .request();
        }
        handler.postDelayed(() -> {
            UpLastChapterModel.getInstance().startUpdate();
            if (BuildConfig.DEBUG) {
                ProcessTextHelp.setProcessTextEnable(false);
            }
        }, 60 * 1000);
    }

    @Override
    public void dismissHUD() {
        moDialogHUD.dismiss();
    }

    public void onRestore(String msg) {
        moDialogHUD.showLoading(msg);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean mo = moDialogHUD.onKeyDown(keyCode, event);
        if (mo) {
            return true;
        } /*else if (mTlIndicator.getSelectedTabPosition() != 0) {
            Objects.requireNonNull(mTlIndicator.getTabAt(0)).select();
            return true;
        }*/
        else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START, !ReaderApplication.isEInkMode);
                    return true;
                }
                exit();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 退出
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, getString(R.string.double_click_exit), Toast.LENGTH_SHORT)
                    .show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    protected void onDestroy() {
        UpLastChapterModel.destroy();
        DbHelper.getDaoSession().getBookContentBeanDao().deleteAll();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestSource) {
                FindBookFragment findBookFragment = getFindFragment();
                if (findBookFragment != null) {
                    findBookFragment.refreshData();
                }
            }
        }
    }


    /**
     * 设置底部导航栏选中页面
     * @param index
     */
    private void setBottomSelecteItem(int index) {
        ViewPager pager = mVp;

        if (pager.getCurrentItem() == index) {
            return;
        } else{
            pager.setCurrentItem(index);
        }

    }

    /**
     * 设置底部导航栏
     */
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_bookshelf:{
                    setBottomSelecteItem(0);
                    return true;
                }
                case R.id.navigation_find:{
                    setBottomSelecteItem(1);
                    return true;
                }
                case R.id.navigation_notifications:{
                    return true;
                }
            }

            return false;
        }
    };

}
