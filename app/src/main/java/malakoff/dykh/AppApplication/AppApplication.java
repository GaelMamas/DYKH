package malakoff.dykh.AppApplication;

import android.app.Application;
import android.content.Context;

import malakoff.dykh.User.User;
import malakoff.dykh.Utils.PreferencesManager;

/**
 * Created by user on 21/06/2016.
 */

public class AppApplication extends Application {

    private static User userInfo;
    private static Context appContext;

    public static User getUserInfo() {
        if (userInfo == null) {
            userInfo = new User();
            userInfo.setUserId(PreferencesManager.getString(Constants.USER_ID, "Admin"));
            userInfo.setHistorian(PreferencesManager.getBool(Constants.HISTORIAN, false));
        }
        return userInfo;
    }

    public static void setUserInfo(User userInfo) {
        if (userInfo == null) return;

        PreferencesManager.setString(Constants.USER_ID, userInfo.getUserId());
        PreferencesManager.setBool(Constants.HISTORIAN, userInfo.isHistorian());
        AppApplication.userInfo = userInfo;
    }

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
    }
}
