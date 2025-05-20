package org.example.te.commands;

import org.example.te.MessagePacket;
import org.example.te.util.Response;

import java.util.HashMap;
import java.util.Map;

public class ExecuteCommand {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("help", new HelpCommand());
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("insert", new InsertCommand());
        commands.put("update", new UpdateCommand());
        commands.put("remove_key", new RemoveKeyCommand());
        commands.put("clear", new ClearCommand());
        commands.put("remove_lower", new RemoveLowerCommand());
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand());
        commands.put("remove_lower_key", new RemoveLowerKeyCommand());
        commands.put("remove_all_by_height", new RemoveAllByHeightCommand());
        commands.put("average_of_height", new AverageOfHeightCommand());
        commands.put("print_unique_nationality", new PrintUniqueNationalityCommand());
        commands.put("login", new LoginCommand());
        commands.put("register", new RegisterCommand());
    }

    /**
     * Выполнение команды с обычными строковыми аргументами (используется только сервером).
     */
    public static void execute(String input) {
        String[] parts = input.trim().split(" ", 2);
        String commandName = parts[0];
        String[] args = (parts.length > 1) ? parts[1].split(" ") : new String[]{};

        Command command = commands.get(commandName);
        if (command != null) {
            if (args.length >= command.getArguments()) {
                command.execute(args);
            } else {
                Response.addMessage(String.format(
                        "Ошибка: команда '%s' ожидает %d аргумент(ов), получено %d.",
                        commandName, command.getArguments(), args.length));
            }
        } else {
            Response.addMessage("Неизвестная команда: " + commandName);
        }
    }

    /**
     * Выполнение команды с объектом и логином (универсальный способ).
     */
    public static void execute(String commandName, Object object, String login) {
        Command command = commands.get(commandName);
        if (command != null) {
            command.executeWithObject(object, login);
        } else {
            Response.addMessage("Неизвестная команда: " + commandName);
        }
    }

    /**
     * Выполнение команды авторизации (login/register), передаётся весь MessagePacket.
     */
    public static void execute(String commandName, MessagePacket packet) {
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(null, packet); // логин внутри пакета
        } else {
            Response.addMessage("Неизвестная команда: " + commandName);
        }
    }
}
