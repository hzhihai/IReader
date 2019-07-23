package com.hai.ireader.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class BasePopFrame extends FrameLayout {

    public BasePopFrame(Context context){
        super(context);
    }

    public BasePopFrame(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public BasePopFrame(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    /**
     * 显示列表框
     * @param context
     * @param setTite
     * @param array
     * @param select
     * @param name
     */
    protected void showDialog(Context context, String tite, String[] array,int select, String name){

    }
}
