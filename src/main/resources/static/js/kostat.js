let accessToken;
const map = sop.map("map");
// marker 목록
const markers = [];
// 경계 목록
const bounds = [];

// access token 가져오기
const getAccessToken = async () => {
  try {
    const json = await getFetch(
      "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json",
      {
        consumer_key: "7184a46e621c433fa95f", // 서비스 id
        consumer_secret: "9a7a539376a44440a4a5", // 보안 key
      }
    );
    accessToken = json.result.accessToken;
  } catch (e) {
    console.log(e);
  }
};

// 주소를 UTM-K좌표로 변환해서 반환: - json의 errCd ==-401에서 access token 확보!!
const getCoords = async (address) => {
  try {
    const json = await getFetch(
      "https://sgisapi.kostat.go.kr/OpenAPI3/addr/geocode.json",
      {
        accessToken: accessToken,
        address: address,
        resultcount: 1,
      }
    );
    if (json.errCd === -401) {
      await getAccessToken();
      return await getCoords(address);
    }
    return json.result.resultdata[0];
  } catch (e) {
    console.log(e);
  }
};

const updateMap = (infos) => {
  resetMarker();
  try {
    for (let i = 0; i < infos.length; i++) {
      const info = infos[i];
      const marker = sop.marker([info.utmk.x, info.utmk.y]);
      const content = `
        <div
          style="
            font-family: dotum, arial, sans-serif;
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 5px;
          "
        >
          ${info.label}
        </div>
        <table style="border-spacing: 2px; border: 0px">
          <tbody>
            <tr>
              <td><img src=${info.firstimage2} style="width: 50%; border-radius: 4px"/></td>
            </tr>
            <tr>
              <td style="color: #767676; padding-right: 12px">주소</td>
              <td><span>${info.address}</span></td>
            </tr>
            <tr>
              <td style="color: #767676; padding-right: 12px">우편번호</td>
              <td><span>${info.zipcode}</span></td>
            </tr>
          </tbody>
        </table>
      `;
      marker.addTo(map).bindInfoWindow(content);
      markers.push(marker);
      bounds.push([info.utmk.x, info.utmk.y]);
    }
    // 경계를 기준으로 map을 중앙에 위치하도록 함
    if (bounds.length > 1) {
      map.setView(
        map._getBoundsCenterZoom(bounds).center,
        map._getBoundsCenterZoom(bounds).zoom
      );
    } else {
      map.setView(map._getBoundsCenterZoom(bounds).center, 9);
    }
  } catch (e) {
    console.log(e);
  }
};

// 마커와 경계 초기화
const resetMarker = () => {
  markers.forEach((item) => item.remove());
  bounds.length = 0;
};
