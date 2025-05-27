package com.ssafy.enjoytrip.travelrequest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.google.client.GooglePlacesApiClient;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.request.TravelRequestDTO;
import com.ssafy.enjoytrip.travelrequest.dto.response.PlanDTO;
import com.ssafy.enjoytrip.travelrequest.service.TravelRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class TravelRequestController {

	private final TravelRequestService travelRequestService;
	private final GooglePlacesApiClient googlePlacesApiClient;
	
	@PostMapping("/plan")
	public ResponseEntity<PlanDTO> planTrip(@RequestBody @Valid TravelRequestDTO request) {
		
		
		return null;
	}
	
	public List<TourDTO> filterAttraction(List<TourDTO> all, TravelRequestDTO request) {
		List<Integer> preferredTypes = request.getPreferredTypes();
		
		return null;
	}
	
	@RequestMapping("/test")
	public ResponseEntity<PlanDTO> test(@RequestBody @Valid TravelRequestDTO request) {
		
		PlanDTO plan = travelRequestService.getTravelPlan(request);
		return ResponseEntity.ok(plan);
	}
}
