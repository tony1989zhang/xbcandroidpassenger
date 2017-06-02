package com.lvchehui.www.xiangbc.utils;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by 张灿能 on 2016/5/10.
 * 作用：获取DbManager，代理操作数据库
 */
public class DbUtil {

    private static DbUtil dbUtil;

    /**
     * DbManager的基本配置
     */
    private DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("xbc.db")
            // .setDbDir(new File(Constants.FilePath.dbPath))
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // 执行增删改查
                    XgoLog.e("数据库版本更新：" + newVersion);
                }
            });

    private DbUtil() {

    }

    public static DbUtil getInstance() {
        if (null == dbUtil) {
            dbUtil = new DbUtil();
        }
        return dbUtil;
    }

    public DbManager getDbManager() {
        return x.getDb(daoConfig);
    }




}
