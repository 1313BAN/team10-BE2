package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.tour.dao.ITourDAO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.MatchedStopoverDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.StopoverDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StopoverProcessor {
	
	private final ITourDAO tourDAO;
	
	public List<MatchedStopoverDTO> resolveStopovers(List<StopoverDTO> stopovers) {
		List<MatchedStopoverDTO> result = new ArrayList<>();

		for (StopoverDTO stopover : stopovers) {
			TourDTO match = findMatchingTour(stopover);
			
			result.add(MatchedStopoverDTO.builder()
					.place(match)
					.date(stopover.getDate())
					.startTime(stopover.getStartTime())
					.endTime(stopover.getEndTime())
					.isFixed(stopover.isFixed())
					.build());
		}
		
		return result;
	}
	
	public List<TourDTO> mapStopoversToTour(List<StopoverDTO> stopovers) {
		List<TourDTO> matched = new ArrayList<>();
		
		for (StopoverDTO stopover : stopovers) {
			TourDTO bestMatch = findMatchingTour(stopover);
			if (bestMatch != null) {
				matched.add(bestMatch);
			} else {
				// TODO : insert
			}
		}
		
		return matched;
	}
	
	private TourDTO findMatchingTour(StopoverDTO stopover) {
		// 0. DB에 캐싱된 place_id로 조회, 매치 시 반환
		TourDTO match = tourDAO.getTourDataByPlaceId(stopover.getPlaceId());
		if (match != null) return match;
		
		// 1. 반경 내 후보 조회
		List<TourDTO> nearby = tourDAO.findNearby(
				stopover.getLatitude() - 0.0018, 
				stopover.getLatitude() + 0.0018, 
				stopover.getLongitude() - 0.0018,
				stopover.getLongitude() + 0.0018); // 200m 이내
				
		// 2. 이름 유사도 기준으로 가장 비슷한 장소 선택
		TourDTO bestMatch = null;
		double bestScore = -1.0;
		
		for (TourDTO candidate : nearby) {
			double score = calcSimilarity(stopover.getName(), candidate.getTitle());
			if (score > bestScore) {
				bestScore = score;
				bestMatch = candidate;
			}
		}
		
		// 3. 임계값 넘는 경우 DB에 캐싱 후 반환
		if (bestScore > 0.75) {
			bestMatch.setPlaceId(stopover.getPlaceId());
			tourDAO.updatePlaceId(bestMatch);
			
			return bestMatch;
		}
		// 4. 임계값 넘지 못하는 경우 DB에 장소 추가 후 반환
		// 추후 구현
		else {
			return convertStopoverToTour(stopover);
		}
	}
	
	private TourDTO convertStopoverToTour(StopoverDTO stopover) {
		return TourDTO.builder()
				.title(stopover.getName())
				.addr1(stopover.getFormattedAddress())
				.latitude(stopover.getLatitude())
				.longitude(stopover.getLongitude())
				.placeId(stopover.getPlaceId())
				.build();
	}
	
	private double calcSimilarity(String a, String b) {
	    if (a == null || b == null) return 0;

	    a = a.toLowerCase().replaceAll("\\s+", "");
	    b = b.toLowerCase().replaceAll("\\s+", "");

	    JaroWinklerSimilarity sim = new JaroWinklerSimilarity();
	    return sim.apply(a, b); // 0.0 ~ 1.0
	}
}
