package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Команда для удаления всех элементов, которые меньше заданного объекта и принадлежат пользователю.
 */
public class RemoveLowerCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (!(obj instanceof Person input)) {
            Response.addMessage("Ошибка: переданный объект не является экземпляром Person.");
            return;
        }

        if (login == null || login.isBlank()) {
            Response.addMessage("Ошибка: вы не авторизованы.");
            return;
        }

        ConcurrentMap<Long, Person> people = (ConcurrentMap<Long, Person>) CollectionManager.getPeople();

        // Собираем ID элементов, которые меньше переданного и принадлежат пользователю
        Set<Long> idsToRemove = people.entrySet().stream()
                .filter(entry -> login.equals(entry.getValue().getOwner()))
                .filter(entry -> entry.getValue().compareTo(input) < 0)
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());

        if (idsToRemove.isEmpty()) {
            Response.addMessage("Нет ваших элементов, меньших заданного.");
            return;
        }

        // Удаление из базы данных
        String sql = "DELETE FROM persons WHERE id = ? AND owner = ?";
        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Long id : idsToRemove) {
                ps.setLong(1, id);
                ps.setString(2, login);
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            Response.addMessage("Ошибка при удалении из БД: " + e.getMessage());
            return;
        }

        idsToRemove.forEach(people::remove);

        Response.addMessage("Удалено ваших элементов: " + idsToRemove.size());
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
