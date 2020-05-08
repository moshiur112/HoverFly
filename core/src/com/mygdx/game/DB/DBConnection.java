package com.mygdx.game.DB;

// close the connection

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    Connection connection;

    public DBConnection (){
    }

    public static void main(String[] args) {

        DBConnection con = new DBConnection();
        con.createConnection();
//        con.insetData("");



    }
    public void startConnection(){
        DBConnection con = new DBConnection();
        con.createConnection();
//        con.insetData("");
    }

    public void createConnection () {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/HoverFly?user=root&password=moshiur1&serverTimezone=UTC"
            );
            System.out.println("Connection established");

            Statement stm = connection.createStatement();
            ResultSet rs =  stm.executeQuery("SELECT * FROM HOVERFLY");


            while(rs.next()){
                String name = rs.getString("name");
                System.out.println(name);
            }
            stm.close();

        } catch (ClassNotFoundException e ) {
            System.out.println("Class not found jdbc or sql exception happened");
            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }



    public void insetData (String input) {
        try {
//            Statement stmt = connection.createStatement();
//            stmt.execute("INSERT INTO hoverfly VALUES ('eero', '123', 'Eero', 100)");
//            stmt.close();

            String sql = "INSERT INTO hoverfly (username, password, name, highscore)" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "benjamin");
            preparedStatement.setString(2, "123");
            preparedStatement.setString(3, "Benjamin");
            preparedStatement.setInt(4, 50);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
