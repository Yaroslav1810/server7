package org.example.te.Objects;

import org.example.te.util.Response;

import java.io.Serializable;

/**
 * Класс представляет местоположение объекта.
 * Содержит координаты X и Y, а также название местоположения.
 */
public class Location implements Serializable {
    private static final long serialVersionUID = 1;

    /** Координата X местоположения (не может быть null) */
    private Long x;

    /** Координата Y местоположения (не может быть null) */
    private Double y;

    /** Название местоположения (не может быть пустым и не может быть null) */
    private String name;

    /**
     * Конструктор для создания объекта Location.
     *
     * @param x    Координата X местоположения (не может быть null)
     * @param y    Координата Y местоположения (не может быть null)
     * @param name Название местоположения (не может быть пустым)
     */
    public Location(Long x, Double y, String name) {
        setX(x);
        setY(y);
        setName(name);
    }

    public Location() {
    }

    /**
     * Устанавливает координату X местоположения.
     *
     * @param x Координата X (не может быть null)
     */
    public void setX(Long x) {
        if (x == null) {
            String errorMessage = "Координата X не может быть null";
            Response.addMessage(errorMessage);  // Добавляем сообщение в Response
            throw new IllegalArgumentException(errorMessage);  // Выбрасываем исключение
        }
        this.x = x;
    }

    /**
     * Устанавливает координату Y местоположения.
     *
     * @param y Координата Y (не может быть null)
     */
    public void setY(Double y) {
        if (y == null) {
            String errorMessage = "Координата Y не может быть null";
            Response.addMessage(errorMessage);  // Добавляем сообщение в Response
            throw new IllegalArgumentException(errorMessage);  // Выбрасываем исключение
        }
        this.y = y;
    }

    /**
     * Устанавливает название местоположения.
     *
     * @param name Название местоположения (не может быть пустым и не может быть null)
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            String errorMessage = "Название местоположения не может быть пустым или null";
            Response.addMessage(errorMessage);  // Добавляем сообщение в Response
            throw new IllegalArgumentException(errorMessage);  // Выбрасываем исключение
        }
        this.name = name;
    }

    /**
     * Возвращает координату X местоположения.
     *
     * @return координата X
     */
    public Long getX() {
        return x;
    }

    /**
     * Возвращает координату Y местоположения.
     *
     * @return координата Y
     */
    public Double getY() {
        return y;
    }

    /**
     * Возвращает название местоположения.
     *
     * @return название местоположения
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает строковое представление объекта Location.
     * Включает в себя координаты X и Y и название местоположения.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}
