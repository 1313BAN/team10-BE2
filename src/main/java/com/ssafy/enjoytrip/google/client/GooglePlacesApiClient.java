package com.ssafy.enjoytrip.google.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.enjoytrip.google.dto.GooglePlacesDetailDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.OpeningHoursDTO;
import com.ssafy.enjoytrip.travelrequest.dto.OpeningPeriodDTO;
import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.dto.RatingDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GooglePlacesApiClient {

	@Value("${google.places.api.url}")
	private String API_URL;
	
	@Value("${google.places.api.key}")
	private String API_KEY;
	
	private final RestTemplate restTemplate;
	
	private List<String> getPlaceId(String... inputs) {
		String input = String.join(" ", inputs);
		
		String url = API_URL
				+ "/findplacefromtext/json"
				+ "?input=" + input
				+ "&inputtype=textquery"
				+ "&fields=place_id"
				+ "&key=" + API_KEY;

		try {
			JsonNode responseNode = restTemplate.getForObject(url, JsonNode.class);
			
			List<String> placeIds = new ArrayList<>();
			
			if (!responseNode.path("status").asText().equals("OK")) {
				// search fail
				return placeIds;
			} else {
				JsonNode candidates = responseNode.path("candidates");
				if (candidates.isArray()) {
					for (JsonNode candidate : candidates) {
						String placeId = candidate.path("place_id").asText();
						placeIds.add(placeId);
					}
				}
				return placeIds;
			}
		} catch (RestClientException e) {
			return Collections.emptyList();
		}
	}
	
	public String getFirstPlaceId(String... inputs) {
		List<String> placeIds = getPlaceId(inputs);
		
		if (!placeIds.isEmpty()) return placeIds.get(0);
		else return null;
	}
	
	public String getFirstPlaceId(PlaceContext context) {
		return getFirstPlaceId(context.getName(), context.getAddress());
	}
	
	public GooglePlacesDetailDTO getDetailInfo(String placeId, List<String> fields) {
		if (placeId == null || fields == null) return null;

		String url = API_URL
				+ "/details/json"
				+ "?place_id=" + placeId
				+ "&language=ko"
				+ "&key=" + API_KEY
		 		+ "&fields=name";

		for (String field : fields) url += "," + field;
		
		try {
			JsonNode responseNode = restTemplate.getForObject(url, JsonNode.class);
			JsonNode resultNode = responseNode.path("result");
			
			Map<String, Object> details = new HashMap<>();
			
			for (String field : fields) {
				JsonNode valueNode = resultNode.path(field);
				
				if (!valueNode.isMissingNode() && !valueNode.isNull()) {
	                if (valueNode.isInt()) {
	                    details.put(field, valueNode.asInt());
	                } else if (valueNode.isDouble()) {
	                    details.put(field, valueNode.asDouble());
	                } else if (valueNode.isBoolean()) {
	                    details.put(field, valueNode.asBoolean());
	                } else if (valueNode.isArray()) {
	                    details.put(field, valueNode); // 배열 그대로 넣기 (JsonNode)
	                } else if (valueNode.isObject()) {
	                    details.put(field, valueNode); // 객체 그대로 넣기
	                } else {
	                    details.put(field, valueNode.asText());
	                }
				}
			}
			
			return GooglePlacesDetailDTO.builder()
					.details(details)
					.build();
		} catch (RestClientException e) {
			return null;
		}
	}
	
	public String getFirstPlaceId(TourDTO place) {
		return getFirstPlaceId(place.getTitle(), place.getAddr1());
	}
	
	public Optional<RatingDTO> getRatingInfo(String placeId) {
		if (placeId == null) return Optional.empty();

		List<String> fields = List.of("rating", "user_ratings_total");
		GooglePlacesDetailDTO detail = getDetailInfo(placeId, fields);
		if (detail == null) return Optional.empty();
		
		Object ratingValue = detail.getDetails().get("rating");
		Double rating = (ratingValue instanceof Double) ? (Double) ratingValue : null;
		
		Object totalRatingsValue = detail.getDetails().get("user_ratings_total");
		Integer totalRatings = (totalRatingsValue instanceof Integer) ? (Integer) totalRatingsValue : null;
		
		if (rating != null && totalRatings != null) {
			return Optional.of(RatingDTO.builder()
					.rating(rating)
					.totalRatings(totalRatings)
					.build());
		}

		return Optional.empty();
	}
	
	public Optional<List<String>> getTypes(String placeId) {
		if (placeId == null) return Optional.empty();
		
		List<String> fields = List.of("types");
		GooglePlacesDetailDTO detail = getDetailInfo(placeId, fields);
		if (detail == null) return Optional.empty();
		
		Object typesValue = detail.getDetails().get("types");
		JsonNode typesNode = (typesValue instanceof JsonNode) ? (JsonNode) typesValue : null;
		
		List<String> types;
		if (typesNode.isArray()) {
			types = new ArrayList<>();
			
			for (JsonNode item : typesNode) {
				types.add(item.asText());
			}
			
			return Optional.of(types);
		}
		
		return Optional.empty();
	}
	
	// 운영 시간 가져오기
	public Optional<OpeningHoursDTO> getOpeningHours(String placeId) {
		if (placeId == null) return Optional.empty();
		
		List<String> fields = List.of("opening_hours");
		GooglePlacesDetailDTO detail = getDetailInfo(placeId, fields);
		if (detail == null) return Optional.empty();
		
		Object openingHoursObj = detail.getDetails().get("opening_hours");
	    if (!(openingHoursObj instanceof JsonNode)) return Optional.empty();

	    JsonNode openingHoursNode = (JsonNode) openingHoursObj;

	    JsonNode periodsArray = openingHoursNode.path("periods");
		if (!periodsArray.isArray()) return Optional.empty();
		
		List<OpeningPeriodDTO> periodList = new ArrayList<>();
		
		for (JsonNode periodNode : periodsArray) {
			JsonNode openNode = periodNode.path("open");
			JsonNode closeNode = periodNode.path("close");
			
	        if (openNode.isMissingNode() || closeNode.isMissingNode()) continue;

	        int openDay = openNode.path("day").asInt();
	        String openTime = openNode.path("time").asText();

	        int closeDay = closeNode.path("day").asInt();
	        String closeTime = closeNode.path("time").asText();

	        // 동일한 날에 open/close 여러 개 올 수 있음 → 각각 저장
	        periodList.add(OpeningPeriodDTO.builder()
	                .dayOfWeek(openDay)
	                .openTime(openTime)
	                .closeTime(closeTime)
	                .build());
		}
		
		OpeningHoursDTO result = OpeningHoursDTO.builder()
				.periods(periodList)
				.build();
		
		return Optional.of(result);
	}
}
