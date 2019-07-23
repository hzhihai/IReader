package com.hai.ireader.widget.modialog.smartisan;


import android.content.Context;

/**
 * =======================================================<br>
 * Author: liying - liruoer2008@yeah.net <br>
 * Date: 2017/5/13 14:02 <br>
 * Version: 1.0  <br>
 * Description: SmartisanDialog <br>
 * Remark:   <br>
 * =======================================================
 */
public class SmartisanDialog {
    /**
     * Create an Options List dialog
     *
     * @param context
     * @return
     */
    public static OptionListDialog createOptionListDialog(Context context) {
        return new OptionListDialog(context);
    }
}
