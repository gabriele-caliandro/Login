package com.itcube.main;

import com.itcube.menus.ClientInput;
import com.itcube.menus.ClientInputI;
import com.itcube.menus.Menu;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.itcube.logger.LoggerLogin.loggerLogin;
import static com.itcube.menus.MenuLogin.getMenuLogin;

public class Main {
  public static void main(String[] args) throws SQLException {
    try {
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Server.schema, "root", "root");
      Menu login = getMenuLogin(connection);
      ClientInputI clientInput = ClientInput.getClientInput();
      login.interact(clientInput);
    } catch (CommunicationsException e) {
      loggerLogin.error("Connection error \n Message: {} - \n SQL state: {}", e.getMessage(), e.getSQLState());
    }
  }
}
