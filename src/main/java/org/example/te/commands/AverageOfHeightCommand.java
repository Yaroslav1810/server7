package org.example.te.commands;

import org.example.te.Objects.Person;
import org.example.te.util.CollectionManager;
import org.example.te.util.Response;

/**
 * Команда для вычисления среднего значения высоты всех элементов коллекции.
 */
public class AverageOfHeightCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
        double average = CollectionManager.getPeople().values().stream()
                .map(Person::getHeight)
                .filter(h -> h != null && h > 0)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(Double.NaN);

        if (Double.isNaN(average)) {
            Response.addMessage("Коллекция пуста или не содержит корректных значений высоты.");
        } else {
            Response.addMessage("Средняя высота: " + average);
        }
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
