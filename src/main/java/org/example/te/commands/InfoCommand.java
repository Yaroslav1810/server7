package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда для отображения информации о коллекции.
 * Эта команда выводит информацию о типе коллекции, её размере, дате инициализации,
 * а также статистику по пользователям-владельцам.
 */
public class InfoCommand extends Command {

    @Override
    public void execute(String[] args) {
        Response.addMessage("Ошибка: команда 'info' доступна только авторизованным пользователям.");
    }

    @Override
    public void executeWithObject(Object obj) {
        Response.addMessage("Ошибка: команда 'info' требует авторизации.");
    }

    @Override
    public void executeWithObject(Object obj, String login) {

        Map<Long, Person> people = CollectionManager.getPeople();

        String type = people.getClass().getName();
        long count = people.size();
        String date = String.valueOf(CollectionManager.getCreationDate());

        Response.addMessage("Информация о коллекции:");
        Response.addMessage("Тип коллекции: " + type);
        Response.addMessage("Размер коллекции: " + count);
        Response.addMessage("Дата инициализации: " + date);

        // Группировка по владельцам
        Map<String, Long> ownerStats = people.values().stream()
                .collect(Collectors.groupingBy(Person::getOwner, Collectors.counting()));

        if (ownerStats.isEmpty()) {
            Response.addMessage("Нет данных о владельцах.");
        } else {
            Response.addMessage("Пользователи, добавившие объекты:");
            ownerStats.forEach((owner, cnt) ->
                    Response.addMessage(" └ " + owner + " — " + cnt + " объект(ов)")
            );
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
