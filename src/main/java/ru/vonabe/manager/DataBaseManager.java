package ru.vonabe.manager;

import java.sql.*;

public class DataBaseManager {

    private static DataBaseManager db = null;
    private static String url = "src/res/LordyDB.db", login = "", password = "";
    private Connection connection = null;
    private Statement statement = null;
    private JDBCConnector jdbc = null;

    private DataBaseManager() {
        jdbc = new JDBCConnector();
        jdbc.setPath(url);

        connection = jdbc.getConnection();
        statement = jdbc.getStatement();
    }

    private static DataBaseManager getDB() {
        return (db == null) ? db = new DataBaseManager() : db;
    }

    public PreparedStatement insertOrUpdateStatement(String sql, String... return_result) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql, return_result);
            statement.executeUpdate();
            return statement;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int insertOrUpdate(String sql) {
        return jdbc.executeUpdate(sql);
    }

    public void addBatch(String... sql) {
        try {
            for (String s : sql)
                statement.addBatch(s);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int[] executeBatch() {
        try {
            return statement.executeBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void clearBatch() {
        try {
            statement.clearBatch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ResultSet query(String sql) {
        return jdbc.executeQuery(sql);
    }

}
