package org.example.te.util;

import java.security.MessageDigest;
import java.sql.*;

public class UserManager {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] digest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка хэширования", e);
        }
    }

    public static boolean authenticate(String login, String password) {
        String hash = hashPassword(password);
        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE login = ? AND password_hash = ?")) {
            stmt.setString(1, login);
            stmt.setString(2, hash);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Ошибка авторизации: " + e.getMessage());
            return false;
        }
    }

    public static String registerUser(String login, String password) {
        String hash = hashPassword(password);
        try (Connection conn = CollectionManager.DBConnection.getConnection()) {
            PreparedStatement check = conn.prepareStatement("SELECT login FROM users WHERE login = ?");
            check.setString(1, login);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return "Логин уже занят.";

            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users (login, password_hash) VALUES (?, ?)");
            insert.setString(1, login);
            insert.setString(2, hash);
            insert.executeUpdate();
            return "Регистрация успешна.";
        } catch (SQLException e) {
            return "Ошибка при регистрации: " + e.getMessage();
        }
    }
}
