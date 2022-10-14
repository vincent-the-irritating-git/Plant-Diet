package com.dissertation.server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {

  Connection conn;

  public void getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    conn = DriverManager.getConnection("jdbc:sqlite:PlantDiet.db");
    if (conn.isClosed()) {
      System.out.println("Connexion not open");
    } else {
      System.out.println("Connexion established!");
    }
  }

}