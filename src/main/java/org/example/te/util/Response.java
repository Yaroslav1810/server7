package org.example.te.util;

import org.example.te.MessagePacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Response {

    private static final ThreadLocal<MessagePacket> localPacket =
            ThreadLocal.withInitial(MessagePacket::new);

    public static void addMessage(String message) {
        localPacket.get().addMessage(message);
    }

    public static void addObject(Object obj) {
        localPacket.get().addObject(obj);
    }

    public static MessagePacket collectResponse() {
        MessagePacket packet = localPacket.get();
        localPacket.remove();
        return packet;
    }

    public static void sendResponse(DatagramChannel channel, InetAddress address, int port, MessagePacket packet) throws IOException {
        if (packet.getMessages().isEmpty() && packet.getObjects().isEmpty()) return;

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(packet);
        }

        byte[] data = byteStream.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        InetSocketAddress clientAddress = new InetSocketAddress(address, port);
        channel.send(buffer, clientAddress);
    }

    public static void show() {
        MessagePacket packet = localPacket.get();
        if (packet.getMessages().isEmpty()) {
            System.out.println("Нет накопленных сообщений.");
        } else {
            System.out.println("Сообщения:\n");
            for (String msg : packet.getMessages()) {
                System.out.println(msg);
            }
        }
        localPacket.remove();
    }
}