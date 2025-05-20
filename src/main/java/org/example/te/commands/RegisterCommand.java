package org.example.te.commands;

import org.example.te.MessagePacket;
import org.example.te.util.Response;
import org.example.te.util.UserManager;

public class RegisterCommand extends Command {

    @Override
    public void execute(String[] args) {
    }

    @Override
    public void executeWithObject(Object obj) {
    }

    @Override
    public void executeWithObject(Object obj, String login) {
    }

    @Override
    public void execute(String[] args, MessagePacket packet) {
        String[] user = packet.getUser();

        if (user == null || user.length < 2) {
            Response.addMessage("Ошибка: логин или пароль не переданы.");
            return;
        }

        String login = user[0];
        String password = user[1];

        String result = UserManager.registerUser(login, password);
        Response.addMessage(result);
    }

    @Override
    public int getArguments() {
        return 0;
    }
}
