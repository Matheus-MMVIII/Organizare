package com.organizare.model;

import java.time.LocalDate;

public class User {
    private final int id;
    private final String name;
    private final Email email;
    private final CellPhone cellPhone;
    private final Birth birth;

    public User(int id, String name, String email, String cellPhone, int day, int month) {
        this.id = id;
        this.name = name;
        this.email = new Email(email);
        this.cellPhone = new CellPhone(cellPhone);
        this.birth = new Birth(day, month);
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

    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue() == birth.getMonth() && today.getDayOfMonth() == birth.getDay();
    }
}
