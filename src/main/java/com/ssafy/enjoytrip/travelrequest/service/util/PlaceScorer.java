package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.tour.dto.TourDTO;
import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;

@Component
public class PlaceScorer {
	
	private final double MEAN_RATING = 3.0;
	private final int LOWER_BOUND = 20;
	private final int UPPER_BOUND = 2000;

	// with preferred types
	public List<ScoredPlaceDTO> scorePlaces(List<TourDTO> places, List<Integer> preferredTypes) {
		Set<Integer> preferredTypesSet = new HashSet<>();
		for (Integer type : preferredTypes) preferredTypesSet.add(type);
		
		List<ScoredPlaceDTO> result = new ArrayList<>();
		for (TourDTO place : places) {
			result.add(
				new ScoredPlaceDTO(
						place,
						calculateScore(place, preferredTypesSet.contains(place.getContentTypeId()))
						)
				);
		}
		
		return result;
	}
	
	// without preferred types
	public List<ScoredPlaceDTO> scorePlaces(List<TourDTO> places) {
		List<ScoredPlaceDTO> result = new ArrayList<>();
		for (TourDTO place : places) {
			result.add(
				new ScoredPlaceDTO(
						place,
						calculateScore(place, false)
						)
				);
		}
		
		return result;
	}
	
	private double calculateScore(TourDTO place, boolean isPreferred) {
		double score = 0.0;
		
		double rating = place.getRating();
		int totalRatings = place.getTotalRatings();
		
		// 평점 정보 없을 경우 0으로 재조정
		if (rating < 0) {
			rating = 0;
			totalRatings = 0;
		}
		// 리뷰 수 upper bound 제한
		totalRatings = Math.min(totalRatings, UPPER_BOUND);
		
		// 평점 가중치 계산(0~1로 정규화)
		score += calcBayesianAverage(rating, totalRatings) / 5.0;
		
		// 리뷰 수 가중치 계산(로그 정규화)
		score += Math.log(1 + totalRatings) / Math.log(1 + UPPER_BOUND);
		
		// 사용자 선호도 반영
		if (isPreferred) score *= 1.2;
		
		return score;
	}
	
	private double calcBayesianAverage(double rating, int totalRatings) {
		return (totalRatings * rating + LOWER_BOUND * MEAN_RATING) 
				/ (totalRatings + LOWER_BOUND);
	}
	
	/*
	 * 계산 로직 보완 예정
	 * 1. 리뷰 수는 정규화 <- done!
	 * 2. content type id 아직 모호하니 구체화 <- done!
	 * 3. TODO : type 12 세분화
	 */
}
