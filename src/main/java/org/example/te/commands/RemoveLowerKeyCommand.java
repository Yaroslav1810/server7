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
 * Команда для удаления элементов с ключом меньше указанного.
 * Удаляет только элементы, принадлежащие текущему пользователю.
 */
public class RemoveLowerKeyCommand extends Command {

    @Override
    public void execute(String[] args) {
        Response.addMessage("Ошибка: команда 'remove_lower_key' требует авторизации.");
    }

    @Override
    public void executeWithObject(Object obj) {
        Response.addMessage("Ошибка: команда 'remove_lower_key' требует авторизации.");
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (login == null || login.isBlank()) {
            Response.addMessage("Ошибка: вы не авторизованы.");
            return;
        }

        if (!(obj instanceof String[] args) || args.length == 0) {
            Response.addMessage("Ошибка: команда требует один числовой аргумент (ключ).");
            return;
        }

        long key;
        try {
            key = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            Response.addMessage("Ошибка: ключ должен быть числом.");
            return;
        }

        ConcurrentMap<Long, Person> people = (ConcurrentMap<Long, Person>) CollectionManager.getPeople();

        // Находим ключи, которые меньше переданного и принадлежат пользователю
        Set<Long> keysToRemove = people.entrySet().stream()
                .filter(entry -> entry.getKey() < key)
                .filter(entry -> login.equals(entry.getValue().getOwner()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());

        if (keysToRemove.isEmpty()) {
            Response.addMessage("Нет ваших элементов с ключом меньше " + key + ".");
            return;
        }

        // Удаляем из БД
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
            Response.addMessage("Ошибка при удалении из БД: " + e.getMessage());
            return;
        }

        // Удаляем из памяти
        keysToRemove.forEach(people::remove);

        Response.addMessage("Удалено элементов с ключом меньше " + key + ": " + keysToRemove.size());
    }

    @Override
    public int getArguments() {
        return 1;
    }
}
