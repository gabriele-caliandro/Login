package com.itcube.menus;

import com.itcube.entities.Person;
import com.itcube.entities.User;

public interface ClientInputI {
  // Persona
  Person getPerson();
  // User
  User getUser();
  // Menu input
  String getString();
}
