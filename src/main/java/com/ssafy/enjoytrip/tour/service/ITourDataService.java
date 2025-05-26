package com.ssafy.enjoytrip.tour.service;

import java.util.List;

import com.ssafy.enjoytrip.tour.dto.AreaDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.tour.dto.SigunguDTO;

public interface ITourDataService {
	List<AreaDTO> getAreaList();
	List<SigunguDTO> getSigunguList(Integer areaCode);
	List<TourDTO> getTourDatum(int areaCode, Integer sigunguCode, Integer contentTypeId);
	TourDTO getTourDataByContentId(int contentId);
	int updateRatingInfo(TourDTO tourData);
	int updateCafeInfo(TourDTO tourData);
}
