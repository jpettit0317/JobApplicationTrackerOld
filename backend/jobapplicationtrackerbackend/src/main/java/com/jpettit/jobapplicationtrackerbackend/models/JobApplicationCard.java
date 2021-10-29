package com.jpettit.jobapplicationtrackerbackend.models;

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
}
