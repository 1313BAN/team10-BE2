package com.ssafy.enjoytrip.travelrequest.service.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.ssafy.enjoytrip.travelrequest.dto.PlaceContext;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistanceMatrixBuilder {

	private final GraphHopper graphHopper;
	
    public Map<String, Map<String, Duration>> build(List<PlaceContext> places) {
        // GraphHopper로 거리 계산
    	// placeId 기반

        Map<String, Map<String, Duration>> matrix = new HashMap<>();
        
        for (PlaceContext from : places) {
            Map<String, Duration> distanceFrom = new HashMap<>();
            for (PlaceContext to : places) {
                String fromId = from.getPlaceId();
                String toId = to.getPlaceId();

                if (fromId.equals(toId)) {
                    distanceFrom.put(toId, Duration.ZERO);
                    continue;
                }

                GHRequest req = new GHRequest(
                    from.getLatitude(), from.getLongitude(),
                    to.getLatitude(), to.getLongitude()
                ).setProfile("car");

                GHResponse res = graphHopper.route(req);

                if (res.hasErrors()) {
                    distanceFrom.put(toId, Duration.ofHours(999));
                    System.err.printf("Route error: %s → %s\n", fromId, toId);
                } else {
                    ResponsePath path = res.getBest();
                    Duration travelTime = Duration.ofMillis(path.getTime());
                    distanceFrom.put(toId, travelTime);
                }
            }
            matrix.put(from.getPlaceId(), distanceFrom);
        }

        return matrix;
    }
}
