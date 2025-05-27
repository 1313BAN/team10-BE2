package com.ssafy.enjoytrip.travelrequest.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.tour.service.TourDataService;
import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.service.util.DataProcessor;
import com.ssafy.enjoytrip.travelrequest.service.util.DistanceMatrixBuilder;
import com.ssafy.enjoytrip.travelrequest.service.util.PlaceCategorizer;
import com.ssafy.enjoytrip.travelrequest.service.util.PlaceFilter;
import com.ssafy.enjoytrip.travelrequest.service.util.PlaceSelector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelRequestService {

    private final TourDataService tourDataService;
    private final DataProcessor processor;
    private final PlaceCategorizer categorizer;
    private final PlaceSelector selector;
    private final PlaceFilter placeFilter;
    
    private final DistanceMatrixBuilder matrixBuilder;
    private final TravelPlannerService planner;
    
    public PlanDTO getTravelPlan(TravelRequestDTO request) {

        /*
         * 사용자가 선택할 수 있는 선호 타입 종류
         * 1. 관광지
         * 2. 문화시설
         * 3. 축제공연행사
         * 4. 레포츠
         * 6. 쇼핑
         * 7. 맛집
         * 8. 카페
         * 
         * TODO : 관광지 카테고리는 추후 세분화 가능 ( 역사 / 자연 등 )
         * 
         * 관광지, 문화시설, 축제공연행사, 레포츠 선택시 선택된 선호 타입에는 점수 가중치 부과
         * 쇼핑 선택시, 관광지에 추가
         * 쇼핑 미선택시, 추천에서 제외
         * 맛집 선택시, 맛집을 우선순위로 해서 주변 관광지 선택
         * 맛집 미선택시, 관광지를 우선순위로 해서 주변 맛집 선택
         * 카페 선택시, 관광지에 추가? or 하루 최소 1번 방문. 단, 연속 방문은 제한? 미정
         * 카페 미선택시, 추천에서 제외
         */
        
        // 1. 여행 지역 기반으로 모든 장소 목록 구하기
        List<TourDTO> allPlaces = tourDataService.getTourDatum(
        		request.getAreaCode(), 
        		request.getSigunguCode(), null);
        
        // 2. placeId 있는지 확인하고 없다면 구글에서 정보 받아오기 ( rating, totalRatings, isCafe, openingHours )
        List<PlaceContext> contexts = processor.toursToContexts(allPlaces);
        
        // 3. 경유지 부족한 정보 보충 ( 캐시 테이블 혹은 구글 ) ( contentTypeId )
        List<PlaceContext> fixedContexts = processor.stopoversToContexts(request.getStopovers());

        // 4. 나머지 / 숙박 / 쇼핑 / 음식점 / 카페로 나누기, 여행코스는 제외
        Map<String, List<PlaceContext>> categorizedPlaces = categorizer.categorize(contexts);
        
        // 5. 점수 매기고 정렬하기
        Map<String, List<PlaceContext>> selectedPlaces = selector.scoreAndSelect(categorizedPlaces, request);
        
        // 6. 선택된 리스트에서 중복 제거하기
        Map<String, List<PlaceContext>> cleaned = placeFilter.removeDuplicatesWithFixed(
        	    selectedPlaces, fixedContexts
        	);
        
        // 7. 이동 시간 행렬 만들기
        List<PlaceContext> combinedList = new ArrayList<>();
        combinedList.addAll(fixedContexts);
        for (List<PlaceContext> list : cleaned.values()) combinedList.addAll(list);
        Map<String, Map<String, Duration>> distanceMatrix = matrixBuilder.build(combinedList);
        
        /* 
         * 8. 그리디하게 날짜별로 장소 분배
         * 이미 경유지로 포함된 장소는 제외
         * 맛집 선호 선택시, 맛집 기준으로 코스 구성
         * 맛집 선호 미선택시, 관광지 기준으로 코스 구성
         * 시간 구간 한쪽 끝에 경유지 존재시, 경유지 기준으로 코스 구성
         * 시간 구간 양쪽 끝에 경유지 존재시, 어떻게 하지?
         * TODO : 최대 이동 시간 및 총 이동 시간 제한 추가
         */
        return planner.buildPlan(request, cleaned, fixedContexts, distanceMatrix);
    }
    
}
