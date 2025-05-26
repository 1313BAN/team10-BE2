<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Enjoy Trip</title>
	
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- AOS (스크롤 애니메이션) -->
    <link href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" rel="stylesheet">
    
	<style>
	    body, html {
	        margin: 0;
	        padding: 0;
	        height: 100%;
	    }
	
	    .hero-section {
	        height: 100vh;
	        background: url('${root}/img/main.jpg') no-repeat center center/cover;
	        position: relative;
	        opacity: 0;
	        animation: fadeInBg 2s ease-out forwards;
	        margin-top: -70px;
	    }
	
	    .hero-text {
	        position: absolute;
	        top: 50%;
	        left: 50%;
	        transform: translate(-50%, 30px);
	        text-align: center;
	        color: white;
	        opacity: 0;
	        animation: fadeUpText 1.5s ease-out 1.5s forwards; /* 텍스트는 배경보다 약간 늦게 */
	    }
	
	    @keyframes fadeInBg {
	        from { opacity: 0; }
	        to { opacity: 1; }
	    }
	
	    @keyframes fadeUpText {
	        from {
	            transform: translate(-50%, 30px);
	            opacity: 0;
	        }
	        to {
	            transform: translate(-50%, -50%);
	            opacity: 1;
	        }
	    }
	
	    .hero-text h1 {
	        font-size: 3.5rem;
	        font-weight: bold;
	        text-shadow: 2px 2px 10px rgba(0,0,0,0.5);
	    }
	</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/menu.jsp"/>
	
	<section class="hero-section" id="home">
	    <div class="hero-text">
	        <h1>당신의 여행을 설계하세요</h1>
	    </div>
	</section>
	
	<section id="tourist-section" class="py-5">
	    <div class="container">
	        <!-- 필터 영역 -->
	        <div class="card p-4 mb-5">
	            <div class="row g-3">
	                <div class="col-md-4">
	                    <select class="form-select" name="areaCode" id="areaCode">
	                    	<option selected disabled>시도 선택</option>
	                    </select>
	                </div>
	                <div class="col-md-4">
	                    <select class="form-select" name="sigunguCode" id="sigunguCode">
		                	<option selected disabled>시군구 선택</option>
		                </select>
	                </div>
	                <div class="col-md-4">
	                    <select class="form-select" name="contentType" id="contentType">
			                <option selected disabled>관광타입 선택</option>
			                <option value="12">관광지</option>
			                <option value="14">문화시설</option>
			                <option value="15">축제공연행사</option>
			                <option value="25">여행코스</option>
			                <option value="28">레포츠</option>
			                <option value="32">숙박</option>
			                <option value="38">쇼핑</option>
			                <option value="39">음식점</option>
	                    </select>
	                </div>
	            </div>
	            <div class="d-flex justify-content-end mt-3">
	                <button type="button" class="btn btn-primary" id="btn_trip_search">조회</button>
	            </div>
	        </div>
	
	        <!-- 지도 + 관광지 리스트 -->
	        <div class="row">
	            <div class="col-md-6">
	                <div id="map" style="height: 400px; border-radius: 15px; overflow: hidden;"></div>
	            </div>
	            <div class="col-md-6">
	                <div id="attractionList" class="d-flex flex-column gap-3">
	                    <!-- 관광지 카드들 -->
	                </div>
	            </div>
	        </div>
	    </div>
	</section>
	
	<section id="plan-section" class="py-5 bg-light">
	    <div class="container text-center">
	
	        <h2 class="mb-4">여행 경로 계획</h2>
	        <p class="mb-5">여행지를 선택하고, 경로를 추천받아보세요.</p>
	
	        <div id="selectedList" class="d-flex flex-wrap gap-3 justify-content-center">
	            <!-- 선택한 관광지 카드들 -->
	        </div>
	
	        <button id="btn_recommend_route" class="btn btn-success btn-lg mt-4">경로 추천하기</button>
	
	        <div id="recommendedRoute" class="mt-5">
	            <!-- 추천 경로 표시 -->
	        </div>
	
	    </div>
	</section>
	<script>
	const areaSelect = document.getElementById("areaCode");
	const sigunguSelect = document.getElementById("sigunguCode");
	
	document.addEventListener("DOMContentLoaded", async function() {
		try {
			const response = await fetch("/attraction/area");
			const areaList = await response.json();
			
			areaList.forEach(area => {
				const option = document.createElement("option");
				option.value = area.code;
				option.textContent = area.name;
				areaSelect.appendChild(option);
			});
		} catch (e) {
			console.error("지역 목록 로딩 실패", e);
		}
	});
	
	areaSelect.addEventListener("change", async function () {
		sigunguSelect.length = 1;
		try {
			const areaCode = areaSelect.value;
			const response = await fetch("/attraction/sigungu?areaCode=" + areaCode);
			const sigunguList = await response.json();
			
			sigunguList.forEach(sigungu => {
				const option = document.createElement("option");
				option.value = sigungu.code;
				option.textContent = sigungu.name;
				sigunguSelect.appendChild(option);
			});
		} catch (e) {
			console.error("지역 목록 로딩 실패", e);
		}
	
	});
	
	document.getElementById("btn_trip_search").addEventListener("click", async fuction() {
	    const areaCode = document.getElementById("areaCode").value;
	    const sigunguCode = document.getElementById("sigunguCode").value;
	    const contentType = document.getElementById("contentType").value;

	    const query = new URLSearchParams({
	        areaCode,
	        sigunguCode,
	        contentType
	    });

	    const response = await fetch(`/attraction/search?${query.toString()}`);
	    const resultList = await response.json();  // 서버에서 관광지 리스트 JSON으로 반환한다고 가정

	    const container = document.getElementById("attractionList");
	    container.innerHTML = ""; // 기존 리스트 초기화

	    resultList.forEach(item => {
	        const card = document.createElement("div");
	        card.className = "card p-3";
	        card.innerHTML = `<h5>${item.title}</h5><p>${item.addr1}</p>`;
	        container.appendChild(card);
	    });
	});
	</script>
	<!--<script src="${root}/js/enjoytrip.js"></script> -->
</body>
</html>