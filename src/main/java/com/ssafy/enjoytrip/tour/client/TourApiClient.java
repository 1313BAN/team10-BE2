package com.ssafy.enjoytrip.tour.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.enjoytrip.tour.dao.ITourDAO;
import com.ssafy.enjoytrip.tour.dto.AreaDTO;
import com.ssafy.enjoytrip.tour.dto.SigunguDTO;
import com.ssafy.enjoytrip.tour.dto.TourDTO;

import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class TourApiClient implements ITourDAO {

	@Value("${tourism.api.url}")
	private String API_URL;
	
	@Value("${tourism.api.key}")
	private String API_KEY;
	
	private final RestTemplate restTemplate;
	
	public List<AreaDTO> getAreaList() {
		String url = API_URL + "/areaCode1"
				+ "?serviceKey=" + API_KEY
				+ "&MobileOS=ETC"
				+ "&MobileApp=EnjoyTrip"
				+ "&_type=json";
		
		JsonNode itemsNode = restTemplate.getForObject(url, JsonNode.class)
				.path("response").path("body").path("items").path("item");

		List<AreaDTO> result = new ArrayList<>();
		
		if (itemsNode.isArray()) {
			for (JsonNode item : itemsNode) {
				result.add(AreaDTO.builder()
						.code(item.path("code").asInt())
						.name(item.path("name").asText())
						.build());
			}
		} else if (!itemsNode.isMissingNode()) {
			result.add(AreaDTO.builder()
					.code(itemsNode.path("code").asInt())
					.name(itemsNode.path("name").asText())
					.build());
		}
		
		return result;
	}
	
	public List<SigunguDTO> getSigunguList(Integer areaCode) {
		String url = API_URL + "/areaCode1"
				+ "?serviceKey=" + API_KEY
				+ "&areaCode=" + areaCode
				+ "&MobileOS=ETC"
				+ "&MobileApp=EnjoyTrip"
				+ "&_type=json";
		
		JsonNode itemsNode = restTemplate.getForObject(url, JsonNode.class)
				.path("response").path("body").path("items").path("item");

		List<SigunguDTO> result = new ArrayList<>();

		if (itemsNode.isArray()) {
			for (JsonNode item : itemsNode) {
				result.add(SigunguDTO.builder()
						.code(item.path("code").asInt())
						.name(item.path("name").asText())
						.build());
			}
		} else if (!itemsNode.isMissingNode()) {
			result.add(SigunguDTO.builder()
					.code(itemsNode.path("code").asInt())
					.name(itemsNode.path("name").asText())
					.build());
		}
		
		return result;
	}
	
	public List<TourDTO> getTourDatum(int areaCode, Integer sigunguCode, Integer contentTypeId) {
		String url = API_URL + "/areaBasedList1"
				+ "?serviceKey=" + API_KEY
				+ "&areaCode=" + areaCode
				+ "&MobileOS=ETC"
				+ "&MobileApp=EnjoyTrip"
				+ "&_type=json";
		
		if (sigunguCode != null) url += "&sigunguCode=" + sigunguCode;
		if (contentTypeId != null) url += "&contentTypeId" + contentTypeId;
		
		JsonNode itemsNode = restTemplate.getForObject(url, JsonNode.class)
				.path("response").path("body").path("items").path("item");
		
		List<TourDTO> result = new ArrayList<>();

		if (itemsNode.isArray()) {
			for (JsonNode item : itemsNode) {
				result.add(TourDTO.builder()
						.addr1(item.path("addr1").asText())
						.addr2(item.path("addr2").asText())
						.contentId(item.path("contentid").asInt())
						.contentTypeId(item.path("contenttypeid").asInt())
						.image(item.path("firstimage").asText())
						.imageThumbnail(item.path("firstimage2").asText())
						.latitude(item.path("mapx").asDouble())
						.longitude(item.path("mapy").asDouble())
						.title(item.path("title").asText())
						.build());
			}
		} else if (!itemsNode.isMissingNode()) {
			result.add(TourDTO.builder()
					.addr1(itemsNode.path("addr1").asText())
					.addr2(itemsNode.path("addr2").asText())
					.contentId(itemsNode.path("contentid").asInt())
					.contentTypeId(itemsNode.path("contenttypeid").asInt())
					.image(itemsNode.path("firstimage").asText())
					.imageThumbnail(itemsNode.path("firstimage2").asText())
					.latitude(itemsNode.path("mapx").asDouble())
					.longitude(itemsNode.path("mapy").asDouble())
					.title(itemsNode.path("title").asText())
					.build());
		}
		
		return result;
	}
	
	public TourDTO getAttractionByContentId(int contentId) {
		String url = API_URL + "/detailCommon1"
				+ "?serviceKey=" + API_KEY
				+ "&contentId=" + contentId
				+ "&defaultYN=Y"
				+ "&firstImageYN=Y"
				+ "&addrinfoYN=Y"
				+ "&mapinfoYN=Y"
				+ "&MobileOS=ETC"
				+ "&MobileApp=EnjoyTrip"
				+ "&_type=json";

		JsonNode itemsNode = restTemplate.getForObject(url, JsonNode.class)
				.path("response").path("body").path("items").path("item");

		if (!itemsNode.isArray()) return null;
		
		return TourDTO.builder()
						.addr1(itemsNode.path("addr1").asText())
						.addr2(itemsNode.path("addr2").asText())
						.contentId(itemsNode.path("contentid").asInt())
						.image(itemsNode.path("firstimage").asText())
						.imageThumbnail(itemsNode.path("firstimage2").asText())
						.latitude(itemsNode.path("mapx").asDouble())
						.longitude(itemsNode.path("mapy").asDouble())
						.title(itemsNode.path("title").asText())
						.build();
	}

	
	@Override
	public int updateRatingInfo(TourDTO attractions) {
		return 0;
	}

	@Override
	public int updateCafe(TourDTO attraction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TourDTO getTourDataByContentId(int contentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertOpeningPeriods(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TourDTO getTourDataByPlaceId(String placeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TourDTO> findNearby(double latMin, double latMax, double lngMin, double lngMax) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updatePlaceId(TourDTO tourData) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(TourDTO tourData) {
		// TODO Auto-generated method stub
		return 0;
	}
}
