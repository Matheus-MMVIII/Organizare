package com.organizare.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private final int id;
    private final String name;
    private final Email email;
    private final CellPhone cellPhone;
    private final Birth birth;
    private final LocalDateTime createdAt;

    public User(int id, String name, String email, String cellPhone, int birthMonth, int birthDay) {
        this(id, name, email, cellPhone, birthMonth, birthDay, LocalDateTime.now());
    }

    public User(int id, String name, String email, String cellPhone, int birthMonth, int birthDay, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = new Email(email);
        this.cellPhone = new CellPhone(cellPhone);
        this.birth = new Birth(birthDay, birthMonth);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getCellPhone() {
        return cellPhone.getCellPhone();
    }

    public int getBirthDay() {
        return birth.getDay();
    }

    public int getBirthMonth() {
        return birth.getMonth();
    }

    public String getBirthdayIso() {
        return String.format("2000-%02d-%02d", getBirthMonth(), getBirthDay());
    }

    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue() == birth.getMonth() && today.getDayOfMonth() == birth.getDay();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
