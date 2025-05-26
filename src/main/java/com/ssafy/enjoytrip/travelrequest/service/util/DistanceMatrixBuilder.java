package com.ssafy.enjoytrip.travelrequest.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.ssafy.enjoytrip.travelrequest.dto.ScoredPlaceDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistanceMatrixBuilder {

	private final GraphHopper graphHopper;
	
    public Map<Integer, Map<Integer, Integer>> build(List<ScoredPlaceDTO> places) {
        // GraphHopper로 거리 계산

        Map<Integer, Map<Integer, Integer>> matrix = new HashMap<>();

        for (ScoredPlaceDTO from : places) {
            Map<Integer, Integer> distanceFrom = new HashMap<>();
            for (ScoredPlaceDTO to : places) {
                int fromId = from.getPlace().getContentId();
                int toId = to.getPlace().getContentId();

                if (fromId == toId) {
                    distanceFrom.put(toId, 0);
                    continue;
                }

                GHRequest req = new GHRequest(
                    from.getPlace().getLatitude(), from.getPlace().getLongitude(),
                    to.getPlace().getLatitude(), to.getPlace().getLongitude()
                ).setProfile("car");

                GHResponse res = graphHopper.route(req);

                if (res.hasErrors()) {
                    distanceFrom.put(toId, Integer.MAX_VALUE);
                    System.err.printf("Route error: %s → %s\n", fromId, toId);
                } else {
                    ResponsePath path = res.getBest();
                    int timeInMinutes = (int) (path.getTime() / 1000 / 60); // ms → 분
                    distanceFrom.put(toId, timeInMinutes);
                }
            }
            matrix.put(from.getPlace().getContentId(), distanceFrom);
        }

        return matrix;
    }
    
    public Map<Integer, Map<Integer, Integer>> build(Map<String, List<ScoredPlaceDTO>> places) {

    	// Map을 하나의 리스트로 합치기
    	List<ScoredPlaceDTO> combinedList = combineMapToList(places);
    	
    	return build(combinedList);
    }
    
    private List<ScoredPlaceDTO> combineMapToList(Map<String, List<ScoredPlaceDTO>> map) {
    	List<ScoredPlaceDTO> combinedList = new ArrayList<>();
    	
    	for (List<ScoredPlaceDTO> list : map.values()) {
    		combinedList.addAll(list);
    	}
    	
    	return combinedList;
    }
}
