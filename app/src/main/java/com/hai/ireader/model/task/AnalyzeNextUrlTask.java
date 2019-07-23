package com.hai.ireader.model.task;

import com.hai.ireader.base.BaseModelImpl;
import com.hai.ireader.base.observer.MyObserver;
import com.hai.ireader.bean.BookChapterBean;
import com.hai.ireader.bean.BookShelfBean;
import com.hai.ireader.bean.WebChapterBean;
import com.hai.ireader.model.analyzeRule.AnalyzeUrl;
import com.hai.ireader.model.content.BookChapterList;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnalyzeNextUrlTask {
    private WebChapterBean webChapterBean;
    private Callback callback;
    private BookShelfBean bookShelfBean;
    private Map<String, String> headerMap;
    private BookChapterList bookChapterList;

    public AnalyzeNextUrlTask(BookChapterList bookChapterList, WebChapterBean webChapterBean, BookShelfBean bookShelfBean, Map<String, String> headerMap) {
        this.bookChapterList = bookChapterList;
        this.webChapterBean = webChapterBean;
        this.bookShelfBean = bookShelfBean;
        this.headerMap = headerMap;
    }

    public AnalyzeNextUrlTask setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public void analyzeUrl(AnalyzeUrl analyzeUrl) {
        BaseModelImpl.getInstance().getResponseO(analyzeUrl)
                .flatMap(stringResponse ->
                        bookChapterList.analyzeChapterList(stringResponse.body(), bookShelfBean, headerMap))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new MyObserver<List<BookChapterBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        callback.addDisposable(d);
                    }

                    @Override
                    public void onNext(List<BookChapterBean> bookChapterBeans) {
                        callback.analyzeFinish(webChapterBean, bookChapterBeans);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onError(throwable);
                    }
                });
    }

    public interface Callback {
        void addDisposable(Disposable disposable);

        void analyzeFinish(WebChapterBean bean, List<BookChapterBean> bookChapterBeans);

        void onError(Throwable throwable);
    }
}
