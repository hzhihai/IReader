package com.hai.ireader.service;

import android.annotation.SuppressLint;
import android.content.Context;

import com.hai.ireader.base.observer.MyObserver;
import com.hai.ireader.bean.BookSourceBean;
import com.hai.ireader.model.BookSourceManager;
import com.hai.ireader.utils.ACache;

import java.util.List;

import io.reactivex.Observable;

public class BookSourceService {

    public static final String sourceUrl = "https://hzhihai-1251162982.cos.ap-beijing.myqcloud.com/booksource/haobaimoren.txt";
    public static Observable<List<BookSourceBean>> observable;

    public static void InitBookSource(Context context){

        if (context==null){
            return;
        }
        String cacheUrl = ACache.get(context).getAsString("sourceUrl");
        if (cacheUrl == null) {
            observable = BookSourceManager.importSource(sourceUrl);
            ACache.get(context).put("sourceUrl", sourceUrl);
            if (observable != null)
            {
                observable.subscribe( new MyObserver<List<BookSourceBean>>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onNext(List<BookSourceBean> bookSourceBeans) {
                        if (BookSourceManager.getAllBookSource().size()>0&&bookSourceBeans.size()==BookSourceManager.getAllBookSource().size()){
                            return;
                        }
                    }
                });
            }
        }
    }
}
