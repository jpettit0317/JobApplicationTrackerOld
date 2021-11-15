package com.jpettit.jobapplicationtrackerbackend.services;

import com.jpettit.jobapplicationtrackerbackend.daos.UserDAO;
import com.jpettit.jobapplicationtrackerbackend.helpers.PasswordEncoder;
import com.jpettit.jobapplicationtrackerbackend.models.*;
import testhelpers.helpers.UserServiceHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import testhelpers.helpervars.UserServiceTestHelperVars;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
    @InjectMocks
    private static UserService sut;

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder encoder;

    @AfterAll
    private static void tearDown() {
        sut = null;
    }

    @Test
    public void testCreateUser_whenPassedInNonExistingUser_shouldReturnSessionIdAndNoErrorMessage() {
        final Optional<User> user = Optional.of(UserServiceTestHelperVars.user);
        final Session session = user.get().getSession();
        final String USER_PASSWORD = user.get().getPassword();

        Mockito.when(userDAO.getByUsername(user.get().getUsername())).thenReturn(Optional.empty());
        Mockito.when(userDAO.insertOne(Mockito.any(User.class))).thenReturn(1);

        final ResultPair<String> ACTUAL = sut.createUser(user.get());

        Assertions.assertEquals("", ACTUAL.getMessage());
        Assertions.assertNotEquals("", ACTUAL.getValue());
    }

    @Test
    public void testCreateUser_whenPassedInExistingUser_shouldReturnEmptyValueAndUserExists() {
        final Optional<User> user = Optional.of(UserServiceTestHelperVars.user);
        final String EXPECTED_ERROR_MSG = "User exists";

        Mockito.when(userDAO.getByUsername(user.get().getUsername())).thenReturn(user);

        final ResultPair<String> ACTUAL_PAIR = sut.createUser(user.get());

        Assertions.assertEquals("", ACTUAL_PAIR.getValue());
        Assertions.assertEquals(EXPECTED_ERROR_MSG, ACTUAL_PAIR.getMessage());
    }

    @Test
    public void testValidateUserLogin_whenPassedInNonExistingUser_shouldReturnEmptyValueAndErrorMessage() {
        final Login LOGIN = UserServiceTestHelperVars.nonExistantLogin;
        final ResultPair<String> pair = new ResultPair<>("", "Username or password doesn't exist");
        Mockito.when(userDAO.getPasswordForUser(LOGIN)).thenReturn(pair);

        final ResultPair<String> actualPair = sut.validateUserLogin(LOGIN);

        Assertions.assertEquals(actualPair.getMessage(), pair.getMessage());
        Assertions.assertEquals(actualPair.getValue(), pair.getValue());
    }

    @Test
    public void testValidateUserLogin_whenPassedInDifferentPasswordForUser_shouldReturnEmptyValueAndErrorMessage() {
       final Login LOGIN = UserServiceTestHelperVars.login;

       Mockito.when(userDAO.getPasswordForUser(LOGIN)).thenReturn(new ResultPair<>("manWord", ""));

       final ResultPair<String> actualPair = sut.validateUserLogin(LOGIN);
       final ResultPair<String> expectedPair = new ResultPair<>("", "Username or password doesn't match");

        Assertions.assertEquals(actualPair.getMessage(), expectedPair.getMessage());
        Assertions.assertEquals(actualPair.getValue(), expectedPair.getValue());
    }

    @Test
    public void testValidateUserLogin_whenPassedInExistingLogin_shouldReturnSessionNameAndNoErrorMessage() {
        final Login LOGIN = UserServiceTestHelperVars.login;

        Mockito.when(userDAO.getPasswordForUser(LOGIN))
                .thenReturn(new ResultPair<>(LOGIN.getPassword(), ""));
        Mockito.when(encoder.comparePassword(Mockito.eq(LOGIN.getPassword()), Mockito.anyString()))
                        .thenReturn(true);
        Mockito.when(userDAO.updateSession(Mockito.eq(LOGIN.getUsername()), Mockito.any(Session.class))).thenReturn(1);

        final ResultPair<String> ACTUAL_PAIR = sut.validateUserLogin(LOGIN);

        Assertions.assertEquals("", ACTUAL_PAIR.getMessage());
        Assertions.assertNotEquals("", ACTUAL_PAIR.getValue());
    }

    @Test
    public void testValidateUserLogin_whenPassedInLoginAndCantUpdateSession_shouldReturnNoValueAndCouldNotUpdateSession() {
        final Login LOGIN = UserServiceTestHelperVars.login;

        Mockito.when(userDAO.getPasswordForUser(LOGIN))
                .thenReturn(new ResultPair<>(LOGIN.getPassword(), ""));
        Mockito.when(encoder.comparePassword(Mockito.eq(LOGIN.getPassword()),
                Mockito.anyString())).thenReturn(true);
        Mockito.when(userDAO.updateSession(Mockito.eq(LOGIN.getUsername()), Mockito.any(Session.class)))
                .thenReturn(0);

        final ResultPair<String> ACTUAL_PAIR = sut.validateUserLogin(LOGIN);
        final String ERROR_MESSAGE = "Couldn't update session";

        Assertions.assertEquals(ERROR_MESSAGE, ACTUAL_PAIR.getMessage());
        Assertions.assertEquals("", ACTUAL_PAIR.getValue());
    }

    @Test
    public void testGetUsernameBySessionId_whenPassedInValidSessionId_shouldReturnAssociatedUsernameAndNoErrorMessage() {
        final User USER = UserServiceTestHelperVars.user;
        final String SESSION_ID = USER.getSession().getSessionName();
        final ResultPair<String> EXPECTED_PAIR = new ResultPair<>(USER.getUsername(), "");

        Mockito.when(userDAO.getUsernameBySessionId(Mockito.anyString())).thenReturn(EXPECTED_PAIR);

        final ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);

        UserServiceHelper.assertThatStringResultPairsAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testGetUsernameBySessionId_whenPassedEmptySessionId_shouldReturnNoUsernameAndEmptySessionIdErrorMessage() {
        final User USER = UserServiceTestHelperVars.user;
        final String SESSION_ID = "";
        final ResultPair<String> EXPECTED_PAIR = new ResultPair<>("", userDAO.EMPTY_SESSIONID);

        Mockito.when(userDAO.getUsernameBySessionId(SESSION_ID)).thenReturn(EXPECTED_PAIR);

        final ResultPair<String> ACTUAL_PAIR = sut.getUsernameBySessionId(SESSION_ID);

        UserServiceHelper.assertThatStringResultPairsAreEqual(ACTUAL_PAIR, EXPECTED_PAIR);
    }

    @Test
    public void testHasSessionExpired_whenPassedInAccessDateOfDec201999AndSessionExpDateIsJan12000_shouldReturnFalse() {
        final Session SESSION = UserServiceTestHelperVars.user.getSession();
        final ResultPair<Optional<LocalDate>> EXPECTED_PAIR = new ResultPair<>(Optional.of(SESSION.getExpirationDate()), "");
        final LocalDate DEC20_1999 = LocalDate.of(1999, 12, 20);

        Mockito.when(userDAO.getSessionExpDateBySessionId(SESSION.getSessionName())).thenReturn(EXPECTED_PAIR);

        final ResultPair<Boolean> ACTUAL = sut.hasSessionExpired(DEC20_1999, SESSION.getSessionName());

        UserServiceHelper.assertSessionHasNotExpired(ACTUAL, DEC20_1999, EXPECTED_PAIR.getValue().get());
    }

    @Test
    public void testHasSessionExpired_whenPassedInAccessDateOfJan2_2000AndSessionExpDateIsJan1_2000_shouldReturnFalse() {
        final Session SESSION = UserServiceTestHelperVars.user.getSession();
        final ResultPair<Optional<LocalDate>> EXPECTED_PAIR = new ResultPair<>(Optional.of(SESSION.getExpirationDate()), "");
        final LocalDate JAN_2_2000 = LocalDate.of(2000, 1, 2);

        Mockito.when(userDAO.getSessionExpDateBySessionId(SESSION.getSessionName())).thenReturn(EXPECTED_PAIR);

        final ResultPair<Boolean> ACTUAL = sut.hasSessionExpired(JAN_2_2000, SESSION.getSessionName());

        UserServiceHelper.assertSessionHasExpired(ACTUAL, JAN_2_2000, EXPECTED_PAIR.getValue().get());
    }

    @Test
    public void testHasSessionExpired_whenSessionCantBeFound_shouldReturnTrueAndCantFindSessionWithThatId() {
        final ResultPair<Optional<LocalDate>> INVALID_PAIR = new ResultPair<>(Optional.empty(), sut.SESSION_NOT_FOUND);
        final Session SESSION = UserServiceTestHelperVars.user.getSession();

        Mockito.when(userDAO.getSessionExpDateBySessionId(Mockito.anyString())).thenReturn(INVALID_PAIR);

        final ResultPair<Boolean> ACTUAL = sut.hasSessionExpired(SESSION.getExpirationDate(), SESSION.getSessionName());
        final ResultPair<Boolean> EXPECTED = new ResultPair<>(true, sut.SESSION_NOT_FOUND);

        UserServiceHelper.assertHasSessionExpiredPair(ACTUAL, EXPECTED);
    }
}