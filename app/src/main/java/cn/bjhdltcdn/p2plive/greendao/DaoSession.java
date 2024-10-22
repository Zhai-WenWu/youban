package cn.bjhdltcdn.p2plive.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.PlaceOrderMessage;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.model.PushdbModel;

import cn.bjhdltcdn.p2plive.greendao.BaseUserDao;
import cn.bjhdltcdn.p2plive.greendao.PlaceOrderMessageDao;
import cn.bjhdltcdn.p2plive.greendao.PropsDao;
import cn.bjhdltcdn.p2plive.greendao.PushdbModelDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig baseUserDaoConfig;
    private final DaoConfig placeOrderMessageDaoConfig;
    private final DaoConfig propsDaoConfig;
    private final DaoConfig pushdbModelDaoConfig;

    private final BaseUserDao baseUserDao;
    private final PlaceOrderMessageDao placeOrderMessageDao;
    private final PropsDao propsDao;
    private final PushdbModelDao pushdbModelDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        baseUserDaoConfig = daoConfigMap.get(BaseUserDao.class).clone();
        baseUserDaoConfig.initIdentityScope(type);

        placeOrderMessageDaoConfig = daoConfigMap.get(PlaceOrderMessageDao.class).clone();
        placeOrderMessageDaoConfig.initIdentityScope(type);

        propsDaoConfig = daoConfigMap.get(PropsDao.class).clone();
        propsDaoConfig.initIdentityScope(type);

        pushdbModelDaoConfig = daoConfigMap.get(PushdbModelDao.class).clone();
        pushdbModelDaoConfig.initIdentityScope(type);

        baseUserDao = new BaseUserDao(baseUserDaoConfig, this);
        placeOrderMessageDao = new PlaceOrderMessageDao(placeOrderMessageDaoConfig, this);
        propsDao = new PropsDao(propsDaoConfig, this);
        pushdbModelDao = new PushdbModelDao(pushdbModelDaoConfig, this);

        registerDao(BaseUser.class, baseUserDao);
        registerDao(PlaceOrderMessage.class, placeOrderMessageDao);
        registerDao(Props.class, propsDao);
        registerDao(PushdbModel.class, pushdbModelDao);
    }
    
    public void clear() {
        baseUserDaoConfig.clearIdentityScope();
        placeOrderMessageDaoConfig.clearIdentityScope();
        propsDaoConfig.clearIdentityScope();
        pushdbModelDaoConfig.clearIdentityScope();
    }

    public BaseUserDao getBaseUserDao() {
        return baseUserDao;
    }

    public PlaceOrderMessageDao getPlaceOrderMessageDao() {
        return placeOrderMessageDao;
    }

    public PropsDao getPropsDao() {
        return propsDao;
    }

    public PushdbModelDao getPushdbModelDao() {
        return pushdbModelDao;
    }

}
