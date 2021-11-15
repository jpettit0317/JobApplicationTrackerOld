package com.jpettit.jobapplicationtrackerbackend.models;

import testhelpers.helpers.UserServiceUserPairHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class UserServiceUserPairTest {
    UserServiceUserPair sut;

    @AfterEach
    void tearDown() {
        destroySut();
    }

    private void destroySut() {
        sut = null;
    }

    @Test
    public void testUserDaoPairConstructor_isInitializedCorrectly() {
        Optional<User> user = Optional.of(UserServiceUserPairHelper.user);
        final String errorMessage = "";
        sut = UserServiceUserPair.createPair(user, errorMessage);

        UserServiceUserPairHelper.verifyDaoPairConstructor(sut, user, "");
    }

    @Test
    public void testUserDaoPairConstructor_whenInitializedWithEmpyOptional_shouldNotBePresent() {
        Optional<User> emptyUser = UserServiceUserPairHelper.nullUser;
        final String errorMessage = "";

        sut = UserServiceUserPair.createPair(emptyUser, errorMessage);

        UserServiceUserPairHelper.verifyUserPairHasAnEmptyUser(sut);
    }
}