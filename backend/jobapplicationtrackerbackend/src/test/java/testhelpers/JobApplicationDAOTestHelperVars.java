package testhelpers;

import com.jpettit.jobapplicationtrackerbackend.models.JobApplicationCard;

import java.util.ArrayList;

public class JobApplicationDAOTestHelperVars {
    public static final String USER1_NAME = "user1";

    public static ArrayList<JobApplicationCard> createDefaultCards() {
        ArrayList<JobApplicationCard> cards = new ArrayList<>();

        createDefaultCardArray(cards);

        return cards;
    }

    private static void createDefaultCardArray(ArrayList<JobApplicationCard> cards) {
        final String[] TITLES = { "iOS Developer", "C Developer" };
        final String[] COMPANIES = { "EJ1", "EJ2" };
        final int[] COUNTS = { 1, 2 };
        final String[] IDS = { "1", "2" };

        for (int i = 0; i < 2; i++) {
            cards.add(createCard(TITLES[i], COMPANIES[i], COUNTS[i], IDS[i]));
        }
    }

    private static JobApplicationCard createCard(final String TITLE, final String COMPANY,
                                                 final int COUNT, final String ID) {
        return JobApplicationCard.createCard(TITLE, COMPANY, COUNT, ID);
    }
}
