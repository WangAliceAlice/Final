package TestofSql;

import java.sql.*;

/**
 * Created by zhuch on 2017/4/30.
 */
public class selectTable {
    public static void main (String args[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("�ɹ�����MySQL����");
        } catch (ClassNotFoundException e) {
            System.out.println("�Ҳ���mysql����");
            e.printStackTrace();
        }

        // �������ݿ������MySQL�е�test���ݿ�
        String url = "jdbc:mysql://localhost:3306/test";
        // ����DirverManager�����һ��getConnection()����������һ�����Ӷ���
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, "doubibobo", "12151618");

            Statement stmt = conn.createStatement();
            System.out.println("�ɹ����ӵ����ݿ�");

            String sql = "select * from basic";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ѧ��"+"\t" + "\t" +"����");

            while (rs.next()) {
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.println();
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
