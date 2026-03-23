package com.organizare.model;

import java.time.LocalDate;

public class User {
    private final int id;
    private final String name;
    private final String email;
    private final String cellPhone;
    private final int birthMonth;
    private final int birthDay;

    public User(int id, String name, String email, String cellPhone, int birthMonth, int birthDay) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cellPhone = cellPhone;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue() == birthMonth && today.getDayOfMonth() == birthDay;
    }
}
