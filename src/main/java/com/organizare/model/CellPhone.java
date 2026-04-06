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
        if (cellPhone == null) {
            return false;
        }

        String trimmedCellPhone = cellPhone.trim();
        if (!trimmedCellPhone.matches("^\\+?[0-9()\\-\\s]{10,24}$")) {
            return false;
        }

        String localDigits = extractLocalDigits(trimmedCellPhone);
        return localDigits.length() == 10 || localDigits.length() == 11;
    }

    private String formatCellPhone(String cellPhone) {
        String digits = extractLocalDigits(cellPhone);
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

    private String extractLocalDigits(String cellPhone) {
        String digits = cellPhone.replaceAll("\\D", "");
        if (digits.startsWith("55") && (digits.length() == 12 || digits.length() == 13)) {
            return digits.substring(2);
        }
        return digits;
    }
}
