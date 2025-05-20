package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Команда для удаления всех элементов с указанной высотой, принадлежащих текущему пользователю.
 */
public class RemoveAllByHeightCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (!(obj instanceof String[] args) || args.length == 0) {
            Response.addMessage("Ошибка: необходимо указать высоту.");
            return;
        }

        Float height;
        try {
            height = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            Response.addMessage("Ошибка: неверный формат высоты.");
            return;
        }

        // Удаление из базы данных
        String sql = "DELETE FROM persons WHERE height = ? AND owner = ?";
        int dbDeleted = 0;

        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setFloat(1, height);
            ps.setString(2, login);
            dbDeleted = ps.executeUpdate();

        } catch (SQLException e) {
            Response.addMessage("Ошибка при удалении из БД: " + e.getMessage());
            return;
        }

        // Удаление из коллекции в памяти
        ConcurrentMap<Long, Person> people = (ConcurrentMap<Long, Person>) CollectionManager.getPeople();

        Set<Long> keysToRemove = people.entrySet().stream()
                .filter(entry -> {
                    Person p = entry.getValue();
                    return login.equals(p.getOwner())
                            && p.getHeight() != null
                            && p.getHeight().equals(height);
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        keysToRemove.forEach(people::remove);

        Response.addMessage("Удалено объектов с высотой " + height + ": " +
                keysToRemove.size() + " (из БД: " + dbDeleted + ")");
    }

    @Override
    public int getArguments() {
        return 1;
    }
}
