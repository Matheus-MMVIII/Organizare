package com.organizare.http.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.organizare.exception.BadRequestException;
import com.organizare.model.Clothing;
import com.organizare.model.User;
import com.organizare.model.Buy;

public final class JsonUtil {
    // Impede a instanciacao da classe utilitaria de serializacao e parse de JSON.
    private JsonUtil() {
    }

    // Converte um JSON plano em mapa de strings para facilitar validacao e uso nos
    // services.
    public static Map<String, String> parseFlatObject(String json) {
        if (json == null) {
            throw new BadRequestException("Corpo JSON obrigatorio.");
        }

        String trimmedJson = json.trim();
        if (trimmedJson.isEmpty()) {
            throw new BadRequestException("Corpo JSON obrigatorio.");
        }
        if (!trimmedJson.startsWith("{") || !trimmedJson.endsWith("}")) {
            throw new BadRequestException("JSON invalido.");
        }

        String content = trimmedJson.substring(1, trimmedJson.length() - 1).trim();
        Map<String, String> values = new LinkedHashMap<>();
        if (content.isEmpty()) {
            return values;
        }

        List<String> pairs = splitTopLevel(content);
        for (String pair : pairs) {
            int colonIndex = findColonOutsideQuotes(pair);
            if (colonIndex <= 0) {
                throw new BadRequestException("JSON invalido.");
            }

            String rawKey = pair.substring(0, colonIndex).trim();
            String rawValue = pair.substring(colonIndex + 1).trim();
            String key = parseQuotedString(rawKey);
            String value = parseValue(rawValue);
            values.put(key, value);
        }
        return values;
    }

    // Cria um objeto JSON simples com apenas uma chave e um valor de texto.
    public static String object(String key, String value) {
        return "{\"" + escape(key) + "\":\"" + escape(value) + "\"}";
    }

    // Cria um JSON padrao de erro usado pelas respostas da API.
    public static String error(String message) {
        return "{\"error\":\"" + escape(message) + "\"}";
    }

    // Serializa um usuario para o formato JSON esperado pelos clientes da API.
    public static String user(User user) {
        return "{"
                + "\"id\":" + user.getId() + ","
                + "\"name\":\"" + escape(user.getName()) + "\","
                + "\"email\":\"" + escape(user.getEmail()) + "\","
                + "\"cellPhone\":\"" + escape(user.getCellPhone()) + "\","
                + "\"birthday\":\"" + escape(user.getBirthdayIso()) + "\","
                + "\"birthMonth\":" + user.getBirthMonth() + ","
                + "\"birthDay\":" + user.getBirthDay() + ","
                + "\"createdAt\":\"" + escape(user.getCreatedAt().toString()) + "\","
                + "\"birthdayToday\":" + user.isBirthdayToday()
                + "}";
    }

    // Serializa uma lista de usuarios como array JSON.
    public static String users(List<User> users) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (User user : users) {
            joiner.add(user(user));
        }
        return joiner.toString();
    }

    // Serializa uma peca de roupa para o formato JSON esperado pelos clientes da
    // API.
    public static String clothing(Clothing clothing) {
        return "{"
                + "\"id\":" + clothing.getId() + ","
                + "\"name\":\"" + escape(clothing.getName()) + "\","
                + "\"price\":" + clothing.getPrice() + ","
                + "\"stock\":" + clothing.getStock() + ","
                + "\"size\":\"" + escape(clothing.getSizeAsString()) + "\","
                + "\"color\":\"" + escape(clothing.getColor()) + "\""
                + "}";
    }

    // Serializa uma lista de roupas como array JSON.
    public static String clothingItems(List<Clothing> clothingItems) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (Clothing clothing : clothingItems) {
            joiner.add(clothing(clothing));
        }
        return joiner.toString();
    }

    public static String buy(Buy buy) {
        return "{"
                + "\"id\":" + buy.getId() + ","
                + "\"userId\":" + buy.getUserId() + ","
                + "\"userName\":\"" + escapeNullable(buy.getUserName()) + "\","
                + "\"clothingId\":" + buy.getClothingId() + ","
                + "\"clothingName\":\"" + escapeNullable(buy.getClothingName()) + "\","
                + "\"quantity\":" + buy.getQuantity() + ","
                + "\"totalPrice\":" + buy.getTotalPrice() + ","
                + "\"orderDate\":\"" + escape(buy.getOrderDate().toString()) + "\""
                + "}";
    }

    public static String buyItems(List<Buy> buyItems) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (Buy buy : buyItems) {
            joiner.add(buy(buy));
        }
        return joiner.toString();
    }

    // Divide os pares de um objeto JSON sem quebrar valores que estejam entre
    // aspas.
    private static List<String> splitTopLevel(String content) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        boolean escaped = false;

        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);
            if (escaped) {
                current.append(currentChar);
                escaped = false;
                continue;
            }
            if (currentChar == '\\') {
                current.append(currentChar);
                escaped = true;
                continue;
            }
            if (currentChar == '"') {
                inQuotes = !inQuotes;
                current.append(currentChar);
                continue;
            }
            if (currentChar == ',' && !inQuotes) {
                parts.add(current.toString().trim());
                current.setLength(0);
                continue;
            }
            current.append(currentChar);
        }

        if (inQuotes || escaped) {
            throw new BadRequestException("JSON invalido.");
        }

        parts.add(current.toString().trim());
        return parts;
    }

    // Localiza o separador chave-valor ignorando dois pontos que estejam dentro de
    // strings.
    private static int findColonOutsideQuotes(String pair) {
        boolean inQuotes = false;
        boolean escaped = false;

        for (int i = 0; i < pair.length(); i++) {
            char currentChar = pair.charAt(i);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (currentChar == '\\') {
                escaped = true;
                continue;
            }
            if (currentChar == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (currentChar == ':' && !inQuotes) {
                return i;
            }
        }
        return -1;
    }

    // Remove as aspas externas de uma string JSON e converte sequencias escapadas
    // para texto normal.
    private static String parseQuotedString(String rawValue) {
        String trimmedValue = rawValue.trim();
        if (trimmedValue.length() < 2 || trimmedValue.charAt(0) != '"'
                || trimmedValue.charAt(trimmedValue.length() - 1) != '"') {
            throw new BadRequestException("JSON invalido.");
        }
        return unescape(trimmedValue.substring(1, trimmedValue.length() - 1));
    }

    // Interpreta um valor JSON simples aceitando strings, nulos, numeros e
    // booleanos.
    private static String parseValue(String rawValue) {
        String trimmedValue = rawValue.trim();
        if (trimmedValue.startsWith("\"")) {
            return parseQuotedString(trimmedValue);
        }
        if ("null".equals(trimmedValue)) {
            return null;
        }
        if (trimmedValue.matches("-?\\d+(\\.\\d+)?")) {
            return trimmedValue;
        }
        if ("true".equals(trimmedValue) || "false".equals(trimmedValue)) {
            return trimmedValue;
        }
        throw new BadRequestException("JSON invalido.");
    }

    // Escapa caracteres especiais para gerar JSON valido nas respostas.
    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private static String escapeNullable(String value) {
        return escape(value == null ? "" : value);
    }

    // Converte sequencias escapadas de uma string JSON para seus caracteres reais.
    private static String unescape(String value) {
        StringBuilder builder = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < value.length(); i++) {
            char currentChar = value.charAt(i);
            if (escaped) {
                switch (currentChar) {
                    case '"':
                    case '\\':
                    case '/':
                        builder.append(currentChar);
                        break;
                    case 'n':
                        builder.append('\n');
                        break;
                    case 'r':
                        builder.append('\r');
                        break;
                    case 't':
                        builder.append('\t');
                        break;
                    default:
                        throw new BadRequestException("JSON invalido.");
                }
                escaped = false;
                continue;
            }

            if (currentChar == '\\') {
                escaped = true;
            } else {
                builder.append(currentChar);
            }
        }

        if (escaped) {
            throw new BadRequestException("JSON invalido.");
        }

        return builder.toString();
    }
}
