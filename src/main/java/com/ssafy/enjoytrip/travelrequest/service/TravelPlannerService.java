package com.ssafy.enjoytrip.travelrequest.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.dto.request.DailyTimeRangeDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.VisitDTO;
import com.ssafy.enjoytrip.travelrequest.service.planstructure.DailyPlanSkeleton;
import com.ssafy.enjoytrip.travelrequest.service.planstructure.TimeBlock;

import lombok.RequiredArgsConstructor;

/* 
 * 이미 경유지로 포함된 장소는 제외
 * 맛집 선호 선택시, 맛집 기준으로 코스 구성
 * 맛집 선호 미선택시, 관광지 기준으로 코스 구성
 * 시간 구간 한쪽 끝에 경유지 존재시, 경유지 기준으로 코스 구성
 * 시간 구간 양쪽 끝에 경유지 존재시, 어떻게 하지?
 * TODO : 최대 이동 시간 및 총 이동 시간 제한 추가
 */

@Component
@RequiredArgsConstructor
public class TravelPlannerService {
	
    public PlanDTO buildPlan(TravelRequestDTO request, 
    		Map<String, List<PlaceContext>> selectedPlaces,
    		List<PlaceContext> fixedStops,
    		Map<String, Map<String, Duration>> distanceMatrix) {
    	
    	// 1. 경유지와 가용 시간으로 뼈대 만들기
    	List<DailyPlanSkeleton> skeletons = buildSkeletons(request, fixedStops);
    	
    	// 2. 빈 시간대에 방문지 채워넣기
    	
    	/*
    	 * 1. 경유지 쭉 검사하면서, 플랜에 박아 넣기
    	 * 2. 경유지도 distanceMatrix에 추가
    	 * 3. 경유지 LinkedList 형태로 구성, 사이사이에 빈 공간 클래스 정의, 이동시간 클래스 정의
    	 * 4. 빈공간에 채워넣기
    	 */

    	/*
    	 * case 구분
    	 * 1. 양쪽 끝이 고정된 경우
    	 * 	a) 같은 날인 경우
    	 * 	b) 다른 날인 경우
    	 * 2. 시작만 고정된 경우
    	 * 3. 끝만 고정된 경우
    	 */
    	
//    	List<PlaceContext> attractions = selectedPlaces.get("attractions");
//    	List<PlaceContext> lodges = selectedPlaces.get("lodges");
//    	List<PlaceContext> shoppings = selectedPlaces.get("shoppings");
//    	List<PlaceContext> restaurants = selectedPlaces.get("restaurants");
//    	List<PlaceContext> cafes = selectedPlaces.get("cafes");
//    	
//    	
//    	PlanDTO plan = PlanDTO.builder()
//    			.title("test")
//    			.sidoName("서울")
//    			.startDate(request.getDailyTimeRanges().get(0).getDate())
//    			.endDate(request.getDailyTimeRanges().get(request.getDailyTimeRanges().size() - 1).getDate())
//    			.build();
//    	
//    	DailyPlanDTO dailyPlan = DailyPlanDTO.builder()
//    			.date(plan.getStartDate())
//    			.build();
//    	
//    	VisitDTO visit = VisitDTO.builder()
//    			.placeId(attractions.get(0).getPlaceId())
//    			.startTime(LocalDateTime.of(2025, 5, 28, 10, 0))
//    			.endTime(LocalDateTime.of(2025, 5, 28, 11, 0))
//    			.visitOrder(0)
//    			.build();
//    	
//    	dailyPlan.setVisits(List.of(visit));
//    	plan.setDailyPlans(List.of(dailyPlan));
    	
    	
    	
    	
    	return null;
    }
    
    public List<DailyPlanSkeleton> buildSkeletons(TravelRequestDTO request, List<PlaceContext> fixedStops) {
    	Map<LocalDate, DailyPlanSkeleton> skeletonMap = new TreeMap<>();
    	
    	// 1. 여행 기간에 해당하는 각 날짜 생성
    	for (DailyTimeRangeDTO timeRange : request.getDailyTimeRanges()) {
    	    LocalDate date = timeRange.getDate();
    	    LocalDateTime start = timeRange.getStartTime();
    	    LocalDateTime end = timeRange.getEndTime();

    	    // 해당 날짜의 fixed stopovers
    	    List<PlaceContext> stopsOnDate = fixedStops.stream()
    	        .filter(s -> s.getDate().equals(date))
    	        .collect(Collectors.toList());

    	    boolean hasFixedLunch = stopsOnDate.stream()
    	        .anyMatch(p -> isMealStop(p) && isInTimeRange(p, 12, 14));

    	    boolean hasFixedDinner = stopsOnDate.stream()
    	        .anyMatch(p -> isMealStop(p) && isInTimeRange(p, 18, 20));

    	    boolean hasLodgingAtStart = stopsOnDate.stream()
    	        .anyMatch(p -> isLodging(p) && p.getStartTime().equals(start));

    	    boolean hasLodgingAtEnd = stopsOnDate.stream()
    	        .anyMatch(p -> isLodging(p) && p.getEndTime().equals(end));

            DailyPlanSkeleton skeleton = DailyPlanSkeleton.builder()
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .hasFixedLunch(hasFixedLunch)
                    .hasFixedDinner(hasFixedDinner)
                    .hasLodgingAtStart(hasLodgingAtStart)
                    .hasLodgingAtEnd(hasLodgingAtEnd)
                    .build();
            
            skeletonMap.put(date, skeleton);
    	}
    	
        // 2. Stopover 삽입
        for (PlaceContext stop : fixedStops) {
            LocalDate stopDate = stop.getDate();
            DailyPlanSkeleton skeleton = skeletonMap.get(stopDate);
            if (skeleton == null) continue;

            TimeBlock block = TimeBlock.builder()
            		.start(stop.getStartTime())
            		.end(stop.getEndTime())
            		.isFixed(true)
            		.fixedVisit(convertToVisit(stop))
            		.build();
            
            skeleton.getBlocks().add(block);
        }

        // 3. block 정렬 + 빈 구간 채우기
        for (DailyPlanSkeleton skeleton : skeletonMap.values()) {
            skeleton.fillOpenBlocks(); // open block 자동 삽입
        }

        return new ArrayList<>(skeletonMap.values());
    }
    
    private VisitDTO convertToVisit(PlaceContext stop) {
        return VisitDTO.builder()
                .placeId(stop.getPlaceId())
                .name(stop.getName())
                .latitude(stop.getLatitude())
                .longitude(stop.getLongitude())
                .contentTypeId(stop.getContentTypeId())
                .startTime(stop.getStartTime())
                .endTime(stop.getEndTime())
                .build();
    }
    
    private boolean isMealStop(PlaceContext place) {
        return place.getContentTypeId() == 39; // 음식점 or 카페
    }

    private boolean isLodging(PlaceContext place) {
        return place.getContentTypeId() == 32;
    }

    private boolean isInTimeRange(PlaceContext place, int fromHour, int toHour) {
        LocalTime start = place.getStartTime().toLocalTime();
        return !start.isBefore(LocalTime.of(fromHour, 0)) &&
               !start.isAfter(LocalTime.of(toHour, 0));
    }
}
