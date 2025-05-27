package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.enjoytrip.google.client.GooglePlacesApiClient;
import com.ssafy.enjoytrip.google.dao.GooglePlacesCacheDAO;
import com.ssafy.enjoytrip.google.dto.GooglePlacesCacheDTO;
import com.ssafy.enjoytrip.google.dto.GooglePlacesDetailDTO;
import com.ssafy.enjoytrip.tour.dao.ITourDAO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.OpeningHoursDTO;
import com.ssafy.enjoytrip.travelrequest.dto.OpeningPeriodDTO;
import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceInfoEnricher {

	private final GooglePlacesApiClient googlePlacesApiClient;
	private final ITourDAO tourDAO;
	private final GooglePlacesCacheDAO googlePlacesCacheDAO;
	
	public void fillFields(PlaceContext place, List<String> fields) {
		if (place.getPlaceId() == null) fillPlaceId(place);
		
		GooglePlacesDetailDTO detail = googlePlacesApiClient.getDetailInfo(place.getPlaceId(), fields);
		if (detail == null) return;

	    Map<String, Object> data = detail.getDetails();

	    for (String field : fields) {
	        Object value = data.get(field);

	        switch (field) {
	            case "rating":
	                if (value instanceof Double) {
	                    place.setRating((Double) value);
	                }
	                break;

	            case "user_ratings_total":
	                if (value instanceof Integer) {
	                    place.setTotalRatings((Integer) value);
	                }
	                break;

	            case "types":
	                if (value instanceof JsonNode) {
	                    JsonNode typesNode = (JsonNode) value;
	                    List<String> types = new ArrayList<>();
	                    typesNode.forEach(node -> types.add(node.asText()));
	                    handleTypes(place, types);
	                }
	                break;
	                
	            case "opening_hours":
	                if (value instanceof JsonNode) {
	                    JsonNode openingHoursNode = (JsonNode) value;
	                    JsonNode periodsArray = openingHoursNode.path("periods");

	                    if (periodsArray.isArray()) {
	                        List<OpeningPeriodDTO> periodList = new ArrayList<>();
	                        for (JsonNode periodNode : periodsArray) {
	                            JsonNode openNode = periodNode.path("open");
	                            JsonNode closeNode = periodNode.path("close");

	                            if (!openNode.isMissingNode() && !closeNode.isMissingNode()) {
	                                periodList.add(OpeningPeriodDTO.builder()
	                                        .dayOfWeek(openNode.path("day").asInt())
	                                        .openTime(openNode.path("time").asText())
	                                        .closeTime(closeNode.path("time").asText())
	                                        .build());
	                            }
	                        }
	                        place.setOpeningHours(OpeningHoursDTO.builder().periods(periodList).build());
	                    }
	                }
	                break;

	            // 필요 시 다른 필드 추가
	        }
	    }
	    
	    updateOrInsertCache(place);
	}
	
	public void handleTypes(PlaceContext place, List<String> types) {
		if (types.contains("cafe")) place.setCafe(true);
		else if (types.contains("restaurant")) place.setContentTypeId(39);
		else if (types.contains("lodging")) place.setContentTypeId(32);
		else place.setContentTypeId(12);
	}
	
	private void fillPlaceId(PlaceContext place) {
		String placeId = googlePlacesApiClient.getFirstPlaceId(place);
		
		place.setPlaceId(placeId);
	}
	
	public void updateOrInsertCache(PlaceContext place) {
		TourDTO existingTour = tourDAO.getTourDataByPlaceId(place.getPlaceId());
		if (existingTour != null) {
			TourDTO updatedTour = TourDTO.builder()
					.contentId(existingTour.getContentId()) // 기존 contentId 유지
					.contentTypeId(place.getContentTypeId())
					.addr1(place.getAddress())
					.title(place.getName())
					.latitude(place.getLatitude())
					.longitude(place.getLongitude())
					.placeId(place.getPlaceId())
					.rating(place.getRating())
					.totalRatings(place.getTotalRatings())
					.isCafe(place.isCafe())
					.openingHours(place.getOpeningHours())
					.build();

			tourDAO.update(updatedTour);
		}

		GooglePlacesCacheDTO existingCache = googlePlacesCacheDAO.getCacheByPlaceId(place.getPlaceId());
		GooglePlacesCacheDTO newCache = GooglePlacesCacheDTO.builder()
				.placeId(place.getPlaceId())
				.contentTypeId(place.getContentTypeId())
				.name(place.getName())
				.address(place.getAddress())
				.latitude(place.getLatitude())
				.longitude(place.getLongitude())
				.image(place.getImage())
				.isCafe(place.isCafe())
				.rating(place.getRating())
				.totalRatings(place.getTotalRatings())
				.build();

		if (existingCache != null) {
			googlePlacesCacheDAO.updateCache(newCache);
		} else {
			googlePlacesCacheDAO.insertCache(newCache);
		}
	}
}
