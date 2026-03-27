package com.organizare.model;

public class CellPhone {
    private final String cellPhone;

    public CellPhone(String cellPhone) {
        if (!isValidCellPhone(cellPhone))
            throw new IllegalArgumentException("Número de celular inválido: " + cellPhone);

        this.cellPhone = formatCellPhone(cellPhone);
    }

    public String getCellPhone() {
        return cellPhone;
    }

    private boolean isValidCellPhone(String cellPhone) {
        return cellPhone != null && cellPhone.matches(
                "(\\d{2}\\s?9?\\s?\\d{4}[\\s-]?\\d{4})" + // 11 9 9999 9999 / 11 9999-9999
                        "|(\\d{2}\\s?9?\\s?\\d{5}[\\s-]?\\d{4})" + // 11 9 99999 9999
                        "|(\\(\\d{2}\\)\\s?9?\\s?\\d{4,5}[\\s-]?\\d{4})" + // (11) 9 9999 9999
                        "|(\\d{10,11})" // 11999999999
        );// "\\(\\d{2}\\)\\s\\d{5}-\\d{4}");
    }

    private String formatCellPhone(String cellPhone) {
        String digits = cellPhone.replaceAll("\\D", "");
        if (digits.length() == 10) {
            return String.format("(%s) %s-%s",
                    digits.substring(0, 2),
                    digits.substring(2, 6),
                    digits.substring(6));
        } else if (digits.length() == 11) {
            return String.format("(%s) %s-%s",
                    digits.substring(0, 2),
                    digits.substring(2, 7),
                    digits.substring(7));
        }
        return cellPhone;
    }
}