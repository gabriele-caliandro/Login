package com.itcube.menus;

import com.itcube.entities.User;
import com.itcube.interact.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.itcube.entities.User.createUser;
import static com.itcube.interact.AccessOperation.getAccessOperation;
import static com.itcube.logger.LoggerLogin.loggerLogin;
import static com.itcube.menus.MenuUser.getMenuPersonOperation;
import static java.lang.System.exit;

public class MenuLogin implements Menu {
  private final Connection connection;
  private final ClientAccessOperation access = getAccessOperation();
  private static MenuLogin menuLogin;

  private MenuLogin(Connection connection) {
    this.connection = connection;
    if (!isThereAdmin()) {
      exit(0);
    }
  }

  public static MenuLogin getMenuLogin(Connection connection) {
    if (menuLogin == null)
      menuLogin = new MenuLogin(connection);
    return menuLogin;
  }

  private boolean isThereAdmin() {
    try {
      ResultSet r = connection
              .prepareStatement("select username from users where role = 'admin' ")
              .executeQuery();
      if (!r.next()) {
        return createAdmin();
      }
      loggerLogin.debug("There is admin");
      return true;
    } catch (SQLException e) {
      loggerLogin.error("Error in isThereAdmin(). \n Message: {} \n State: {} \n ErrorCode: {}", e.getMessage(), e.getSQLState(), e.getErrorCode());
    }
    return false;
  }

  private boolean createAdmin() {
    try {
      connection
              .prepareStatement("insert into users (username,password,role) " +
                      " values ('admin','admin','admin')")
              .executeUpdate();
      loggerLogin.debug("Admin created in DB.");
      return true;
    } catch (SQLException e) {
      loggerLogin.error("Error in creatingAdmin. Message: {}", e.getMessage());
    }
    return false;
  }

  @Override
  public void display() {
    System.out.println("1 - Login");
    System.out.println("2 - Register");
    System.out.println("0 - Exit");
    System.out.print("Choice: ");
  }

  @Override
  public void interact(ClientInputI clientInput) {
    User tempUser;
    boolean end = false;
    Optional<User> user;

    do {
      display();
      String choice = clientInput.getString();
      switch (choice) {
        case "1" -> {
          // User login
          tempUser = clientInput.getUser();
          user = access.loginUser(tempUser, connection);
          System.out.println();
          if (user.isPresent()) {
            Menu menuPersonOperation = getMenuPersonOperation(connection, user.get());
            menuPersonOperation.interact(clientInput);
          } else {
            System.out.println("No such user present");
          }
        }
        case "2" -> {
          // Registration
          tempUser = clientInput.getUser();
          System.out.println("Admin login required");
          User admin = createUser("admin", "admin", "admin");
          // Admin login request
          if (access.loginUser(admin, connection).isEmpty()) {
            System.out.println("Access denied");
            loggerLogin.debug("Admin not logged");
            break;
          }
          // Admin approval
          System.out.println("Does admin approve? (y/n)");
          if (clientInput.getString().equalsIgnoreCase("y")) {
            loggerLogin.debug("User approved");
            if (access.registerUser(tempUser, connection)) {
              System.out.println("Person inserted in DB");
            } else {
              System.out.println("Error in registration");
            }
          } else {
            loggerLogin.debug("User approved");
            System.out.println("User rejected");
          }
        }
        case "0" -> end = true;
        default -> {
        }
      }
    } while (!end);
    System.out.println("Login Ended");
  }
}

