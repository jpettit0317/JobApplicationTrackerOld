package testhelpers.helpervars;

import com.jpettit.jobapplicationtrackerbackend.enums.ProjectEnvironment;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class UserDAOTestHelperVars {
    public static final String tableName = "testusers";
    public static final ProjectEnvironment testEnvironment = ProjectEnvironment.TEST;
    public static final User nonExistantUser = User.createUser("u1", "e1",
            "p1",
            Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)),
            3L);
    public static final User nonExistantUser2 = User.createUser("u3", "e3", "p3",
            Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)), 4L);
    public static final User newUser = User.createUser("u3", "e3", "p1",
            Session.createSession("s1", LocalDate.of(2000, 1, 1)), 4L);
    public static final Login login = Login.createLogin("u1", "p1");
    public static final int CALL_COUNT = 1;

    public static ArrayList<User> createTestUsers(User[] userToAdd) {
        ArrayList<User> users = new ArrayList<>();

        Collections.addAll(users, userToAdd);

        return users;
    }

    public static ArrayList<User> createDefaultTestUsers() {
        final Session[] sessions = {
                Session.createSession("first", LocalDate.of(2000, 1, 1)),
                Session.createSession("second", LocalDate.of(3000, 1, 1))
        };
        final User[] users = {
                User.createUser("u1", "e1", "p1", sessions[0], 1),
                User.createUser("u2", "e2", "p2", sessions[1], 2)
        };

        return createTestUsers(users);
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }
}
