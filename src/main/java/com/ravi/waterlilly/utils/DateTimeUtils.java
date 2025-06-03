package com.ravi.waterlilly.utils;

import com.ravi.waterlilly.config.AppConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// date time related utility functions
@Component
public class DateTimeUtils {

    // helper method to calculate number of nights between check in and check out dates.
    // Hotel night: 2:00 PM to next day 10:00 AM = 1 night
    public long getNumberOfNights(LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime) {
        final int CHECKIN_HOUR = AppConstants.DEFAULT_CHECK_IN_TIME.getHour();
        final int CHECKOUT_HOUR = AppConstants.DEFAULT_CHECK_OUT_TIME.getHour();

        // Determine the effective start date of the first night
        LocalDate effectiveCheckInDate = checkInDateTime.toLocalDate();

        // Determine the effective end date of the last night
        LocalDate effectiveCheckOutDate = checkOutDateTime.toLocalDate();
        if (checkOutDateTime.getHour() > CHECKOUT_HOUR) {
            effectiveCheckOutDate = checkOutDateTime.toLocalDate().plusDays(1);
        }

        long nights = ChronoUnit.DAYS.between(effectiveCheckInDate, effectiveCheckOutDate);

        // Minimum of 1 night
        return Math.max(1, nights);
    }

}
