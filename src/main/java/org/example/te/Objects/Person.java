package org.example.te.Objects;

import org.example.te.enums.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс представляет объект человека с различными характеристиками.
 * Включает в себя обязательные поля, а также поддерживает автоматическую генерацию значений date и id.
 */
public class Person implements Comparable<Person>, Serializable {
    private static final long serialVersionUID = 1;

    private Long id;
    private String name;
    private Coordinates coordinates;
    private Float height;
    private Eye eyeColor;
    private Hair hairColor;
    private Country nationality;
    private Location location;
    private LocalDate creationDate;

    private String owner; // логин пользователя-владельца

    public Person(Long id, String name, Coordinates coordinates, Float height,
                  Eye eyeColor, Hair hairColor, Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public Person() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Float getHeight() {
        return height;
    }

    public Eye getEyeColor() {
        return eyeColor;
    }

    public Hair getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        this.name = name;
    }

    public void setHeight(Float height) {
        if (height != null && height <= 0) {
            throw new IllegalArgumentException("Рост должен быть больше 0");
        }
        this.height = height;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public void setEyeColor(Eye eyeColor) {
        this.eyeColor = eyeColor;
    }

    public void setHairColor(Hair hairColor) {
        this.hairColor = hairColor;
    }

    public void setNationality(Country nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Национальность не может быть null");
        }
        this.nationality = nationality;
    }

    public void setLocation(Location location) {
        if (location == null || location.getName() == null || location.getName().isBlank()) {
            throw new IllegalArgumentException("Местоположение не может быть null или без названия");
        }
        this.location = location;
    }

    public void setCreationDate(LocalDate now) {
        this.creationDate = now;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", height=" + height +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                ", owner='" + owner + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Person o) {
        if (this.height == null && o.height == null) return 0;
        if (this.height == null) return -1;
        if (o.height == null) return 1;
        return Float.compare(this.height, o.height);
    }
}
