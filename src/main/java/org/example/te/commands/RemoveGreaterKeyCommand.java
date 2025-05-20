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
 * Команда для удаления всех элементов, ключ которых больше указанного и принадлежат текущему пользователю.
 */
public class RemoveGreaterKeyCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (login == null || login.isBlank()) {
            Response.addMessage("Ошибка: вы не авторизованы.");
            return;
        }

        if (!(obj instanceof String[] args) || args.length == 0) {
            Response.addMessage("Ошибка: необходимо указать ключ.");
            return;
        }

        long key;
        try {
            key = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            Response.addMessage("Ошибка: ключ должен быть целым числом.");
            return;
        }

        ConcurrentMap<Long, Person> people = (ConcurrentMap<Long, Person>) CollectionManager.getPeople();

        // Ищем ключи, которые принадлежат пользователю и больше заданного
        Set<Long> keysToRemove = people.entrySet().stream()
                .filter(entry -> entry.getKey() > key && login.equals(entry.getValue().getOwner()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());

        if (keysToRemove.isEmpty()) {
            Response.addMessage("Нет ваших элементов с ключом больше " + key + ".");
            return;
        }

        // Удаление из базы данных
        String sql = "DELETE FROM persons WHERE id = ? AND owner = ?";
        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Long id : keysToRemove) {
                ps.setLong(1, id);
                ps.setString(2, login);
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            Response.addMessage("Ошибка при удалении из базы данных: " + e.getMessage());
            return;
        }

        // Удаление из памяти
        keysToRemove.forEach(people::remove);

        Response.addMessage("Удалено ваших объектов с ключом больше " + key + ": " + keysToRemove.size());
    }

    @Override
    public int getArguments() {
        return 1;
    }
}
