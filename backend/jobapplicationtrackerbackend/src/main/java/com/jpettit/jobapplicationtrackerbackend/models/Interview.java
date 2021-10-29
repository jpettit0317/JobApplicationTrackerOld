package com.jpettit.jobapplicationtrackerbackend.models;

import java.sql.Date;
import java.util.Objects;

public class Interview {
    final String interviewId;
    final String jobAppId;
    final String type;
    final Date interviewDate;
    final String interviewStatus;


    private Interview(String interviewId, String jobAppId, String type,
                     Date interviewDate, String interviewStatus) {
        this.interviewId = interviewId;
        this.jobAppId = jobAppId;
        this.type = type;
        this.interviewDate = interviewDate;
        this.interviewStatus = interviewStatus;
    }

    public static Interview createInterview(String interviewId, String jobAppId, String type,
                                            Date interviewDate, String interviewStatus) {
        return new Interview(interviewId, jobAppId, type, interviewDate, interviewStatus);
    }

    public String getInterviewId() {
        return interviewId;
    }

    public String getJobAppId() {
        return jobAppId;
    }

    public String getType() {
        return type;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interview interview = (Interview) o;
        return interviewId.equals(interview.interviewId) && jobAppId.equals(interview.jobAppId) && type.equals(interview.type) && interviewDate.equals(interview.interviewDate) && interviewStatus.equals(interview.interviewStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interviewId, jobAppId, type, interviewDate, interviewStatus);
    }
}
