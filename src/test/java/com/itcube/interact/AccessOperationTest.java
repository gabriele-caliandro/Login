package com.itcube.interact;

import com.itcube.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.itcube.entities.User.createUser;
import static com.itcube.interact.AccessOperation.getAccessOperation;
import static com.itcube.utils.UsefulChar.quote;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessOperationTest {
  @Mock
  Connection c;
  @Mock
  PreparedStatement stmt1;
  @Mock
  PreparedStatement stmt2;
  @Mock
  ResultSet result1;
  @Mock
  ResultSet result2;
  User user = createUser("gabriele", "caliandro", "admin");

  @BeforeEach
  public void setUp() {
  }

  /**
   * Tests a failed login, no user registered
   */
  @Test
  public void shouldNotExistTest() throws SQLException {
    ClientAccessOperation a = new AccessOperation();
    when(c.prepareStatement(anyString())).thenReturn(stmt1);
    when(stmt1.executeQuery()).thenReturn(result1);
    when(result1.next()).thenReturn(false);
    assertEquals(a.loginUser(user, c), Optional.empty());
  }

  /**
   * Tests a successful login
   */
  @Test
  public void shouldExistTest() throws SQLException {
    ClientAccessOperation a =  getAccessOperation();
    when(c.prepareStatement(anyString())).thenReturn(stmt1);
    when(stmt1.executeQuery()).thenReturn(result1);
    when(result1.next()).thenReturn(true);
    assertTrue(a.loginUser(user, c).isPresent());
    assertEquals(user, a.loginUser(user, c).get());
  }

  /**
   * Tests a failed login
   */
  @Test
  public void wrongPasswordTest() throws SQLException {
    ClientAccessOperation a = getAccessOperation();
    when(c.prepareStatement(" select * " +
            " from users " +
            " where username = " + quote(user.getUsername()) + ";")).thenReturn(stmt1);
    when(stmt1.executeQuery()).thenReturn(result1);
    when(result1.next()).thenReturn(true);
    when(c.prepareStatement("select *  " +
            " from users " +
            " where username = " + quote(user.getUsername()) +
            " and password = " + quote(user.getPassword()) + ";")).thenReturn(stmt2);
    when(stmt2.executeQuery()).thenReturn(result2);
    when(result2.next()).thenReturn(false);
    assertEquals(a.loginUser(user, c), Optional.empty());
  }

  /**
   * Test both if approved or rejected
   */
  @Test
  public void registerUserTest() throws SQLException {
    ClientAccessOperation reg = getAccessOperation();
    when(c.prepareStatement(anyString())).thenReturn(stmt1);
    when(stmt1.executeUpdate()).thenReturn(1);

    assertTrue(reg.registerUser(user, c));
  }
}