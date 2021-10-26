package com.jpettit.jobapplicationtrackerbackend.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    private final Integer roundsOfEncryption;

    private PasswordEncoder(Integer newRoundsOfEncryption) {
        if (newRoundsOfEncryption == null) {
            this.roundsOfEncryption = 12;
        } else {
            this.roundsOfEncryption = newRoundsOfEncryption;
        }
    }

    public static PasswordEncoder createPasswordEncoder(Integer strength) {
        return new PasswordEncoder(strength);
    }

    public String hashPassword(String rawPassword) {
        final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(roundsOfEncryption);

        return ENCODER.encode(rawPassword);
    }

    public boolean comparePassword(String rawPassword, String hashedPassword) {
        final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(roundsOfEncryption);

        return ENCODER.matches(rawPassword, hashedPassword);
    }
}
