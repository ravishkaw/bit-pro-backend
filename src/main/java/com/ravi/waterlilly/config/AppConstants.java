package com.ravi.waterlilly.config;

import java.time.LocalTime;

// Application constants class.
public class AppConstants {

    // Default pagination values
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "10";

    // Default sorting values
    public static final String SORT_DIR = "desc";
    public static final String SORT_BY = "id";

    // Default privilege values
    public static final String SELECT = "select";
    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    // Default Status
    public static final String ACTIVE_STATUS = "Active";
    public static final String DELETED_STATUS = "Deleted";
    public static final String INACTIVE_STATUS = "Inactive";

    // Default values for check-in and check-out time
    public static final LocalTime DEFAULT_CHECK_IN_TIME = LocalTime.of(14, 0);  // 2 PM
    public static final LocalTime DEFAULT_CHECK_OUT_TIME = LocalTime.of(10, 0); // 10 AM

    // Default values for event start and end time
    public static final LocalTime DEFAULT_EVENT_START_TIME = LocalTime.of(8, 0); // 8 AM
    public static final LocalTime DEFAULT_EVENT_END_TIME = LocalTime.of(18, 0);  // 6 PM

}
