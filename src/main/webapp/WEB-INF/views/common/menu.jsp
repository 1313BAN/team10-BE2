<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<style>
.custom-navbar {
    background-color: rgba(255, 255, 255, 0.15); /* 아주 옅은 흰색 */
    backdrop-filter: blur(15px);               /* 배경 블러 효과 */
    -webkit-backdrop-filter: blur(10px);       /* Safari 지원 */
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    transition: background-color 0.3s ease;
}

.custom-navbar .nav-link {
    color: white;
    font-weight: 500;
    transition: color 0.2s ease;
}

.custom-navbar .nav-link:hover {
    color: #ffd369;
}
</style>
<nav class="navbar navbar-expand-lg sticky-top custom-navbar">
    <div class="container-fluid">
        <a class="navbar-brand mx-3 text-white fw-bold" href="${root}/">EnjoyTrip!</a>
        
		<!-- 토글 버튼 (작은 화면에서 메뉴 펼치기용) -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="#tourist-section">지역별 관광지</a></li>
                <li class="nav-item"><a class="nav-link" href="#plan-section">여행코스</a></li>
		        <c:choose>
		        	<c:when test="${empty userId}">
				        <!-- 로그인 되지 않았을 경우 -->
				        <li class="nav-item"><a class="nav-link" href="${root}/user/loginForm">로그인</a>
		        	</c:when>
		        	<c:otherwise>
				        <!-- 로그인 되었을 경우 -->
					    <li class="nav-item dropdown">
					      <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="profileDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
					        <!-- 프로필 이미지 (기본 이미지 or 사용자 이미지) -->
					        <img src="${root}/img/default-profile.jpg" alt="프로필" width="25" height="25" class="rounded-circle border border-white" />
					      </a>
					      <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
					        <li><a class="dropdown-item" href="${root}/user/mypage">마이페이지</a></li>
					        <li><a class="dropdown-item" href="${root}/user/logout">로그아웃</a></li>
					      </ul>
					    </li>	        
		        	</c:otherwise>
		        </c:choose>
            </ul>
        </div>
    </div>
</nav>