package com.ssafy.enjoytrip.travelrequest.service.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;
import com.ssafy.enjoytrip.travelrequest.service.planstructure.TimeBlock;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GreedyVisitInserter {

	private final StayTimeCalculator stayTimeCalc;
	private final PlaceFilter placeFilter;
	
	public List<VisitDTO> insertVisits(
			TimeBlock block,
			PlaceContext prev,
			PlaceContext next,
			Map<String, List<PlaceContext>> candidates,
			Map<String, Map<String, Duration>> distanceMatrix,
			int travelStyle,
			boolean hasFixedLunch,
			boolean hasFixedDinner,
			boolean hasLodgingAtEnd
			) {
		
		List<VisitDTO> visits = new ArrayList<>();
		LocalDateTime cursor = block.getStart();
		PlaceContext current = prev;
		
		boolean hadLunch = hasFixedLunch;
		boolean hadDinner = hasFixedDinner;
		
		while (cursor.isBefore(block.getEnd())) {
			LocalTime time = cursor.toLocalTime();
			boolean inserted = false;
			
            // 1. 점심 삽입
            if (!hadLunch && isBetween(time, 12, 14)) {
                inserted = insertMeal(visits, current, next, cursor, block, candidates.getOrDefault("meal", List.of()), distanceMatrix, travelStyle);
                if (inserted) {
                    hadLunch = true;
                    current = visits.get(visits.size() - 1).toPlaceContext();
                    cursor = current.getEndTime();
                    continue;
                }
            }
            
            // 2. 저녁 삽입
            if (!hadDinner && isBetween(time, 18, 20)) {
                inserted = insertMeal(visits, current, next, cursor, block, candidates.getOrDefault("meal", List.of()), distanceMatrix, travelStyle);
                if (inserted) {
                    hadDinner = true;
                    current = visits.get(visits.size() - 1).toPlaceContext();
                    cursor = current.getEndTime();
                    continue;
                }
            }
            
            // 3. 일반 관광지 삽입
            List<PlaceContext> attractions = candidates.getOrDefault("attraction", List.of());
            List<PlaceContext> filtered = placeFilter.filterByDistanceOpeningHours(block, current, next, attractions, travelStyle, distanceMatrix);
            PlaceContext best = pickBestCandidate(filtered, current, distanceMatrix);

            if (best != null) {
                best.setStartTime(cursor.plus(getTravelTime(current, best, distanceMatrix)));
                best.setEndTime(best.getStartTime().plus(stayTimeCalc.stayTimeFor(best.getContentTypeId(), travelStyle)));
                visits.add(toVisitDTO(best));
                cursor = best.getEndTime();
                current = best;
                attractions.remove(best);
                continue;
            }

            // 4. 숙소 삽입 (마지막에 고정 숙소 없을 경우)
            if (!hasLodgingAtEnd && current != null && current.getContentTypeId() != 32) {
                List<PlaceContext> lodgings = candidates.getOrDefault("lodging", List.of());
                List<PlaceContext> lodgingFiltered = placeFilter.filterByDistanceOpeningHours(block, current, next, lodgings, travelStyle, distanceMatrix);
                PlaceContext hotel = pickBestCandidate(lodgingFiltered, current, distanceMatrix);
                if (hotel != null) {
                    hotel.setStartTime(cursor.plus(getTravelTime(current, hotel, distanceMatrix)));
                    hotel.setEndTime(hotel.getStartTime().plus(stayTimeCalc.stayTimeFor(hotel.getContentTypeId(), travelStyle)));
                    visits.add(toVisitDTO(hotel));
                }
                break;
            }

            break;
		}
	}
}
