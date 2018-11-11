package cn.bjhdltcdn.p2plive.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.greendao.DaoMaster;
import cn.bjhdltcdn.p2plive.greendao.DaoSession;

/**
 * Created by xiawenquan on 17/12/19.
 * 创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级
 */

public class DaoManager {

    private static final String DB_NAME = "you_ban_app_db";

    private Context context;

    //多线程中要被共享的使用volatile关键字修饰
    private volatile static DaoManager manager;
    private static DaoMaster sDaoMaster;
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoSession sDaoSession;

    /**
     * 单例模式获得操作数据库对象
     * @return
     */
    public static DaoManager getInstance(){
        if (manager == null) {
            manager = new DaoManager();
        }
        return manager;
    }

    public void init(Context context){
        this.context = context;
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     * @return
     */
    public DaoMaster getDaoMaster(){
        if(sDaoMaster == null) {
//            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
//            sDaoMaster = new DaoMaster(helper.getWritableDatabase());

            AppOpenHelper helper = new AppOpenHelper(context,DB_NAME,null);
            sDaoMaster = new DaoMaster(helper.getWritableDatabase());
            
        }
        return sDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     * @return
     */
    public DaoSession getDaoSession(){
        if(sDaoSession == null){
            if(sDaoMaster == null){
                sDaoMaster = getDaoMaster();
            }
            sDaoSession = sDaoMaster.newSession();
        }
        return sDaoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug(){

        QueryBuilder.LOG_SQL = false;
        QueryBuilder.LOG_VALUES = false;

        if (BuildConfig.LOG_DEBUG) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }

    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection(){
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper(){
        if(sHelper != null){
            sHelper.close();
            sHelper = null;
        }
    }

    public void closeDaoSession(){
        if(sDaoSession != null){
            sDaoSession.clear();
            sDaoSession = null;
        }
    }



}
