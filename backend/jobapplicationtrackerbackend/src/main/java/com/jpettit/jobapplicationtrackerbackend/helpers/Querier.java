package com.jpettit.jobapplicationtrackerbackend.helpers;

public abstract class Querier {
    abstract String getTableName(ProjectEnvironment env);
}