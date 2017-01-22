package lin.rong.scrolltocollapsecomponent;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by dell on 2017/1/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashReport.initCrashReport(getApplicationContext(), "ec5a016559", true);
    }

}
