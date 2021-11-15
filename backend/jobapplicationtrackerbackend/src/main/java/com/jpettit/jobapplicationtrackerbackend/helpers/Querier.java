package com.jpettit.jobapplicationtrackerbackend.helpers;

import com.jpettit.jobapplicationtrackerbackend.enums.ProjectEnvironment;

public abstract class Querier {
    abstract String getTableName(ProjectEnvironment env);
}