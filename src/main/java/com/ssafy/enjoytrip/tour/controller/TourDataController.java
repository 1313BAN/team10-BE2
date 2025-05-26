package com.ssafy.enjoytrip.tour.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.tour.dto.AreaDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.tour.dto.SigunguDTO;
import com.ssafy.enjoytrip.tour.service.TourDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TourDataController {

	private final TourDataService tourDataService;
	
	@GetMapping("/area")
	public List<AreaDTO> getAreaList() {
		return tourDataService.getAreaList();
	}
	
	@GetMapping("/area/{areaCode}")
	public List<SigunguDTO> getSigunguList(@PathVariable Integer areaCode) {
		return tourDataService.getSigunguList(areaCode);
	}
	
	@GetMapping("/attraction")
	public List<TourDTO> getTourDatum(
			@RequestParam Integer areaCode,
			@RequestParam(required = false) Integer sigunguCode,
			@RequestParam(required = false) Integer contentTypeId) {
		return tourDataService.getTourDatum(areaCode, sigunguCode, contentTypeId);
	}
}
