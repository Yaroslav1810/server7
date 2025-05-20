package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.util.Comparator;

/**
 * Команда для вывода всех элементов коллекции, отсортированных по ID.
 */
public class ShowCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        if (CollectionManager.getPeople().isEmpty()) {
            Response.addMessage("Коллекция пуста.");
        } else {
            CollectionManager.getPeople().values().stream()
                    .sorted(Comparator.comparing(Person::getId))
                    .forEach(Response::addObject);

            Response.addMessage("Элементы коллекции успешно отправлены.");
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
