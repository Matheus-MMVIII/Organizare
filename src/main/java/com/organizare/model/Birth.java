package com.organizare.model;

public class Birth {
    private final int day;
    private final int mounth;

    public Birth(int day, int mounth) {
        if (!isValidBirht(day, mounth))
            throw new IllegalArgumentException("Dia e ou Mês invalidos. dia: " + day + "mês: " + mounth);
        this.day = day;
        this.mounth = mounth;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return mounth;
    }

    private boolean isValidBirht(int day, int mounth) {
        return day > 0 && day <= 31 && mounth > 0 && mounth >= 12;
    }
}