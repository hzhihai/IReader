package com.hai.ireader.model.content;

import com.hai.ireader.ReaderApplication;
import com.hai.ireader.R;

public class VipThrowable extends Throwable {

    private final static String tag = "VIP_THROWABLE";

    VipThrowable() {
        super(ReaderApplication.getInstance().getString(R.string.donate_s));
    }
}
