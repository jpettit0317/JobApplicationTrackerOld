package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.ResultPair;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class JobApplicationControllerTestHelper {
    public static final User USER = User.createUser(
            "u1", "u1@email.com", "p1",
            Session.createSession("session1", LocalDate.now()), 1L);

    public static ArrayList<JobApplicationCard> getDefaultCards() {
        final JobApplicationCard[] cardsArray =  {
                createCard("C Dev", "EJ", 1, "1"),
                createCard("iOS Dev", "EF", 2, "2")
        };
        ArrayList<JobApplicationCard> cards = new ArrayList<>();

        Collections.addAll(cards, cardsArray);

        return cards;
    }

    public static ArrayList<JobApplicationCard> getEmptyCardList() {
        return new ArrayList<>();
    }

    private static JobApplicationCard createCard(final String TITLE,
                                                 final String COMPANY,
                                                 final Integer COUNT,
                                                 final String ID) {
        return JobApplicationCard.createCard(TITLE,
                COMPANY,
                COUNT,
                ID);
    }

    public static JSONArray convertCardsToJson(final ArrayList<JobApplicationCard> CARDS) {
        JSONArray cardsAsJson = new JSONArray();
        for (JobApplicationCard card : CARDS) {
            cardsAsJson.put(card);
        }

        return cardsAsJson;
    }

    public static JSONObject getValuesFromJson(final String JSON_RESPONSE) throws JSONException {
        return new JSONObject(JSON_RESPONSE);
    }

    public static void assertJobAppCardsAreEqual(final ArrayList<JobApplicationCard> ACTUAL,
                                            final ArrayList<JobApplicationCard> EXPECTED) {
        final String ERR_MSG = String.format("Expected size of %d for %s, got size of %d for %s",
                EXPECTED.size(), EXPECTED.toString(), ACTUAL.size(), ACTUAL.toString());

        Assertions.assertEquals(EXPECTED.size(), ACTUAL.size(), ERR_MSG);

        for (int i = 0; i < ACTUAL.size(); i++) {
            final String OBJ_ERR_MSG = String.format("Expected %s, got %s instead.",
                    EXPECTED.get(i).toString(), ACTUAL.get(i).toString());
            final JobApplicationCard ACTUAL_CARD =  ACTUAL.get(i);
            final JobApplicationCard EXPECTED_CARD = EXPECTED.get(i);

            Assertions.assertEquals(EXPECTED_CARD, ACTUAL_CARD, OBJ_ERR_MSG);
        }
    }

    public static void assertErrorMessagesAreEqual(final String EXPECTED, final String ACTUAL) {
        final String ERR_MSG = String.format("Expected %s, got %s instead.",
                EXPECTED, ACTUAL);

        Assertions.assertEquals(EXPECTED, ACTUAL, ERR_MSG);
    }

    public static void assertResultsAreEqual(ResultPair<ArrayList<JobApplicationCard>> pair, final String JSON_RESP) {
        try {
            JSONObject object = getValuesFromJson(JSON_RESP);
            final JSONArray arr = object.getJSONArray("value");
            final String message = object.getString("message");
            final ResultPair<ArrayList<JobApplicationCard>> ACTUAL_PAIR = getResultPair(message, arr);

            assertErrorMessagesAreEqual(pair.getMessage(), ACTUAL_PAIR.getMessage());
            assertJobAppCardsAreEqual(ACTUAL_PAIR.getValue(), pair.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
            Assertions.fail(String.format("Error: %s", e.getMessage()));
        }
    }

    public static ResultPair<ArrayList<JobApplicationCard>> getResultPair(final String MSG,
                                                                          final JSONArray ARR) throws JSONException {
        final ArrayList<JobApplicationCard> CARDS = convertJSONArrayToCards(ARR);

        return new ResultPair<>(CARDS, MSG);
    }
    public static ArrayList<JobApplicationCard> convertJSONArrayToCards(JSONArray jsonArray) throws JSONException {
        ArrayList<JobApplicationCard> cards = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject obj = jsonArray.getJSONObject(i);
            cards.add(convertJSONObjectToCard(obj));
        }

        return cards;
    }

    public static JobApplicationCard convertJSONObjectToCard(JSONObject obj) throws JSONException {
        final String jobTitle = obj.getString("jobTitle");
        final String companyName = obj.getString("companyName");
        final Integer interviewCount = obj.getInt("interviewCount");
        final String jobAppId = obj.getString("jobAppId");

        return JobApplicationCard.createCard(
                jobTitle, companyName, interviewCount, jobAppId
        );
    }

    public static ArrayList<Integer> initCalledTimesRepeating(final int SIZE, final int VALUE) {
        return new ArrayList<Integer>(Collections.nCopies(SIZE, VALUE));
    }
}
