package com.crock.proxy;

import com.crock.proxy.db.DataSourceManager;
import com.google.common.collect.Lists;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by yanshi on 15-6-24.
 */
public class MySqlDBResourceLoader implements ProxyResourceLoader {

    private QueryRunner queryRunner = new QueryRunner();

    private List<Proxy> findProxies() {
        Connection conn = DataSourceManager.INSTANCE.getConnection();
        try {
            return queryRunner.query(conn, "select id,host,port,ms from proxy_list", new ResultSetHandler<List<Proxy>>() {
                @Override
                public List<Proxy> handle(ResultSet rs) throws SQLException {

                    List<Proxy> proxyList = Lists.newArrayList();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String host = rs.getString("host");
                        int port = rs.getInt("port");
                        int ms = rs.getInt("ms");

                        Proxy proxy = new Proxy(host, port);
                        proxy.setMs(ms);
                        proxy.setId(id);

                        proxyList.add(proxy);
                    }
                    return proxyList;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public void updateRepTime(Proxy proxy) {
        Connection conn = DataSourceManager.INSTANCE.getConnection();
        try {
            queryRunner.update(conn, "update proxy_list set ms=? where id=? ", new Object[]{proxy.getMs(),proxy.getId()});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    @Override
    public List<Proxy> loadProxies() {
        return Collections.unmodifiableList(Lists.asList(new Proxy("101.69.178.2", 8888),
                new Proxy[]{new Proxy("61.174.8.3", 8888),
                        new Proxy("61.174.8.4", 8888)
                        , new Proxy("61.174.8.5", 8888)
                        , new Proxy("61.174.8.6", 8888)
                        , new Proxy("61.174.8.7", 8888)
                        , new Proxy("61.174.8.8", 8888)
                        , new Proxy("61.174.8.9", 8888)
                        , new Proxy("61.174.8.10", 8888)
                        , new Proxy("61.174.8.11", 8888)
                        , new Proxy("61.174.8.12", 8888)
                        , new Proxy("61.174.8.13", 8888)
                        , new Proxy("61.174.8.14", 8888)
                        , new Proxy("61.174.8.15", 8888)
                        , new Proxy("61.174.8.16", 8888)
                        , new Proxy("61.174.8.17", 8888)
                        , new Proxy("61.174.8.18", 8888)
                        , new Proxy("61.174.8.19", 8888)
                        , new Proxy("61.174.8.20", 8888)
                        , new Proxy("61.174.8.21", 8888)
                        , new Proxy("61.174.8.22", 8888)
                        , new Proxy("61.174.8.23", 8888)
                        , new Proxy("61.174.8.24", 8888)
                        , new Proxy("61.174.8.25", 8888)
                        , new Proxy("61.174.8.26", 8888)
                        , new Proxy("61.174.8.27", 8888)
                        , new Proxy("61.174.8.28", 8888)
                        , new Proxy("61.174.8.29", 8888)
                        , new Proxy("61.174.8.30", 8888)
                        , new Proxy("61.174.8.31", 8888)
                        , new Proxy("61.174.8.32", 8888)
                        , new Proxy("61.174.8.33", 8888)
                        , new Proxy("61.174.8.34", 8888)
                        , new Proxy("61.174.8.35", 8888)
                        , new Proxy("61.174.8.36", 8888)
                        , new Proxy("61.174.8.37", 8888)
                        , new Proxy("61.174.8.38", 8888)
                        , new Proxy("61.174.8.39", 8888)
                        , new Proxy("61.174.8.40", 8888)
                        , new Proxy("61.174.8.41", 8888)
                        , new Proxy("61.174.8.42", 8888)
                        , new Proxy("61.174.8.44", 8888)
                        , new Proxy("61.174.8.45", 8888)
                        , new Proxy("61.174.8.46", 8888)
                        , new Proxy("61.174.8.47", 8888)
                        , new Proxy("61.174.8.48", 8888)
                        , new Proxy("61.174.8.49", 8888)
                        , new Proxy("61.174.8.50", 8888)
                        , new Proxy("61.174.8.51", 8888)
                        , new Proxy("61.174.8.52", 8888)
                        , new Proxy("61.174.8.53", 8888)
                        , new Proxy("61.174.8.54", 8888)
                        , new Proxy("61.174.8.55", 8888)
                        , new Proxy("61.174.8.56", 8888)
                }));

    }
}
