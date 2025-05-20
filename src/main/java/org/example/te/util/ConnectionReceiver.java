package org.example.te.util;

import org.example.te.MessagePacket;
import org.example.te.commands.ExecuteCommand;
import org.example.te.validationModule.Command;
import org.example.te.validationModule.SaveCommand;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;

public class ConnectionReceiver {
    private static final int SERVER_PORT = 60000;
    private static final int BUFFER_SIZE = 65507;

    private static final ForkJoinPool readerPool = new ForkJoinPool();
    private static final ExecutorService processorPool = Executors.newCachedThreadPool();
    private static final ExecutorService senderPool = Executors.newFixedThreadPool(4);

    private static final Map<String, Command> availableCommands = new HashMap<>();

    static {
        availableCommands.put("save", new SaveCommand());
    }

    public static void startListening() {
        try (
                DatagramChannel channel = DatagramChannel.open();
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(SERVER_PORT));
            System.out.println("Сервер слушает на порту " + SERVER_PORT);

            while (true) {
                readerPool.execute(() -> handleClientRequest(channel));
                handleConsoleCommands(consoleReader);
            }

        } catch (IOException e) {
            System.out.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    private static void handleConsoleCommands(BufferedReader consoleReader) throws IOException {
        if (System.in.available() > 0) {
            String input = consoleReader.readLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Сервер завершает работу...");
                readerPool.shutdown();
                processorPool.shutdown();
                senderPool.shutdown();
                System.exit(0);
            }

            Command command = availableCommands.get(input);
            if (command != null) {
                String args = command.create();
                ExecuteCommand.execute(input + (args.isEmpty() ? "" : " " + args));
                Response.show();
            } else {
                System.out.println("Неизвестная команда: " + input);
            }
        }
    }

    private static void handleClientRequest(DatagramChannel channel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            SocketAddress clientAddress = channel.receive(buffer);

            if (clientAddress != null) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                InetSocketAddress inetAddress = (InetSocketAddress) clientAddress;
                InetAddress ip = inetAddress.getAddress();
                int port = inetAddress.getPort();

                processorPool.submit(() -> processMessagePacket(data, channel, ip, port));
            }

        } catch (IOException e) {
            System.out.println("Ошибка при приёме пакета: " + e.getMessage());
        }
    }

    private static void processMessagePacket(byte[] data, DatagramChannel channel, InetAddress ip, int port) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            MessagePacket request = (MessagePacket) ois.readObject();

            String commandLine = request.getMessages().isEmpty() ? null : request.getMessages().get(0);
            Object object = request.getObjects().isEmpty() ? null : request.getObjects().get(0);
            String[] user = request.getUser();

            System.out.println(">>> Получен запрос от пользователя: " + (user != null ? user[0] : "неизвестно"));
            System.out.println(">>> Полный текст команды: " + commandLine);
            System.out.println(">>> Приложенный объект: " + (object != null ? object.toString() : "отсутствует"));

            if (commandLine == null) {
                Response.addMessage("Ошибка: команда не распознана.");
            } else {
                String[] parts = commandLine.trim().split(" ", 2);
                String commandName = parts[0];
                String[] args = (parts.length > 1) ? parts[1].split(" ") : new String[]{};

                if (commandName.equals("login") || commandName.equals("register")) {
                    ExecuteCommand.execute(commandName, request); // авторизация
                } else if (object != null) {
                    ExecuteCommand.execute(commandName, object, user[0]);
                } else {
                    ExecuteCommand.execute(commandName, args, user[0]);
                }
            }

            MessagePacket response = Response.collectResponse();

            senderPool.submit(() -> {
                try {
                    Response.sendResponse(channel, ip, port, response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            System.out.println("Ошибка при обработке пакета: " + e.getMessage());
        }
    }
}
