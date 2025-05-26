package com.ssafy.enjoytrip.travelrequest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.tour.dao.ITourDAO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.client.GooglePlacesApiClient;
import com.ssafy.enjoytrip.travelrequest.dto.OpeningHoursDTO;
import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpeningHoursService {

	private final GooglePlacesApiClient googlePlacesApiClient;
	private final ITourDAO tourDAO;
	
	public void fillOpeningHours(TourDTO place) {
		
		// 이미 있음
        if (place.getOpeningHours() != null &&
                place.getOpeningHours().getPeriods() != null &&
                !place.getOpeningHours().getPeriods().isEmpty()) {
                return;
            }
        
        // 없으면 API 호출
        Optional<OpeningHoursDTO> fetched = googlePlacesApiClient.getOpeningHours(place);
        if (fetched.isEmpty()) return;
        
        // DB에 저장
        Map<String, Object> param = new HashMap<>();
        param.put("contentId", place.getContentId());
        param.put("periods", fetched.get().getPeriods());
        tourDAO.insertOpeningPeriods(param);
		
        // 객체에 주입
        place.setOpeningHours(fetched.get());
	}
	
	public void fillOpeningHours(List<ScoredPlaceDTO> scoredPlaces) {
		for (ScoredPlaceDTO scoredPlace : scoredPlaces) {
			fillOpeningHours(scoredPlace.getPlace());
		}
	}
	
	public void fillOpeningHours(Map<String, List<ScoredPlaceDTO>> categorizedPlaces) {
		for (Map.Entry<String, List<ScoredPlaceDTO>> entry : categorizedPlaces.entrySet()) {
			String category = entry.getKey();
			List<ScoredPlaceDTO> scoredPlaces = entry.getValue();
			
			if (category.equals("attractions")) {
				for (ScoredPlaceDTO scoredPlace : scoredPlaces) {
					TourDTO place = scoredPlace.getPlace();
					if (place.getContentTypeId() <= 14) {
						// 12: 관광지, 14: 문화시설
						fillOpeningHours(place);
					}
				}
			} else {
				fillOpeningHours(scoredPlaces);
			}
		}
	}
}
