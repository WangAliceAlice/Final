package Tools;

import java.sql.*;

/**
 * Created by zhuch on 2017/4/30.
 */
public class find {
    private static String url = null;          //����Ŀ�ĵ�ַ
    private static Connection connection = null;   //�������Ӷ���
    private static Statement statement = null;               //�����������
    private static ResultSet resultSet = null;                 //��ѯ���
    private static int rows = 0;                                // ��Ӱ��Ľ��
    private static String username = null;             //���ݿ��û���
    private static String password = null;             //���ݿ�����
    private static String sql = null;                  //��ѯ���

    /**
     * �������ܣ����ӻ��������ݿ�
     */
    public static void setValue() {
        url = "jdbc:mysql://localhost:3306/test";
        username = "doubibobo";
        password = "12151618";
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * �������ܣ�����û��Ƿ��Ѿ�ע��
     * @param uname
     * @param pwd
     * @return
     */
    public static boolean checkLogin(String uname, String pwd) {
        String databasepwd = null;      //���ݿ�������
        setValue();
        sql = "select * from user where user_name = '"+ uname+"'";
        try {
            resultSet  = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    databasepwd = resultSet.getString(3);
                    if (pwd.equals(databasepwd)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * �������ܣ���ȡ�Ա�
     * @param theName
     * @return
     */
    public static String getSex(String theName) {
        int databasesex = 0;
        String sex = null;
        setValue();
        sql = "select * from user where user_name = '"+ theName+"'";
        try {
            resultSet  = statement.executeQuery(sql);
            if (resultSet != null) {
                while (resultSet.next()) {
                    databasesex = resultSet.getInt(4);
                    if (databasesex == 0) {
                        sex = "��";
                    } else if (databasesex == 1){
                        sex = "Ů";
                    } else {
                        sex = "����";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sex;
    }

    /**
     * ��֤�Ƿ��Ѿ�ע���
     * @param theName
     * @param sex
     * @param userpassword
     * @return
     */
    public static boolean checkItems(String theName, String sex, String userpassword) {
        setValue();
        sql = "select * from user where user_name = '"+ theName+"'";
        try {
            resultSet  = statement.executeQuery(sql);
            System.out.println(resultSet.getRow());
            if (resultSet.next() == false) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * �������ܣ����ݿ������һ���û�
     * @param theName
     * @param sex
     * @param userpassword
     * @return
     */
    public static boolean addUser(String theName, String sex, String userpassword) {
        // ���ȶ��Ա���е�������Ϊ0��ŮΪ1������Ϊ2
        int theSex = 2;
        int count;
        switch (sex) {
            case "boy": theSex = 0;break;
            case "girl": theSex = 1;break;
            case "unknown": theSex = 2;break;
            default:return false;
        }
        sql = "SELECT COUNT(user_id) FROM user";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                count = resultSet.getInt(1);
                count++;
                sql = "INSERT INTO user VALUES (" + count + ", '" + theName + "', '"+ userpassword + "', " + theSex +")";
                try {
                    rows = statement.executeUpdate(sql);
                    if (rows != 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
