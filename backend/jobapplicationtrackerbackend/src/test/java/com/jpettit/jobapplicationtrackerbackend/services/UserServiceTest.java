package com.jpettit.jobapplicationtrackerbackend.services;

import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceIntPair;
import com.jpettit.jobapplicationtrackerbackend.models.UserServiceUserPair;
import helpers.UserServiceHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import helpers.UserServiceUserPairHelper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private static UserService sut;

    @Mock
    private UserDAO userDAO;

//    @BeforeEach
//    void setUp() {
//
//    }

    @AfterAll
    private static void tearDown() {
        sut = null;
    }

    @Test
    public void testGetUserById_WhenValidIdIsPassedIn_UserShouldBeReturned() {
        final Long testId = 0L;
        final Optional<User> expectedUser = Optional.of(UserServiceUserPairHelper.user);

        Mockito.when(userDAO.getById(testId)).thenReturn(expectedUser);

        final UserServiceUserPair actualResult = sut.getUserById(testId);

        UserServiceUserPairHelper.verifyDaoPairConstructor(actualResult, expectedUser, "");
    }

    @Test
    public void testGetUserById_whenNonExistingIdIsPassedIn_shouldReturnEmptyUserAndCouldFindUser() {
        final Long badId = 1L;

        final Optional<User> expectedUser = Optional.empty();

        Mockito.when(userDAO.getById(badId)).thenReturn(Optional.empty());

        final UserServiceUserPair actualResult = sut.getUserById(badId);

        UserServiceUserPairHelper.verifyDaoPairConstructor(actualResult, expectedUser, "Couldn't find user.");
    }

    @Test
    public void testCreateUser_whenPassedInNonExistingUser_shouldReturnOneAndNoErrorMessage() {
        final Optional<User> user = Optional.of(UserServiceHelper.user);
        final UserServiceIntPair expectedPair = UserServiceIntPair.createPair(1, "");

        Mockito.when(userDAO.getByUsername(user.get().getUsername())).thenReturn(Optional.empty());
        Mockito.when(userDAO.insertOne(user.get())).thenReturn(1);

        final UserServiceIntPair pair = sut.createUser(user.get());

        UserServiceHelper.verifyUserServiceUserPair(pair, expectedPair);
    }

    @Test
    public void testCreateUser_whenPassedInExistingUser_shouldReturnZeroAndUserExists() {
        final Optional<User> user = Optional.of(UserServiceHelper.user);
        final UserServiceIntPair expectedPair = UserServiceIntPair.createPair(0, "User exists");

        Mockito.when(userDAO.getByUsername(user.get().getUsername())).thenReturn(user);

        final UserServiceIntPair pair = sut.createUser(user.get());

        UserServiceHelper.verifyUserServiceUserPair(pair, expectedPair);
    }
}