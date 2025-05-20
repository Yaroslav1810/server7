package org.example.te.commands;

import org.example.te.util.Response;

/**
 * Команда для вывода справки по доступным командам.
 * Выводит список доступных команд и пояснения о правильном формате ввода.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {

        Response.addMessage("Доступные команды:");
        Response.addMessage("help : вывести справку по доступным командам");
        Response.addMessage("info : вывести информацию о коллекции");
        Response.addMessage("show : вывести все элементы коллекции");
        Response.addMessage("insert : добавить новый элемент с заданными значениями");
        Response.addMessage("update : обновить значение элемента по id");
        Response.addMessage("remove_key : удалить элемент по ключу");
        Response.addMessage("clear : очистить коллекцию (только свои объекты)");
        Response.addMessage("execute_script : выполнить команды из скрипта");
        Response.addMessage("exit : завершить клиент");
        Response.addMessage("remove_lower : удалить элементы меньше заданного");
        Response.addMessage("remove_greater_key : удалить элементы с ключом больше заданного");
        Response.addMessage("remove_lower_key : удалить элементы с ключом меньше заданного");
        Response.addMessage("remove_all_by_height : удалить элементы по height");
        Response.addMessage("average_of_height : вывести среднее значение height");
        Response.addMessage("print_unique_nationality : вывести уникальные значения nationality");
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
