package org.example.te.commands;

import org.example.te.MessagePacket;

public abstract class Command {

    /** Вызов команды с обычными строковыми аргументами */
    public abstract void execute(String[] args);

    /** Вызов команды с объектом (например, Person) */
    public abstract void executeWithObject(Object obj);

    /** Вызов команды с объектом и логином пользователя */
    public abstract void executeWithObject(Object obj, String login);

    /** Вызов команды с аргументами и логином */
    public void execute(String[] args, String login) {
        // по умолчанию вызывает обычный execute, можно переопределить при необходимости
        execute(args);
    }

    /** Вызов команды с аргументами и полным пакетом (например, для login/register) */
    public void execute(String[] args, MessagePacket packet) {
        // по умолчанию вызывает обычный execute
        execute(args);
    }

    /** Сколько аргументов ожидает команда */
    public abstract int getArguments();
}