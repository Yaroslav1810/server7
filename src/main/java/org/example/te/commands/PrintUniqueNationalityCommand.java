package org.example.te.commands;

import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Команда для вывода уникальных значений поля nationality.
 * Проходит по всем людям в коллекции и отправляет клиенту список уникальных национальностей.
 */
public class PrintUniqueNationalityCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {


        Set<String> uniqueNationalities = CollectionManager.getPeople().values().stream()
                .map(person -> person.getNationality() != null ? person.getNationality().toString() : null)
                .filter(n -> n != null)
                .collect(Collectors.toSet());

        if (uniqueNationalities.isEmpty()) {
            Response.addMessage("В коллекции нет заданных национальностей.");
        } else {
            Response.addMessage("Уникальные национальности:");
            uniqueNationalities.forEach(Response::addMessage);
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
