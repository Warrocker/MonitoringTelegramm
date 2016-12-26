import com.sun.istack.internal.Nullable;

import java.sql.*;
import java.util.ArrayList;

class DatabaseHelper {
    private static DatabaseHelper dbHelper = new DatabaseHelper();
    private Connection dbConnection;
    Statement statement;
    private DatabaseHelper(){
        dbConnection = getDBConnection();
    }
    public static DatabaseHelper getInstance(){
        return dbHelper;
    }

    private static Connection getDBConnection(){
        Connection dbConnection = null;
        try {
            //Ищем драйвер
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //Подключаемся к базе драйвер:база:адресс:порт(стандартный порт 3306)/имя_базы?useUnicode=true&characterEncoding=utf-8, логин, пароль
            dbConnection = DriverManager.getConnection("jdbc:mysql://Your_IP:3306/actros?useUnicode=true&characterEncoding=utf-8", "Your_user","pass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }
    @Nullable
    public ArrayList<Switch> dbQuery(String query){
        ResultSet resultSet;
        ArrayList<Switch> switches = new ArrayList<>();
        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                switches.add(new Switch(resultSet.getString("address"),resultSet.getString("ip")));
            }
            return switches;
        }catch (SQLException e){
            e.printStackTrace();
            return switches;
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Cant close statement");
            }
        }
    }


}
