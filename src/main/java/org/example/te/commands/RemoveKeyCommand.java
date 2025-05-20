package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Команда для удаления элемента из коллекции по ключу.
 * Удаляет элемент с заданным ID, если он принадлежит текущему пользователю.
 */
public class RemoveKeyCommand extends Command {

    @Override
    public void execute(String[] args) {
        Response.addMessage("Ошибка: команда 'remove_key' требует авторизации.");
    }

    @Override
    public void executeWithObject(Object obj) {
        Response.addMessage("Ошибка: команда 'remove_key' требует авторизации.");
    }

    @Override
    public void executeWithObject(Object obj, String login) {

        if (!(obj instanceof String[] args) || args.length == 0) {
            Response.addMessage("Ошибка: необходимо указать ID.");
            return;
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            Response.addMessage("Ошибка: ID должен быть целым числом.");
            return;
        }

        Map<Long, Person> people = CollectionManager.getPeople();
        Person person = people.get(id);

        if (person == null) {
            Response.addMessage("Ошибка: объект с ID " + id + " не найден.");
            return;
        }

        if (!login.equals(person.getOwner())) {
            Response.addMessage("Ошибка: вы не являетесь владельцем объекта с ID " + id + ".");
            return;
        }

        // Удаляем из БД
        String sql = "DELETE FROM persons WHERE id = ? AND owner = ?";
        try (Connection conn = CollectionManager.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, login);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                Response.addMessage("Ошибка: объект не был удалён из БД.");
                return;
            }

        } catch (SQLException e) {
            Response.addMessage("Ошибка при удалении из БД: " + e.getMessage());
            return;
        }

        // Удаляем из памяти
        people.remove(id);
        Response.addMessage("Объект с ID " + id + " успешно удалён.");
    }

    @Override
    public int getArguments() {
        return 1;
    }
}
