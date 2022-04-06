package com.itcube.menus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.itcube.menus.MenuLogin.getMenuLogin;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuLoginTest {
  @Mock
  Connection c;
  @Mock
  PreparedStatement stmt;
  @Mock
  ResultSet r;

  @Test
  public void adminCreationTest() throws SQLException {
    when(c.prepareStatement(anyString())).thenReturn(stmt);
    when(stmt.executeQuery()).thenReturn(r);
    when(r.next()).thenReturn(false);
//    when(stmt.executeQuery()).thenThrow(new SQLException());
//    when(stmt.executeUpdate()).thenThrow(new SQLException());
    getMenuLogin(c);
  }

}