package com.jecfalo.palermus_api.core.security;

public class DataMasker {

    public static String maskDocument(String document) {
        if (document == null || document.length() <= 4) {
            return "****";
        }
        return "*".repeat(document.length() - 4) + document.substring(document.length() - 4);
    }
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "****";
        }
        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];

        if (name.length() <= 2) {
            return "*".repeat(name.length()) + "@" + domain;
        }

        String maskedName = name.charAt(0) +
                "*".repeat(name.length() - 2) +
                name.charAt(name.length() - 1);

        return maskedName + "@" + domain;
    }
}
