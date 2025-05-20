package org.example.te;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessagePacket implements Serializable {
    private List<String> messages = new ArrayList<>();
    private List<Object> objects = new ArrayList<>();

    private String[] user; // ← обычное поле, НЕ static

    public void setUser(String[] user) {
        this.user = user;
    }

    public String[] getUser() {
        return user;
    }

    public void addMessage(String msg) {
        messages.add(msg);
    }

    public void addObject(Object obj) {
        objects.add(obj);
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<Object> getObjects() {
        return objects;
    }


}
