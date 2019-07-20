package com.hai.ireader.presenter.contract;

import android.content.SharedPreferences;

import com.hai.basemvplib.impl.IPresenter;
import com.hai.basemvplib.impl.IView;
import com.hai.ireader.bean.BookShelfBean;

import java.util.List;

public interface BookListContract {

    interface View extends IView {

        /**
         * 刷新书架书籍小说信息 更新UI
         *
         * @param bookShelfBeanList 书架
         */
        void refreshBookShelf(List<BookShelfBean> bookShelfBeanList);

        void refreshBook(String noteUrl);

        /**
         * 刷新错误
         *
         * @param error 错误
         */
        void refreshError(String error);

        SharedPreferences getPreferences();

        /**
         * 更新Group
         */
        void updateGroup(Integer group);

    }

    interface Presenter extends IPresenter {
        void queryBookShelf(Boolean needRefresh, int group);
    }

}
