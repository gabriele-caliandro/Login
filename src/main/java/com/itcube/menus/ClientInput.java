package com.itcube.menus;

import com.itcube.entities.Person;
import com.itcube.entities.User;

import java.util.Scanner;

public class ClientInput implements ClientInputI {
  private static ClientInput clientInput;

  private ClientInput() {}
  public static ClientInput getClientInput() {
    if (clientInput == null) {
      clientInput = new ClientInput();
    }
    return clientInput;
  }

  @Override
  public Person getPerson() {
    Scanner s = new Scanner(System.in);
    System.out.print("Insert name: ");
    String name = s.nextLine();
    System.out.print("Insert surname: ");
    String surname = s.nextLine();
    return Person.createPerson(0, name, surname);
  }

  @Override
  public User getUser() {
    Scanner s = new Scanner(System.in);
    System.out.print("username: ");
    String username = s.nextLine();
    System.out.print("password: ");
    String password = s.nextLine();
    return User.createUser(username, password, "base");

  }

  @Override
  public String getString() {
    Scanner s = new Scanner(System.in);
    return s.nextLine();
  }
}
