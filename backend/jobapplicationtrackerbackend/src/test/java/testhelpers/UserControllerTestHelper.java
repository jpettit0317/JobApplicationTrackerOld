package testhelpers;

import com.jpettit.jobapplicationtrackerbackend.helpers.UserControllerURL;
import com.jpettit.jobapplicationtrackerbackend.models.Login;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;

public class UserControllerTestHelper<Object> {
    private Object actualValue;
    private Object EXPECTED;
    private MvcResult mvcResult;

    public UserControllerTestHelper() {}

    public UserControllerTestHelper(final Object ACTUAL, final Object EXP) {
        setActualValue(ACTUAL);
        setExpectedValue(EXP);
    }

    public void setActualAndExpected(final Object EXPECTED, final Object ACTUAL) {
        setExpectedValue(EXPECTED);
        setActualValue(ACTUAL);
    }

    public void verifyEqual() {
        final String ERR_MSG = getStandardErrorMessage();

        Assertions.assertEquals(EXPECTED, actualValue, ERR_MSG);
    }

    public String getStandardErrorMessage() {
        final String ACTUAL_STR = convertToString(actualValue);
        final String EXP_STR = convertToString(EXPECTED);

        return String.format("Expected %s, got %s instead.", EXP_STR, ACTUAL_STR);
    }

    public void buildResultForLoginUser(final MockMvc MVC, final Login LOGIN, final HttpStatus STATUS) throws Exception {
        if (STATUS == HttpStatus.OK) {
            this.mvcResult = buildLoginUserSuccess(MVC, LOGIN);
        } else if (STATUS == HttpStatus.NOT_FOUND) {
            this.mvcResult = buildLoginUserError(MVC, LOGIN);
        } else {
            final String ERROR = String.format("Error: %d not supported", STATUS.value());
            throw new Exception(ERROR);
        }
    }

    private MvcResult buildLoginUserSuccess(final MockMvc MVC, final Login LOGIN) throws Exception {
        final String LOGIN_JSON = JSONHelper.convertJSONLoginToString(LOGIN);
        final String CHAR_ENCODING = "UTF-8";

        return MVC.perform(MockMvcRequestBuilders.post(UserControllerURL.loginUser)
                        .content(LOGIN_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(CHAR_ENCODING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private MvcResult buildLoginUserError(final MockMvc MVC, final Login LOGIN) throws Exception {
        final String LOGIN_JSON = JSONHelper.convertJSONLoginToString(LOGIN);
        final String CHAR_ENCODING = "UTF-8";

        return MVC.perform(MockMvcRequestBuilders.post(UserControllerURL.loginUser)
                        .content(LOGIN_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(CHAR_ENCODING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    public void buildResultForCreateUser(final MockMvc MVC, final User USER, final HttpStatus STATUS) throws Exception {
        if (STATUS == HttpStatus.CREATED) {
            mvcResult = buildCreateUserSuccess(MVC, USER);
        } else if (STATUS == HttpStatus.NOT_FOUND) {
            mvcResult = buildCreateUserError(MVC, USER);
        } else {
            final String ERR_MSG = String.format("Error: %d not supported.", STATUS.value());
            throw new Exception(ERR_MSG);
        }
    }

    public String getResponse() throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString();
    }

    private MvcResult buildCreateUserSuccess(final MockMvc MVC, final User USER) throws Exception {
        final String USER_JSON = JSONHelper.convertJSONUserToString(USER);
        final String CHAR_ENCODING = "UTF-8";

        return MVC.perform(MockMvcRequestBuilders.post(UserControllerURL.addUser)
                        .content(USER_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(CHAR_ENCODING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
    }

    private MvcResult buildCreateUserError(final MockMvc MVC, final User USER) throws Exception {
        final String USER_JSON = JSONHelper.convertJSONUserToString(USER);
        final String CHAR_ENCODING = "UTF-8";

        return MVC.perform(MockMvcRequestBuilders.post(UserControllerURL.addUser)
                        .content(USER_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(CHAR_ENCODING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    private String convertToString(final Object OBJ) {
        return OBJ.toString();
    }

    public void setActualValue(final Object ACTUAL) {
        this.actualValue = ACTUAL;
    }

    public void setExpectedValue(final Object EXPECTED) {
        this.EXPECTED = EXPECTED;
    }
}
