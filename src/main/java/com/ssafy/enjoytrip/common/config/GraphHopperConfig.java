package com.ssafy.enjoytrip.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;

@Configuration
public class GraphHopperConfig {

    @Bean
    GraphHopper graphHopper() {
		GraphHopper hopper = new GraphHopper();
		hopper.setOSMFile("src/main/resources/data/south-korea-latest.osm.pbf");
		hopper.setGraphHopperLocation("data/graph-cache");
		hopper.setProfiles(List.of(new Profile("car").setVehicle("car").setWeighting("fastest")));
		hopper.getCHPreparationHandler().setCHProfiles(List.of(new CHProfile("car")));
		hopper.importOrLoad();
		
		return hopper;
	}
}
