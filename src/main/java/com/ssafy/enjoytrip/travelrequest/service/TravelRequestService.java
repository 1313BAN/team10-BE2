package com.ssafy.enjoytrip.travelrequest.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.tour.service.TourDataService;
import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.service.util.DistanceMatrixBuilder;
import com.ssafy.enjoytrip.travelrequest.service.util.PlaceCategorizer;
import com.ssafy.enjoytrip.travelrequest.service.util.PlaceSelector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelRequestService {

    private final TourDataService tourDataService;
    private final PlaceCategorizer categorizer;
    private final PlaceSelector selector;
    private final DistanceMatrixBuilder matrixBuilder;
    private final OpeningHoursService openingHoursService;
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
        List<TourDTO> allPlaces = filterByArea(request);
        
        /* 
         * DB 기준 장소 타입
         * 12: 관광지
         * 14: 문화시설
         * 15: 축제공연행사
         * 25: 여행코스
         * 28: 레포츠
         * 32: 숙박
         * 38: 쇼핑
         * 39: 음식점
         */
        /*
         *  2. 나머지 / 숙박 / 쇼핑 / 음식점 / 카페로 나누기
         *  여행코스는 제외
         */
        Map<String, List<TourDTO>> categorizedPlaces = categorizer.categorize(allPlaces);
        
        /*
         * 3. 점수 매기고 정렬하기
         * 4. 일단 총 날짜만 단순하게 고려해서 필요한 장소 개수 구하기
         * 5. 점수 분포 상위 장소만 추리기. 필요한 장소의 1배수 이상으로
         * 랜덤성 확보를 위해 상위 40%를 넘지 않는 선에서 필요한 장소의 2배수까지 개수 늘리고 shuffle
         */
        Map<String, List<ScoredPlaceDTO>> selectedPlaces = selector.scoreAndSelect(categorizedPlaces, request);
        
        // 6. 이동 시간 행렬 만들기
        Map<Integer, Map<Integer, Integer>> distanceMatrix = matrixBuilder.build(selectedPlaces);
        
        /*
         *  7. 상위 장소들에 대해 Google API 통해서 운영 시간 데이터 얻어오기
         *  DB에 캐싱 되어있다면 그대로 사용
         *  TODO : attraction 테이블에 운영 시간 column 추가
         */
        openingHoursService.fillOpeningHours(selectedPlaces);
        
        /* 
         * 8. 그리디하게 날짜별로 장소 분배
         * 이미 경유지로 포함된 장소는 제외
         * 맛집 선호 선택시, 맛집 기준으로 코스 구성
         * 맛집 선호 미선택시, 관광지 기준으로 코스 구성
         * 시간 구간 한쪽 끝에 경유지 존재시, 경유지 기준으로 코스 구성
         * 시간 구간 양쪽 끝에 경유지 존재시, 어떻게 하지?
         * TODO : 최대 이동 시간 및 총 이동 시간 제한 추가
         */
        return planner.buildPlan(request, selectedPlaces, distanceMatrix);
    }
    
    private List<TourDTO> filterByArea(TravelRequestDTO request) {
    	int areaCode = request.getAreaCode();
    	Integer sigunguCode = request.getSigunguCode();
    	
        return tourDataService.getTourDatum(areaCode, sigunguCode, null);
    }
}
