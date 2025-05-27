package com.ssafy.enjoytrip.google.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.google.dto.GooglePlacesCacheDTO;

@Mapper
public interface GooglePlacesCacheDAO {

	GooglePlacesCacheDTO getCacheByPlaceId(@Param("placeId") String placeId);
	int insertCache(GooglePlacesCacheDTO cache);
	int updateCache(GooglePlacesCacheDTO cache);
}
