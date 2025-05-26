package com.ssafy.enjoytrip.travelrequest.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.DailyTimeRangeDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.DailyPlanDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.service.util.StayTimeCalculator;

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

	private StayTimeCalculator stayTimeCalculator;
	
    public PlanDTO buildPlan(TravelRequestDTO request, 
    		Map<String, List<ScoredPlaceDTO>> selectedPlaces,
    		Map<Integer, Map<Integer, Integer>> distanceMatrix) {
    	
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
    	
    	
    	
    	
    	
    	
    	return null;
    }
    
    private DailyPlanDTO
}
