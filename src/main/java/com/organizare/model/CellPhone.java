package com.organizare.model;

public class CellPhone {
    private final String cellPhone;

    public CellPhone(String cellPhone) {
        if (!isValidCellPhone(cellPhone))
            throw new IllegalArgumentException("Número de celular inválido: " + cellPhone);

        this.cellPhone = cellPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    private boolean isValidCellPhone(String cellPhone) {
        return cellPhone != null && cellPhone.matches("\\(\\d{2}\\)\\s\\d{5}-\\d{4}");
    }
}