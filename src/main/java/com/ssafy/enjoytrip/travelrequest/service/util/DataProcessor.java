package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.google.dao.GooglePlacesCacheDAO;
import com.ssafy.enjoytrip.google.dto.GooglePlacesCacheDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;
import com.ssafy.enjoytrip.travelrequest.dto.request.StopoverDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataProcessor {
	
	private final PlaceInfoEnricher enricher;
	private final GooglePlacesCacheDAO googlePlacesCacheDAO;
	
	public List<PlaceContext> toursToContexts(List<TourDTO> tours) {
		List<PlaceContext> result = new ArrayList<>();
		for (TourDTO tour : tours) result.add(getContext(tour));
		
		return result;
	}
	
	public List<PlaceContext> stopoversToContexts(List<StopoverDTO> stopovers) {
		List<PlaceContext> result = new ArrayList<>();
		for (StopoverDTO stopover : stopovers) result.add(getContext(stopover));
		
		return result;
	}
	
	public PlaceContext getContext(TourDTO tour) {
		
		PlaceContext context = PlaceContext.builder()
				.placeId(tour.getPlaceId())
				.name(tour.getTitle())
				.contentTypeId(tour.getContentTypeId())
				.latitude(tour.getLatitude())
				.longitude(tour.getLongitude())
				.address(tour.getAddr1())
				.rating(tour.getRating())
				.totalRatings(tour.getTotalRatings())
				.isCafe(tour.isCafe())
				.openingHours(tour.getOpeningHours())
				.isRequested(false)
				.isFixed(false)
				.build();
		
		if (context.getPlaceId() == null) enricher.fillFields(context, List.of(
				"rating", 
				"user_ratings_total", 
				"types",
				"opening_hours"));
		
		return context;
	}
	
	public PlaceContext getContext(StopoverDTO stopover) {

		PlaceContext context = PlaceContext.builder()
				.name(stopover.getName())
				.latitude(stopover.getLatitude())
				.longitude(stopover.getLongitude())
				.address(stopover.getFormattedAddress())
				.image(stopover.getImage())
				.placeId(stopover.getPlaceId())
				.rating(stopover.getRating())
				.totalRatings(stopover.getTotalRatings())
				.isCafe(stopover.getTypes().contains("cafe"))
				.date(stopover.getDate())
				.startTime(stopover.getStartTime())
				.endTime(stopover.getEndTime())
				.isRequested(true)
				.isFixed(stopover.isFixed())
				.build();
		
		GooglePlacesCacheDTO cache = googlePlacesCacheDAO.getCacheByPlaceId(stopover.getPlaceId());
		
		if (cache != null) {
			context.setContentTypeId(cache.getContentTypeId());
		} else {
			enricher.fillFields(context, List.of("types"));
			
			googlePlacesCacheDAO.insertCache(
					GooglePlacesCacheDTO.builder()
					.placeId(context.getPlaceId())
					.contentTypeId(context.getContentTypeId())
					.name(context.getName())
					.latitude(context.getLatitude())
					.longitude(context.getLongitude())
					.address(context.getAddress())
					.image(context.getImage())
					.isCafe(context.isCafe())
					.rating(context.getRating())
					.totalRatings(context.getTotalRatings())
					.build());
		}
		
		return context;
	}
}
