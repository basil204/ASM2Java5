package com.example.ASM2.storage;

import com.example.ASM2.model.Users;

public class SessionManager {

  private static boolean isLogin = false;
  private static Users users = null;

  public static void login(Users users) {
    SessionManager.isLogin = true;
    SessionManager.users = users;
  }

  public static Users getUsers() {
    return SessionManager.users;
  }

  public static boolean isLogin() {
    return SessionManager.isLogin;
  }

  public static void logout() {
    SessionManager.isLogin = false;
    SessionManager.users = null;
  }
}
