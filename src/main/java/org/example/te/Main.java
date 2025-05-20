package org.example.te;

import org.example.te.util.CollectionManager;
import org.example.te.util.ConnectionReceiver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 ssh -p 2222 s465235@se.ifmo.ru

 psql -h pg -d studs -U s465235

 CREATE TABLE users (
 login TEXT PRIMARY KEY,
 password_hash TEXT NOT NULL
 );

 CREATE TABLE persons (
 id SERIAL PRIMARY KEY,
 name TEXT NOT NULL,
 x BIGINT NOT NULL,
 y INTEGER NOT NULL,
 height REAL,
 creation_date DATE NOT NULL,
 eye_color TEXT,
 hair_color TEXT,
 nationality TEXT NOT NULL,
 loc_x BIGINT NOT NULL,
 loc_y DOUBLE PRECISION NOT NULL,
 loc_name TEXT NOT NULL,
 owner TEXT NOT NULL REFERENCES users(login) ON DELETE CASCADE
 );
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("Сервер запускается...");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://pg:5432/studs",
                "s465235",
                "313LHPUN6vkbHwyu"))
        {
            System.out.println("Подключение установлено!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }

        CollectionManager manager = new CollectionManager();

        // Загружаем коллекцию из БД
        manager.loadFromDatabase();

        // Запускаем сервер на прослушивание запросов
        ConnectionReceiver.startListening();
    }
}
