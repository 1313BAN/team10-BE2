package com.ssafy.enjoytrip.travelrequest.dto;

import com.ssafy.enjoytrip.tour.dto.TourDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoredPlaceDTO implements Comparable<ScoredPlaceDTO> {

    TourDTO place;
    double score;
    
    @Override
    public int compareTo(ScoredPlaceDTO o) {
        return Double.compare(o.score, this.score);
    }
}
