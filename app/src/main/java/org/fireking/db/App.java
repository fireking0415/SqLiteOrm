package org.fireking.db;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.fireking.db.db.AppOrmHelper;
import org.fireking.db.db.AppSqLiteOpenHelper;

/**
 * Created by 刚 on 2016/8/30.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppSqLiteOpenHelper.init(this);
        AppOrmHelper.getInstance().registerSqLiteDatabase(AppSqLiteOpenHelper.getInstance());

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
