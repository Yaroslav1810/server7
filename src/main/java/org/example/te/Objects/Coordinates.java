package org.example.te.Objects;

import org.example.te.util.Response;

import java.io.Serializable;

/**
 * Класс представляет координаты объекта.
 * Используется для хранения положения объекта в пространстве.
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1;


    /** Координата X (не может быть null) */
    private Long x;

    /** Координата Y (не может быть null) */
    private int y;

    /**
     * Конструктор для создания объекта Coordinates.
     * Проверяет, чтобы координата X не была null.
     *
     * @param x Координата X (не может быть null)
     * @param y Координата Y
     */
    public Coordinates(Long x, int y) {
        setX(x);
        this.y = y;
    }

    public Coordinates() {
    }

    /**
     * Устанавливает координату X.
     * Проверяет, чтобы значение X не было null.
     *
     * @param x Координата X (не может быть null)
     */
    public void setX(Long x) {
        if (x == null) {
            // Добавляем сообщение об ошибке в Response
            String errorMessage = "Координата X не может быть null";
            Response.addMessage(errorMessage);  // Добавляем сообщение в Response
            throw new IllegalArgumentException(errorMessage);  // Выбрасываем исключение
        }
        this.x = x;
    }

    /**
     * Возвращает координату X.
     *
     * @return Значение X.
     */
    public Long getX() {
        return x;
    }

    /**
     * Устанавливает координату Y.
     *
     * @param y Координата Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Возвращает координату Y.
     *
     * @return Значение Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Возвращает строковое представление объекта Coordinates.
     *
     * @return Строка, содержащая значения X и Y.
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
