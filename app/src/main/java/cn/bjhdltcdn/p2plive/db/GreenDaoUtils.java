package cn.bjhdltcdn.p2plive.db;

import android.util.Log;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.greendao.BaseUserDao;
import cn.bjhdltcdn.p2plive.greendao.PlaceOrderMessageDao;
import cn.bjhdltcdn.p2plive.greendao.PropsDao;
import cn.bjhdltcdn.p2plive.greendao.PushdbModelDao;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.PlaceOrderMessage;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.model.PushdbModel;
import cn.bjhdltcdn.p2plive.model.YouBanActivityMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanGroupMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanHelpInfoMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanOrganizationMessageModel;
import cn.bjhdltcdn.p2plive.model.YouBanSayLoveInfoMessageModel;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;

/**
 * 完成对某一张数据表的具体操作，ORM操作
 * Created by xiawenquan on 17/12/19.
 */

public class GreenDaoUtils {

    private static GreenDaoUtils instance;

    private ExecutorService cachedThreadPool;

    public static GreenDaoUtils getInstance() {
        if (instance == null) {
            DaoManager.getInstance().init(App.getInstance());
            DaoManager.getInstance().setDebug();
            instance = new GreenDaoUtils();

        }

        return instance;
    }

    public ExecutorService getCachedThreadPool() {
        if (cachedThreadPool == null) {
            cachedThreadPool = Executors.newScheduledThreadPool(10);
        }
        return cachedThreadPool;
    }

    public void getBaseUser(final long userId, final ExecuteCallBack callBack) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                QueryBuilder<BaseUser> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(BaseUser.class);
                List<BaseUser> list = queryBuilder.where(BaseUserDao.Properties.UserId.eq(userId)).list();
                if (callBack != null) {
                    callBack.callBack(list.get(0));
                }
            }
        });
    }

    public BaseUser getBaseUser(final long userId) {
        QueryBuilder<BaseUser> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(BaseUser.class);
        return queryBuilder.where(BaseUserDao.Properties.UserId.eq(userId)).list().get(0);
    }

    public void getListBaseUser(final String sql, final String[] conditions, final ExecuteCallBack callBack) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<BaseUser> list = DaoManager.getInstance().getDaoSession().queryRaw(BaseUser.class, sql, conditions);
                if (callBack != null) {
                    callBack.callBack(list);
                }
            }
        });
    }


    /**
     * 单条插入数据
     *
     * @param baseUser
     * @return
     */
    public void insertBaseUser(final BaseUser baseUser) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getBaseUserDao().insertOrReplace(baseUser);
            }
        });
    }

    /**
     * 批量插入数据
     *
     * @param list
     */
    public void insertListBaseUser(final List<BaseUser> list) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getBaseUserDao().insertOrReplaceInTx(list);
            }
        });
    }





    /**
     * 单条删除数据
     *
     * @param baseUser
     */
    public void deleteBaseUser(final BaseUser baseUser) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getBaseUserDao().delete(baseUser);
            }
        });
    }

    /**
     * 批量删除数据
     *
     * @param list
     */
    public void deleteListBaseUser(final List<BaseUser> list) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getBaseUserDao().deleteInTx(list);
            }
        });
    }

    /**
     * 清除baseUser数据表
     */
    public void deleteAllBaseUser() {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getBaseUserDao().deleteAll();
            }
        });
    }


    /**
     * 单条插入数据
     *
     * @param pushdbModel
     */
    public void insertPushdbModel(final PushdbModel pushdbModel) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPushdbModelDao().insertOrReplace(pushdbModel);
            }
        });
    }

    /**
     * 批量插入数据
     *
     * @param list
     */
    public void insertListPushdbModel(final List<PushdbModel> list) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPushdbModelDao().insertOrReplaceInTx(list);
            }
        });
    }

    /**
     * 插入礼物数据
     *
     * @param list
     */
    public void insertListProps(final List<Props> list) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPropsDao().insertOrReplaceInTx(list);
            }
        });
    }

    /**
     * 根据id查询道具
     *
     * @param id
     * @param callBack
     */
    public void getPropsById(final long id, final ExecuteCallBack callBack) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Props load = DaoManager.getInstance().getDaoSession().getPropsDao().load(id);
                if (callBack != null) {
                    callBack.callBack(load);
                }
            }
        });
    }

    /**
     * 根据id查询单条数据
     *
     * @param id
     * @param callBack
     */
    public void queryPushdbModelById(final long id, final ExecuteCallBack callBack) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                PushdbModel pushdbModel = DaoManager.getInstance().getDaoSession().getPushdbModelDao().load(id);
                if (callBack != null) {
                    callBack.callBack(pushdbModel);
                }
            }
        });
    }


    /**
     * 使用sql语句查询
     *
     * @param sql
     * @param conditions
     * @return
     */
    public void queryPushdbModelBySql(final String sql, final String[] conditions, final ExecuteCallBack callBack) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<PushdbModel> list = DaoManager.getInstance().getDaoSession().queryRaw(PushdbModel.class, sql, conditions);
                if (callBack != null) {
                    callBack.callBack(list);
                }
            }
        });

    }

    /**
     * 根据giftType类型获取礼物道具
     *
     * @param callBack
     * @param type     1单人 2多人
     */
    public void getPropsListByGiftType(final ExecuteCallBack callBack, final int type) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //从数据库读取
                PropsDao propsDao = DaoManager.getInstance().getDaoSession().getPropsDao();
                if (propsDao != null) {
                    List<Props> list = propsDao.queryBuilder().where(PropsDao.Properties.GiftType.in(type)).orderAsc(PropsDao.Properties.SalePrice).list();
                    if (callBack != null) {
                        callBack.callBack(list);
                    }
                }
            }
        });
    }


    /**
     * 单条更新数据
     *
     * @param pushdbModel
     */
    public void updatePushdbModel(final PushdbModel pushdbModel) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPushdbModelDao().update(pushdbModel);
            }
        });
    }


    /**
     * 使用SQL更新数据
     *
     * @param sql
     */
    public void updatePushdbModelBySq(final String sql) {
        Database db = DaoManager.getInstance().getDaoMaster().newSession().getDatabase();
        if (db != null) {
            db.execSQL(sql);
        }
    }

    /**
     * 使用SQL更新数据
     *
     * @param sql
     * @param conditions
     */
    public void updatePushdbModelBySq(final String sql, final String[] conditions) {

        Database db = DaoManager.getInstance().getDaoMaster().newSession().getDatabase();
        if (db != null) {
            db.execSQL(sql, conditions);
        }

    }


    /**
     * 分页查询数据
     *
     * @param params     消息类型
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param callBack   回调接口
     */
    public void queryPushdbModelByQueryBuilder(final int pageNumber, final int pageSize, final ExecuteCallBack callBack, final Object... params) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    QueryBuilder<PushdbModel> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(PushdbModel.class);
                    queryBuilder.where(PushdbModelDao.Properties.MessageType.in(params));
                    queryBuilder.orderDesc(PushdbModelDao.Properties.MessageId);
                    queryBuilder.offset((pageNumber - 1) * pageSize).limit(pageSize);

                    List<PushdbModel> pushdbModelList = queryBuilder.list();

                    if (pushdbModelList != null && pushdbModelList.size() > 0) {

                        List<Object> list = new ArrayList<>(1);

                        for (PushdbModel pushdbModel : pushdbModelList) {

                            if ("RC:YouBanActivity".equals(pushdbModel.getObjectName())) {// 活动消息

                                YouBanActivityMessageModel youBanActivityMessageModel = JsonUtil.getObjectMapper().readValue(pushdbModel.getSource(), YouBanActivityMessageModel.class);
                                youBanActivityMessageModel.setMessageId(pushdbModel.getMessageId());
                                list.add(youBanActivityMessageModel);

                            } else if ("RC:YouBanOrganization".equals(pushdbModel.getObjectName())) {// 圈子

                                YouBanOrganizationMessageModel youBanOrganizationMessageModel = JsonUtil.getObjectMapper().readValue(pushdbModel.getSource(), YouBanOrganizationMessageModel.class);
                                youBanOrganizationMessageModel.setMessageId(pushdbModel.getMessageId());
                                list.add(youBanOrganizationMessageModel);

                            } else if ("RC:YouBanGroup".equals(pushdbModel.getObjectName())) {// 群

                                YouBanGroupMessageModel youBanGroupSharedMessageModel = JsonUtil.getObjectMapper().readValue(pushdbModel.getSource(), YouBanGroupMessageModel.class);
                                youBanGroupSharedMessageModel.setMessageId(pushdbModel.getMessageId());
                                list.add(youBanGroupSharedMessageModel);

                            } else if ("RC:YouBanSayLoveInfo".equals(pushdbModel.getObjectName())) {// 表白

                                YouBanSayLoveInfoMessageModel youBanSayLoveInfoMessageModel = JsonUtil.getObjectMapper().readValue(pushdbModel.getSource(), YouBanSayLoveInfoMessageModel.class);
                                youBanSayLoveInfoMessageModel.setMessageId(pushdbModel.getMessageId());
                                list.add(youBanSayLoveInfoMessageModel);

                            } else if ("RC:YouBanHelpInfo".equals(pushdbModel.getObjectName())) {// 同学帮帮忙
                                YouBanHelpInfoMessageModel youBanHelpInfoMessageModel = JsonUtil.getObjectMapper().readValue(pushdbModel.getSource(), YouBanHelpInfoMessageModel.class);
                                youBanHelpInfoMessageModel.setMessageId(pushdbModel.getMessageId());
                                list.add(youBanHelpInfoMessageModel);
                            }

                        }

                        pushdbModelList.clear();
                        pushdbModelList = null;

                        if (callBack != null) {
                            callBack.callBack(list);
                        }

                    } else {
                        if (callBack != null) {
                            callBack.callBack(null);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    if (callBack != null) {
                        callBack.callBack(null);
                    }

                }
            }
        });
    }

    /**
     * 根据类型查询未读数
     *
     * @param params
     * @return
     */

    public long queryUnreadCountByQueryBuilder(Object... params) {
        QueryBuilder queryBuilder = DaoManager.getInstance().getDaoSession().getPushdbModelDao().queryBuilder();
        return queryBuilder.where(PushdbModelDao.Properties.UnRead.eq("0"), PushdbModelDao.Properties.MessageType.in(params)).count();
    }


    /**
     * 查询所有未读数
     *
     * @return
     */
    public long queryPushdbModelUnreadAllCountByQueryBuilder() {
        QueryBuilder queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(PushdbModel.class);
        queryBuilder.where(PushdbModelDao.Properties.UnRead.eq("0"));
        return queryBuilder.buildCount().count();
    }


    /**
     * 清除pushdbModel数据表
     */
    public void deleteAllPushdbModel() {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPushdbModelDao().deleteAll();
            }
        });
    }


    /**
     * 清除pushdbModel数据表
     */
    public void deletePushdbModelById(final long messageId) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPushdbModelDao().deleteByKey(messageId);
            }
        });
    }


    /**
     * 清除Props数据表
     */
    public void deleteAllProps(final ExecuteCallBack callBack) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPropsDao().deleteAll();
                if (callBack != null) {
                    callBack.callBack(1);
                }
            }
        });
    }

    /**
     * 插入下单红点消息
     * @param placeOrderMessage
     */
    public void insertPlaceOrderMessage(final PlaceOrderMessage placeOrderMessage){
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPlaceOrderMessageDao().insertOrReplace(placeOrderMessage);
            }
        });
    }

    /**
     * 查询数据库下单消息
     * @param userId
     * @return
     */
    public PlaceOrderMessage getPlaceOrderMessage(final long userId,final long storeId) {
        QueryBuilder<PlaceOrderMessage> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(PlaceOrderMessage.class);
        List<PlaceOrderMessage> placeOrderMessageLis=queryBuilder.where(PlaceOrderMessageDao.Properties.UserIdAndStoreId.eq(userId+"/"+storeId)).list();
        if(placeOrderMessageLis!=null&&placeOrderMessageLis.size()>0){
            return placeOrderMessageLis.get(0);
        }
        return null;
    }

    /**
     * 删除数据库下单消息
     * @param userId
     * @return
     */
    public void deletePlaceOrderMessageById(final long userId, final long storeId) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().getPlaceOrderMessageDao().deleteByKey(userId+"/"+storeId);
            }
        });
//        getCachedThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                String sql = "DELETE FROM PLACE_ORDER_MESSAGE WHERE USER_ID_AND_STORE_ID="+userId+"/"+storeId;
//                try {
//                    DaoManager.getInstance().getDaoMaster().getDatabase().execSQL(sql);
//                }catch (Exception e){
//                    Log.e("exception",e.toString());
//                }
//            }
//        });
    }

    /**
     * 清除指定的表数据
     *
     * @param entityClass
     */
    public <T> void deleteAll(final Class<T> entityClass) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DaoManager.getInstance().getDaoSession().deleteAll(entityClass);
            }
        });
    }


    /**
     * 批量清表数据
     *
     * @param entityClass
     * @param <T>
     */
    public <T> void deleteAll(final List<Class<T>> entityClass) {
        getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < entityClass.size(); i++) {
                    DaoManager.getInstance().getDaoSession().deleteAll(entityClass.get(i));
                }

                if (entityClass != null) {
                    entityClass.clear();
                }
            }
        });
    }


    public void onDestroy() {
        if (cachedThreadPool != null) {
            cachedThreadPool.shutdownNow();
        }
        cachedThreadPool = null;

        DaoManager.getInstance().closeConnection();

        if (instance != null) {
            instance = null;
        }

    }

    public interface ExecuteCallBack {
        void callBack(Object object);
    }

}
