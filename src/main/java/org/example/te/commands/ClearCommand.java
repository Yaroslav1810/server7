package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ClearCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {

        // Удаляем из БД
        String sql = "DELETE FROM persons WHERE owner = ?";

        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            int deleted = ps.executeUpdate();

            // Удаляем из памяти
            ConcurrentMap<Long, Person> people = (ConcurrentMap<Long, Person>) CollectionManager.getPeople();
            people.entrySet().removeIf(entry -> login.equals(entry.getValue().getOwner()));

            Response.addMessage("Удалено ваших объектов: " + deleted);

        } catch (SQLException e) {
            Response.addMessage("Ошибка при удалении из БД: " + e.getMessage());
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
