package com.jecfalo.palermus_api.core.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.Key;
import java.nio.charset.StandardCharsets;

@Converter
@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private final Key key;

    public AttributeEncryptor() {
        String secret = "PalermusSuperSecretKey2026!@#$%^";
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        byte[] validKey = new byte[32]; // 32 bytes for AES-256
        System.arraycopy(keyBytes, 0, validKey, 0, Math.min(keyBytes.length, 32));
        this.key = new SecretKeySpec(validKey, AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Error al encriptar el atributo", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return dbData;
        }
    }
}
