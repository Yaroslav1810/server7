package org.example.te.util;

import org.example.te.Objects.*;
import org.example.te.enums.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Класс для управления коллекцией объектов типа Person.
 * Предоставляет методы для загрузки и сохранения коллекции в файл, а также для выполнения различных команд.
 */
public class CollectionManager {

    /** Коллекция людей, хранящаяся в виде LinkedHashMap */
    private static ConcurrentMap<Long, Person> people = new ConcurrentHashMap<>();
    private static LocalDate creationDate = LocalDate.now();

    public class DBConnection {
        private static final String URL = "jdbc:postgresql://pg:5432/studs";
        private static final String USER = "s465235";
        private static final String PASSWORD = "313LHPUN6vkbHwyu";

        static {
            try {
                Class.forName("org.postgresql.Driver"); // Загрузка драйвера
            } catch (ClassNotFoundException e) {
                System.err.println("PostgreSQL JDBC Driver not found.");
            }
        }

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        Person p = new Person();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setCoordinates(new Coordinates(rs.getLong("x"), rs.getInt("y")));
        p.setHeight(rs.getFloat("height"));
        p.setCreationDate(rs.getDate("creation_date").toLocalDate());

        String eye = rs.getString("eye_color");
        String hair = rs.getString("hair_color");
        if (eye != null) p.setEyeColor(Eye.valueOf(eye));
        if (hair != null) p.setHairColor(Hair.valueOf(hair));
        p.setNationality(Country.valueOf(rs.getString("nationality")));

        p.setLocation(new Location(
                rs.getLong("loc_x"),
                rs.getDouble("loc_y"),
                rs.getString("loc_name")
        ));

        p.setOwner(rs.getString("owner"));

        return p;
    }

    public void loadFromDatabase() {
        people.clear();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM persons")) {

            int added = 0;
            int skipped = 0;

            while (rs.next()) {
                Person p = mapResultSetToPerson(rs);
                if (isValidPerson(p)) {
                    people.put(p.getId(), p);
                    added++;
                } else {
                    skipped++;
                }
            }

            Response.addMessage("Загрузка из БД выполнена. Загружено: " + added + ", пропущено: " + skipped);

        } catch (SQLException e) {
            Response.addMessage("Ошибка при загрузке из БД: " + e.getMessage());
        }
    }




    public static LocalDate getCreationDate() {
        return creationDate;
    }

    private boolean isValidPerson(Person p) {
        if (p == null) return false;

        try {
            if (p.getId() == null || p.getId() <= 0) return false;
            if (p.getName() == null || p.getName().isBlank()) return false;

            Coordinates coords = p.getCoordinates();
            if (coords == null || coords.getX() == null) return false;

            if (p.getHeight() != null && p.getHeight() <= 0) return false;

            if (p.getNationality() == null) return false;

            Location loc = p.getLocation();
            if (loc == null ||
                    loc.getX() == null ||
                    loc.getY() == null ||
                    loc.getName() == null || loc.getName().trim().isEmpty()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }








    /**
     * Метод для получения коллекции людей.
     *
     * @return Возвращает коллекцию людей, хранящуюся в LinkedHashMap.
     */
    public static Map<Long, Person> getPeople() {
        return people;
    }
}
