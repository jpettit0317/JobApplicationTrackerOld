package testhelpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

public class PasswordEncoderTestHelperVars {
    public static final Integer DEFAULT_ROUNDS = 12;
    public static final String PASSWORD = "password";
    public static final String PASSWORD2 = "password2";

    public static String hashPassword(final Integer ROUNDS, String rawPassword) {
        final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(ROUNDS);

        return ENCODER.encode(rawPassword);
    }

    public static Integer createRounds(final int MIN, final int MAX) {
        Random r = new Random();

        if (MIN >= MAX) {
            return r.nextInt((10 - 1) + 1) + MIN;
        }

        return r.nextInt((MAX - MIN) + 1) + MIN;
    }
}
