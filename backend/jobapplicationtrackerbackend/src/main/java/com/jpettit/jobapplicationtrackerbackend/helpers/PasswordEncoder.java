package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.enums.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordEncoder {
    private Integer hashRounds;

    public PasswordEncoder(@Value(AppProperties.roundCount) Integer rounds) {
        setHashRounds(rounds);
    }

    public Integer getHashRounds() {
        return hashRounds;
    }

    public void setHashRounds(Integer hashRounds) {
        if (Optional.ofNullable(hashRounds).isPresent()) {
            this.hashRounds = hashRounds;
        } else {
            this.hashRounds = 12;
        }
    }

    public String hashPassword(String password) {
        final BCryptPasswordEncoder ENCODER = createBCryptEncoder();

        return ENCODER.encode(password);
    }

    public boolean comparePassword(String rawPassword, String hashedPassword) {
        final BCryptPasswordEncoder ENCODER = createBCryptEncoder();

        return ENCODER.matches(rawPassword, hashedPassword);
    }

    public BCryptPasswordEncoder createBCryptEncoder() {
        if (Optional.ofNullable(hashRounds).isPresent()) {
            return new BCryptPasswordEncoder(hashRounds);
        } else {
            return new BCryptPasswordEncoder(12);
        }
    }
}
