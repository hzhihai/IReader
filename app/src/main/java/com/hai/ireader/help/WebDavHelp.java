package com.hai.ireader.help;

import com.hai.ireader.ReaderApplication;
import com.hai.ireader.utils.StringUtils;
import com.hai.ireader.utils.web_dav.http.HttpAuth;

import static com.hai.ireader.constant.AppConstant.DEFAULT_WEB_DAV_URL;

public class WebDavHelp {

    public static String getWebDavUrl() {
        String url = ReaderApplication.getConfigPreferences().getString("web_dav_url", DEFAULT_WEB_DAV_URL);
        assert url != null;
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        return url;
    }

    public static boolean initWebDav() {
        String account = ReaderApplication.getConfigPreferences().getString("web_dav_account", "");
        String password = ReaderApplication.getConfigPreferences().getString("web_dav_password", "");
        if (!StringUtils.isTrimEmpty(account) && !StringUtils.isTrimEmpty(password)) {
            HttpAuth.setAuth(account, password);
            return true;
        }
        return false;
    }

    private WebDavHelp() {

    }
}
