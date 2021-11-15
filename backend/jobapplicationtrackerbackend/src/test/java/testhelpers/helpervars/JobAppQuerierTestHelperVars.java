package testhelpers.helpervars;

import com.jpettit.jobapplicationtrackerbackend.enums.ProjectEnvironment;

public class JobAppQuerierTestHelperVars {
    public static final ProjectEnvironment TEST_ENV = ProjectEnvironment.TEST;
    public static final  ProjectEnvironment DEV_ENV = ProjectEnvironment.DEV;
    public static final ProjectEnvironment PROD_ENV = ProjectEnvironment.PROD;

    public static final String INVALID_USERNAME = "Username is empty";
    private static final String INVALID_TABLENAME = "Table name is empty";

    public static final String USER1 = "user1";
}
