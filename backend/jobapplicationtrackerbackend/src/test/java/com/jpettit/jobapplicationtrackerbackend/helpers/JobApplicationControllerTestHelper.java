package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;
import com.jpettit.jobapplicationtrackerbackend.models.Session;
import com.jpettit.jobapplicationtrackerbackend.models.User;

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
                createCard("iOS Dev", "EJ", 2, "1")
        };
        ArrayList<JobApplicationCard> cards = new ArrayList<>();

        Collections.addAll(cards, cardsArray);

        return cards;
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
}
