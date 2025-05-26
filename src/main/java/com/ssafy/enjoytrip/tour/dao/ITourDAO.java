package com.ssafy.enjoytrip.tour.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.tour.dto.AreaDTO;
import com.ssafy.enjoytrip.tour.dto.SigunguDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;

@Mapper
public interface ITourDAO {
	
	List<AreaDTO> getAreaList();
	List<SigunguDTO> getSigunguList(@Param("areaCode") Integer areaCode);
	List<TourDTO> getTourDatum(
			@Param("areaCode") int areaCode, 
			@Param("sigunguCode") Integer sigunguCode, 
			@Param("contentTypeId") Integer contentTypeId);
	TourDTO getTourDataByContentId(@Param("contentId") int contentId);
	int updateRatingInfo(TourDTO tourData);
	int updateCafe(TourDTO tourData);
	int insertOpeningPeriods(Map<String, Object> param);
}
