package com.ssafy.enjoytrip.travelrequest.service.util;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.OpeningPeriodDTO;

@Component
public class OpeningHoursChecker {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    public static boolean contains(OpeningPeriodDTO period, LocalTime visitStart, LocalTime visitEnd) {
        LocalTime open = LocalTime.parse(period.getOpenTime(), FORMATTER);
        LocalTime close = LocalTime.parse(period.getCloseTime(), FORMATTER);

        return !visitStart.isBefore(open) && !visitEnd.isAfter(close);
    }

    public static boolean isVisitable(List<OpeningPeriodDTO> periods, DayOfWeek day, LocalTime start, LocalTime end) {
        int dayValue = day.getValue() % 7;

        return periods.stream()
            .filter(p -> p.getDayOfWeek() == dayValue)
            .anyMatch(p -> contains(p, start, end));
    }
}
