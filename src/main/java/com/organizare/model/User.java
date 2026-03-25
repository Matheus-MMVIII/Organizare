package com.organizare.model;

import java.time.LocalDate;

public class User {
    private final int id;
    private final String name;
    private final Email email;
    private final CellPhone cellPhone;
    private final int birthMonth;
    private final int birthDay;

    // Inicializa um usuario com seus dados principais e aniversario separado em mes e dia.
    public User(int id, String name, String email, String cellPhone, int birthMonth, int birthDay) {
        this.id = id;
        this.name = name;
        this.email = new Email(email);
        this.cellPhone = new CellPhone(cellPhone);
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }

    // Retorna o identificador unico do usuario.
    public int getId() {
        return id;
    }

    // Retorna o nome do usuario.
    public String getName() {
        return name;
    }

    // Retorna o email do usuario.
    public String getEmail() {
        return email.getEmail();
    }

    // Retorna o telefone celular do usuario.
    public String getCellPhone() {
        return cellPhone.getCellPhone();
    }

    // Retorna o mes do aniversario do usuario.
    public int getBirthMonth() {
        return birthMonth;
    }

    // Retorna o dia do aniversario do usuario.
    public int getBirthDay() {
        return birthDay;
    }

    // Verifica se a data atual coincide com o aniversario cadastrado do usuario.
    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue() == birthMonth && today.getDayOfMonth() == birthDay;
    }
}
