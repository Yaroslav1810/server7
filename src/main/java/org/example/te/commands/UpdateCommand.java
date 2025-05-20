package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Команда для обновления данных существующего человека по его ID.
 * Объект приходит с клиента и используется для обновления.
 */
public class UpdateCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (!(obj instanceof Person newData)) {
            Response.addMessage("Ошибка: переданный объект не является экземпляром Person.");
            return;
        }

        Long id = newData.getId();
        if (id == null) {
            Response.addMessage("Ошибка: объект не содержит ID.");
            return;
        }

        ConcurrentMap<Long, Person> people = (ConcurrentMap<Long, Person>) CollectionManager.getPeople();
        Person existing = people.get(id);

        if (existing == null) {
            Response.addMessage("Ошибка: человек с ID " + id + " не найден.");
            return;
        }

        if (!login.equals(existing.getOwner())) {
            Response.addMessage("Ошибка: вы не являетесь владельцем этого объекта.");
            return;
        }

        // Обновляем в БД
        String sql = """
            UPDATE persons SET 
                name = ?, x = ?, y = ?, height = ?, creation_date = ?, 
                eye_color = ?, hair_color = ?, nationality = ?, 
                loc_x = ?, loc_y = ?, loc_name = ?
            WHERE id = ? AND owner = ?;
        """;

        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newData.getName());
            ps.setDouble(2, newData.getCoordinates().getX());
            ps.setDouble(3, newData.getCoordinates().getY());
            ps.setObject(4, newData.getHeight());
            ps.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(6, newData.getEyeColor() != null ? newData.getEyeColor().name() : null);
            ps.setString(7, newData.getHairColor() != null ? newData.getHairColor().name() : null);
            ps.setString(8, newData.getNationality().name());
            ps.setLong(9, newData.getLocation().getX());
            ps.setDouble(10, newData.getLocation().getY());
            ps.setString(11, newData.getLocation().getName());
            ps.setLong(12, id);
            ps.setString(13, login);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                Response.addMessage("Ошибка: объект не был обновлён в БД (возможно, он был удалён).");
                return;
            }

            // Обновляем в памяти
            newData.setOwner(login);
            newData.setCreationDate(LocalDate.now());
            people.put(id, newData);

            Response.addMessage("Объект с ID " + id + " успешно обновлён.");
            Response.addObject(newData);

        } catch (SQLException e) {
            Response.addMessage("Ошибка при обновлении в БД: " + e.getMessage());
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
