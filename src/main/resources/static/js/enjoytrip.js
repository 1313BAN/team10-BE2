const area = document.querySelector("#areaCode");
const sigungu = document.querySelector("#sigunguCode");
const contentType = document.querySelector("#contentType");

document.querySelector("#btn_trip_search").addEventListener("click", async () => {
  const queryObj = {
    serviceKey: key_data,
    numOfRows: 20,
    pageNo: 1,
    MobileOS: "ETC",
    MobileApp: "Test",
    _type: "json",
  };
  // 추가로 설정할 조건이 있다면 추가하기
  if (area.value) {
    queryObj.areaCode = area.value;
  }
  if (sigungu.value) {
    queryObj.sigunguCode = sigungu.value;
  }
  if (contentType.value) {
    queryObj.contentTypeId = contentType.value;
  }

  try {
    const json = await getFetch("https://apis.data.go.kr/B551011/KorService1/areaBasedList1", queryObj);

    const spots = json.response.body.items.item;
    spots.forEach((element) => {
      // 기본적으로 통계청의 SGIS map은 utmk 기반이므로 WG384(lat, lng)기반을 utmk 로 변경
      element.utmk = new sop.LatLng(element.mapy, element.mapx);
      element.address = element.addr1;
      element.label = element.title;
    });
    updateMap(spots);
	renderAttractionList(spots);
  } catch (e) {
    console.log(e);
  }
});

const selectedSpots = [];
// ✅ 관광지 리스트를 오른쪽에 렌더링
const renderAttractionList = (spots) => {
  const container = document.getElementById("attractionList");
  container.innerHTML = "";

  if (!spots || spots.length === 0) {
    container.innerHTML = "<p>해당 조건에 맞는 관광지를 찾을 수 없습니다.</p>";
    return;
  }

  spots.forEach((spot) => {
    const div = document.createElement("div");
    div.className = "attraction-item";
	
	const isSelected = selectedSpots.some(
		(s) => s.contentid === spot.contentid
	);
    div.innerHTML = `
		<input type="checkbox" class="select-attraction"
		  data-title="${spot.title}"
		  data-lat="${spot.mapy}"
		  data-lng="${spot.mapx}"
		  data-contentid="${spot.contentid}"
		  ${isSelected ? "checked" : ""}>
		<div class="attraction-info">
		  <h3>${spot.title || "제목 없음"}</h3>
		  <p>${spot.addr1 || ""} ${spot.addr2 || ""}</p>
		</div>
    `;
	
	const checkbox = div.querySelector(".select-attraction");
	
	checkbox.addEventListener("change", () => {
		const title = checkbox.dataset.title;
		const lat = parseFloat(checkbox.dataset.lat);
		const lng = parseFloat(checkbox.dataset.lng);
	
		if (checkbox.checked) {
			selectedSpots.push({
			  contentid: spot.contentid,
			  title,
			  latitude: lat,
			  longitude: lng
			});
		} else {
		  const idx = selectedSpots.findIndex(
		    (s) => s.contentid === spot.contentid
		  );
		  if (idx !== -1) selectedSpots.splice(idx, 1);
		}
	
		div.classList.toggle("checked", checkbox.checked);
		updateSelectedList(); // 다시 렌더링
	});
	
	div.classList.toggle("checked", isSelected);
    container.appendChild(div);
  });
};

const updateSelectedList = () => {
  const container = document.getElementById("selectedList");
  container.innerHTML = "";

  selectedSpots.forEach((spot, idx) => {
    const item = document.createElement("div");
    item.className = "selected-item";
    item.innerHTML = `
      <div class="item-content">
        <h4>${spot.title}</h4>
        <button class="remove-selected" data-index="${idx}">❌</button>
      </div>
    `;
    container.appendChild(item);
  });

  document.querySelectorAll(".remove-selected").forEach((btn) => {
    btn.addEventListener("click", (e) => {
      const idx = parseInt(btn.dataset.index);
      const removed = selectedSpots[idx];
      selectedSpots.splice(idx, 1);

      // 현재 렌더링된 attractionList 내 체크박스도 업데이트
      document.querySelectorAll(".select-attraction").forEach((checkbox) => {
		if (
		  checkbox.dataset.contentid === removed.contentid
		) {
		  checkbox.checked = false;
		  checkbox.closest(".attraction-item")?.classList.remove("checked");
		}
      });

      updateSelectedList();
    });
  });
};

document.querySelector("#btn_recommend_route").addEventListener("click", async () => {
  if (selectedSpots.length < 2) {
    alert("2개 이상 관광지를 선택해주세요!");
    return;
  }

  try {
    const response = await fetch("recommendRoute", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(selectedSpots),
    });

    const result = await response.json(); // 나중에 추천 경로 받으면 여기에 뜸
    console.log("추천 경로 결과:", result);

	// 🔽 추천 경로 리스트 출력
	const routeContainer = document.getElementById("recommendedRoute");
	routeContainer.innerHTML = "<h3>추천 경로</h3>";

	result.forEach((spot, idx) => {
	  const div = document.createElement("div");
	  div.className = "route-item";
	  div.innerHTML = `
	    <div><strong>${idx + 1}</strong>. ${spot.title}</div>
	    <div style="font-size: 0.9em; color: #666;">${spot.addr1 || ""} ${spot.addr2 || ""}</div>
	  `;
	  routeContainer.appendChild(div);
	});
  } catch (e) {
    console.error("추천 요청 중 오류 발생:", e);
  }
});


// ✅ 페이지 로드 시 초기화 함수
const init = async () => {
  await areaCode1();
  /*
  updateMap([
    {
      address: "서울특별시 강남구 테헤란로 212",
      utmk: await getCoords("서울특별시 강남구 테헤란로 212"),
      label: "멀티캠퍼스",
    },
  ]);
  */
};

init();