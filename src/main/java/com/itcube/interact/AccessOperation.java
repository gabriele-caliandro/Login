package com.itcube.interact;

import com.itcube.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.itcube.logger.LoggerLogin.loggerLogin;
import static com.itcube.utils.UsefulChar.quote;

public class AccessOperation implements ClientAccessOperation {
  private static AccessOperation accessOp;

  public static AccessOperation getAccessOperation() {
    if (accessOp == null) {
      accessOp = new AccessOperation();
    }
    return accessOp;
  }

  @Override
  public Optional<User> loginUser(User user, Connection connection) {
    try {
      PreparedStatement query = connection.prepareStatement(
              " select * " +
                      " from users " +
                      " where username = " + quote(user.getUsername()) + ";"
      );
      ResultSet r = query.executeQuery();
      if (!r.next()) {
        loggerLogin.info("Cannot login. User not present");
        return Optional.empty();
      }
      PreparedStatement statement = connection.prepareStatement(
              "select *  " +
                      " from users " +
                      " where username = " + quote(user.getUsername()) +
                      " and password = " + quote(user.getPassword()) + ";"
      );
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        loggerLogin.debug("User logged");
        return Optional.of(user);
      } else {
        loggerLogin.info("Wrong username or password");
      }
    } catch (SQLException e) {
      loggerLogin.warn("Login failed. {} \n State: {} \n ErrorCode: {}",
              e.getMessage(), e.getSQLState(), e.getErrorCode());
    }
    return Optional.empty();
  }

  @Override
  public boolean registerUser(User user, Connection connection) {
    try {
      PreparedStatement insert = connection.prepareStatement(
              "insert into users (username,password) values ("
                      + quote(user.getUsername()) + "," + quote(user.getPassword()) + ");");
      insert.executeUpdate();
      loggerLogin.info("User registered");
      return true;
    } catch (SQLException e) {
      loggerLogin.warn("Registration failed. {} \n State: {} \n ErrorCode: {}",
              e.getMessage(), e.getSQLState(), e.getErrorCode());
    }
    return false;
  }
}