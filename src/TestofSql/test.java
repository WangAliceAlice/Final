package TestofSql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by zhuch on 2017/4/30.
 */
public class test {
    public static void main (String args[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("�ɹ�����MySQL������");
        } catch (ClassNotFoundException e) {
            System.out.println("�Ҳ���MySQL������");
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/mysql";
        // ����DriverManager�����getConnection()���������һ��Connection����
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, "root", "12151618");
            // ����һ��statement����
            Statement stmt = conn.createStatement();
            System.out.println("�ɹ����ӵ����ݿ⣡");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
