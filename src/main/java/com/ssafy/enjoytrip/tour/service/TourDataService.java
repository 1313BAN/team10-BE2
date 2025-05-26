package com.ssafy.enjoytrip.tour.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.tour.dao.ITourDAO;
import com.ssafy.enjoytrip.tour.dto.AreaDTO;
import com.ssafy.enjoytrip.tour.dto.SigunguDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourDataService implements ITourDataService {

	private final ITourDAO tourDAO;

	@Override
	public List<AreaDTO> getAreaList() {
		return tourDAO.getAreaList();
	}

	@Override
	public List<SigunguDTO> getSigunguList(Integer areaCode) {
		return tourDAO.getSigunguList(areaCode);
	}

	@Override
	public List<TourDTO> getTourDatum(int areaCode, Integer sigunguCode, Integer contentTypeId) {
		return tourDAO.getTourDatum(areaCode, sigunguCode, contentTypeId);
	}
	
	@Override
	public TourDTO getTourDataByContentId(int contentId) {
		return tourDAO.getTourDataByContentId(contentId);
	}

	@Override
	public int updateRatingInfo(TourDTO tourData) {
		return tourDAO.updateRatingInfo(tourData);
	}

	@Override
	public int updateCafeInfo(TourDTO tourData) {
		return tourDAO.updateCafe(tourData);
	}
	
}
