package com.hai.ireader.presenter.contract;

import com.hai.basemvplib.impl.IPresenter;
import com.hai.basemvplib.impl.IView;
import com.hai.ireader.widget.recycler.expandable.bean.RecyclerViewData;

import java.util.List;

public interface FindBookContract {
    interface Presenter extends IPresenter {

        void initData();

    }

    interface View extends IView {

        void upData(List<RecyclerViewData> group);

    }
}
