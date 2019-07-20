package com.hai.ireader.presenter.contract;

import com.hai.basemvplib.impl.IPresenter;
import com.hai.basemvplib.impl.IView;

import java.io.File;
import java.util.List;

public interface ImportBookContract {

    interface Presenter extends IPresenter {

        void importBooks(List<File> books);

    }

    interface View extends IView {

        /**
         * 添加成功
         */
        void addSuccess();

        /**
         * 添加失败
         */
        void addError(String msg);
    }
}
