package util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionPool {

    private static final String DB_URL = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static ConnectionPool instance;
    private static final int DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;


    private ConnectionPool() {
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    static {
        initConnectionPool();
    }


    private static void initConnectionPool() {

        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);

        for (int i = 0; i < size; i++) {
            var connection = open();
            var ProxyConnection = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(ProxyConnection);
        }
    }


    private static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(DB_URL));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

