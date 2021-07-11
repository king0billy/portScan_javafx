/**
 * @author 22427(king0liam)
 * @version 1.0
 * @ClassName: DriverUtils
 * @Description
 * @Date: 2021/7/8 12:54
 * @since version-0.0
 */


import java.sql.*;
//private static final String url = "jdbc:mysql://localhost:3306/iana_default?useUnicode=true&characterEncoding=utf8";
//    private static final String url= "jdbc:mysql://localhost:3306/iana_default?useUnicode=true&amp;characterEncoding=UTF-8&amp;useSSL=false&amp;serverTimezone=GMT";
public final class DriverUtils {
    private static final String url= "jdbc:mysql://localhost:3306/iana_default?useUnicode=true&characterEncoding=UTF-8&useSSL=false&ServerTimezone=UTC";
    private static final String user = "root";
    private static final String passWord = "123456";
    private static  Connection connection;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection() {
        if(null == connection) {
            try {
                connection = DriverManager.getConnection(url, user, passWord);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
    /**
     * 获取数据库的连接
     * @return connection
     */
    /**
     * 释放资源
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void release(Connection connection, Statement statement, ResultSet resultSet){
        /**
         * @description 释放连接的方法
         * @exception
         * @param [connection, statement, resultSet]
         * @return [java.sql.Connection, java.sql.Statement, java.sql.ResultSet]
         * @since version-1.0
         * @author 22427(king0liam)
         * @date 2021/6/18 14:30
         */
        if(resultSet!=null){
            try {
                resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if(statement!=null){
            try {
                statement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
