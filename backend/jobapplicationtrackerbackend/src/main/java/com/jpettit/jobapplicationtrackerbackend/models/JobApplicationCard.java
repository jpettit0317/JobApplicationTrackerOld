package com.jpettit.jobapplicationtrackerbackend.models;

import java.util.Objects;

public class JobApplicationCard {
    private final String jobTitle;
    private final String companyName;
    private final Integer interviewCount;
    private final String jobAppId;


    private JobApplicationCard(String jobTitle, String companyName,
                              Integer interviewCount, String jobAppId) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.interviewCount = interviewCount;
        this.jobAppId = jobAppId;
    }

    public static JobApplicationCard createCard(String jobTitle, String companyName,
                                     Integer interviewCount, String jobAppId) {
        return new JobApplicationCard(jobTitle, companyName, interviewCount, jobAppId);
    }

    public static JobApplicationCard createFromJobApp(JobApplication JOBAPP) {
        final Integer INTERVIEW_COUNT = JOBAPP.getInterviews().size();

        return new JobApplicationCard(
                JOBAPP.getJobAppTitle(), JOBAPP.getCompanyName(),
                INTERVIEW_COUNT, JOBAPP.getId()
        );
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Integer getInterviewCount() {
        return interviewCount;
    }

    public String getJobAppId() {
        return jobAppId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobApplicationCard that = (JobApplicationCard) o;
        return jobTitle.equals(that.jobTitle) && companyName.equals(that.companyName) && interviewCount.equals(that.interviewCount) && jobAppId.equals(that.jobAppId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobTitle, companyName, interviewCount, jobAppId);
    }
}
