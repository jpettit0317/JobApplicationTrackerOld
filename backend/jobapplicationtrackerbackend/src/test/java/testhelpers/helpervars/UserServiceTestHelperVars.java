package testhelpers.helpervars;

import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;

import java.time.LocalDate;
import java.util.Optional;

public class UserServiceTestHelperVars {
    public static final User user = User.createUser("u1", "e1",
            "p1",
            Session.createSession("invalidSession", LocalDate.of(2000, 1, 1)),
            0L);
    public static final User user2 = User.createUser("u2", "e2", "p2",
            Session.createSession("goodSession", LocalDate.of(2000, 1, 1)), 1L);
    public static final Optional<User> nullUser = Optional.empty();
    public static final Login login = Login.createLogin("u1", "p1");
    public static final Login nonExistantLogin = Login.createLogin("dude", "p5");
}
