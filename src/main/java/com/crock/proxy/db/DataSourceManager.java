package com.crock.proxy.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.DbUtils;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yanshi on 9/12/14.
 */
public enum DataSourceManager {

    INSTANCE;

    private ComboPooledDataSource cpds;

    DataSourceManager() {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(Config.DRIVER_CLASS);
            cpds.setJdbcUrl(Config.DB_URL);
            cpds.setUser(Config.USER_NAME);
            cpds.setPassword(Config.PASS_WORD);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setInitialPoolSize(50);

        cpds.setMinPoolSize(50);
        cpds.setMaxPoolSize(0);
        cpds.setAcquireIncrement(100);
        cpds.setMaxStatements(0);
        cpds.setMaxStatementsPerConnection(200);
        cpds.setTestConnectionOnCheckin(true);
        cpds.setTestConnectionOnCheckout(true);
        cpds.setCheckoutTimeout(50000);
        cpds.setNumHelperThreads(5);
        cpds.setUnreturnedConnectionTimeout(60);
        cpds.setMaxIdleTimeExcessConnections(60);
        cpds.setMaxConnectionAge(60);
    }

    public synchronized  Connection  getConnection() {
        Connection conn = null;
        try {
            conn = cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void rollback(Connection conn) {
        try {
           DbUtils.rollbackAndClose(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit(Connection conn) {
        try {
            DbUtils.commitAndClose(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Connection conn) {
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
