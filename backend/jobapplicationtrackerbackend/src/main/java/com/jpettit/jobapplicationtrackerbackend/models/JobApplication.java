package com.jpettit.jobapplicationtrackerbackend.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class JobApplication {
    final private String jobAppTitle;
    final private String companyName;
    final private String id;
    final private LocalDate dateApplied;
    final private String applicant;
    final private String status;
    final private ArrayList<Interview> interviews;

    private JobApplication(String jobAppTitle, String companyName,
                          String id, LocalDate dateApplied,
                          String applicant, String status, ArrayList<Interview> interviews) {
        this.jobAppTitle = jobAppTitle;
        this.companyName = companyName;
        this.id = id;
        this.dateApplied = dateApplied;
        this.applicant = applicant;
        this.status = status;
        this.interviews = interviews;
    }

    public static JobApplication createJobApplication(String jobAppTitle, String companyName,
                                                      String id, LocalDate dateApplied,
                                                      String applicant, String status,
                                                      ArrayList<Interview> interviews) {
        return new JobApplication(jobAppTitle, companyName, id,
                dateApplied, applicant, status, interviews);
    }

    public String getJobAppTitle() {
        return jobAppTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public String getApplicant() {
        return applicant;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Interview> getInterviews() {
        return interviews;
    }

    public Optional<Interview> getInterviewAtIndex(Integer index) {
        if (index < 0 || index > interviews.size() - 1) {
            return Optional.empty();
        } else {
            return Optional.of(interviews.get(index));
        }
    }
}
