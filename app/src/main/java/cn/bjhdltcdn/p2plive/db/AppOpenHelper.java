package cn.bjhdltcdn.p2plive.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import cn.bjhdltcdn.p2plive.greendao.BaseUserDao;
import cn.bjhdltcdn.p2plive.greendao.DaoMaster;

/**
 * Created by xiawenquan on 18/4/10.
 */

public class AppOpenHelper extends DaoMaster.OpenHelper {

    public AppOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面
        MigrationHelper.getInstance().migrate(db,BaseUserDao.class);
    }

}
