package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.sql.*;

public class InsertCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (!(obj instanceof Person person)) {
            Response.addMessage("Ошибка: передан не объект Person.");
            return;
        }

        person.setCreationDate(java.time.LocalDate.now());
        person.setOwner(login);

        String sql = """
            INSERT INTO persons 
            (name, x, y, height, creation_date, eye_color, hair_color, nationality, loc_x, loc_y, loc_name, owner) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
        """;

        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, person.getName());
            ps.setDouble(2, person.getCoordinates().getX());
            ps.setDouble(3, person.getCoordinates().getY());
            ps.setObject(4, person.getHeight());
            ps.setDate(5, Date.valueOf(person.getCreationDate()));
            ps.setString(6, person.getEyeColor() != null ? person.getEyeColor().name() : null);
            ps.setString(7, person.getHairColor() != null ? person.getHairColor().name() : null);
            ps.setString(8, person.getNationality().name());
            ps.setLong(9, person.getLocation().getX());
            ps.setDouble(10, person.getLocation().getY());
            ps.setString(11, person.getLocation().getName());
            ps.setString(12, login);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                person.setId(id);
                CollectionManager.getPeople().put(id, person);

                Response.addMessage("Объект успешно добавлен. ID: " + id);
                Response.addObject(person);
            } else {
                Response.addMessage("Ошибка: база не вернула ID.");
            }

        } catch (SQLException e) {
            Response.addMessage("Ошибка при вставке: " + e.getMessage());
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
