package com.emergency.ambulance.common.utility;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final boolean encryptionEnabled;
    private final PasswordEncoder passwordEncoder;

    private static final String CHARACTERS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*";

    private static final SecureRandom RANDOM = new SecureRandom();

    public PasswordUtil(@Value("${password.encryption.enabled:true}") boolean encryptionEnabled,PasswordEncoder passwordEncoder) {
        this.encryptionEnabled = encryptionEnabled;
        this.passwordEncoder = passwordEncoder;
    }

    // Encodes the password only if encryption is enabled
    public String getEncodedPassword(String plainPassword) {

        if (!encryptionEnabled) {
            return plainPassword;
        }

        return passwordEncoder.encode(plainPassword);
    }

    //Matches the password based on the encryption flag.
    public boolean matches(String plainPassword, String storedPassword) {

        if (!encryptionEnabled) {
            return plainPassword.equals(storedPassword);
        }

        return passwordEncoder.matches(plainPassword, storedPassword);
    }

    //Generates a random password.
    public String generatePassword(int length) {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }

    //Returns whether encryption is enabled.
    public boolean isEncryptionEnabled() {
        return encryptionEnabled;
    }

}