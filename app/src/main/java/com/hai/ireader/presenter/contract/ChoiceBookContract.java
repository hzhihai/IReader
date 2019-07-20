package com.hai.ireader.presenter.contract;

import com.hai.basemvplib.impl.IPresenter;
import com.hai.basemvplib.impl.IView;
import com.hai.ireader.bean.SearchBookBean;

import java.util.List;

public interface ChoiceBookContract {
    interface Presenter extends IPresenter {

        int getPage();

        void initPage();

        void toSearchBooks(String key);

        String getTitle();
    }

    interface View extends IView {

        void refreshSearchBook(List<SearchBookBean> books);

        void loadMoreSearchBook(List<SearchBookBean> books);

        void refreshFinish(Boolean isAll);

        void loadMoreFinish(Boolean isAll);

        void searchBookError(String msg);

        void addBookShelfFailed(String massage);

        void startRefreshAnim();
    }


}
