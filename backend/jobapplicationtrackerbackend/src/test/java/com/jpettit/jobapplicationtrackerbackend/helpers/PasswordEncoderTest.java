package com.jpettit.jobapplicationtrackerbackend.helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testhelpers.helpers.PasswordEncoderTestHelper;
import testhelpers.helpervars.PasswordEncoderTestHelperVars;

class PasswordEncoderTest {
    private PasswordEncoder sut;

    @BeforeEach
    void setUp() {
        sut = new PasswordEncoder(null);
    }

    @AfterEach
    void tearDown() {
        sut = null;
    }

    @Test
    public void testGetHashRounds_whenInitializedWithNull_shouldSetItToTwelve() {
        final Integer EXPECTED = 12;
        final Integer ACTUAL = sut.getHashRounds();

        final PasswordEncoderTestHelper<Integer> HELPERS = new PasswordEncoderTestHelper<>(ACTUAL, EXPECTED);

        HELPERS.verifyValuesAreEqual();
    }

    @Test
    public void testGetHashRounds_whenInitializedWithAValue_shouldSetItToThatValue() {
        final Integer EXPECTED = PasswordEncoderTestHelperVars.createRounds(1, 100);
        sut.setHashRounds(EXPECTED);

        final Integer ACTUAL = sut.getHashRounds();

        final PasswordEncoderTestHelper<Integer> HELPERS = new PasswordEncoderTestHelper<>(ACTUAL, EXPECTED);

        HELPERS.verifyValuesAreEqual();
    }

    @Test
    public void testHashPassword_whenPassedInTwelveAndPassword_shouldSetItToThatInteger() {
        final String PASSWORD = PasswordEncoderTestHelperVars.PASSWORD;
        final String EXPECTED = "";
        final String ACTUAL = sut.hashPassword(PASSWORD);
        final PasswordEncoderTestHelper<String> HELPER = new PasswordEncoderTestHelper<>(ACTUAL, EXPECTED);

        HELPER.verifyValuesAreNotEqual();
    }
}