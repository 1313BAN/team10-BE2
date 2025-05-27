package com.ssafy.enjoytrip.travelrequest.service.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.ssafy.enjoytrip.travelrequest.dto.OpeningHoursDTO;
import com.ssafy.enjoytrip.travelrequest.dto.OpeningPeriodDTO;

public class OpeningHoursUtil {

	public static boolean isAvailableBetween(
			OpeningHoursDTO hours,
			LocalDateTime arrival,
			LocalDateTime departure
			) {
		if (hours == null || hours.getPeriods() == null) return true;
		
		DayOfWeek day = arrival.getDayOfWeek();
		int dayIdx = (day.getValue() % 7);
		
		List<OpeningPeriodDTO> periods = hours.getPeriods().stream()
				.filter(p -> p.getDayOfWeek() == dayIdx)
				.toList();
		
		if (periods.isEmpty()) return false;
		
		LocalTime arrTime = arrival.toLocalTime();
		LocalTime depTime = departure.toLocalTime();
		
		for (OpeningPeriodDTO p : periods) {
			LocalTime open = parseTime(p.getOpenTime());
			LocalTime close = parseTime(p.getCloseTime());
			
			if (!arrTime.isBefore(open) && !depTime.isAfter(close)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static LocalTime parseTime(String timeString) {
		return LocalTime.of(
				Integer.parseInt(timeString.substring(0, 2)), 
				Integer.parseInt(timeString.substring(2, 4))
				);
	}
}
