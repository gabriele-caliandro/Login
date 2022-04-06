package com.itcube.interact;

import com.itcube.entities.User;

import java.sql.Connection;
import java.util.Optional;

public interface ClientAccessOperation {
  Optional<User> loginUser(User user, Connection connection);
  boolean registerUser(User user, Connection connection);
}
