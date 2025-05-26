<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>마이페이지</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

  <style>
    body {
      background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)),
                  url('${pageContext.request.contextPath}/img/main.jpg') no-repeat center center/cover;
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: 'Segoe UI', sans-serif;
    }

    .mypage-card {
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(12px);
      border-radius: 20px;
      padding: 2.5rem;
      color: white;
      width: 100%;
      max-width: 500px;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
    }

    .form-control {
      background-color: rgba(255,255,255,0.2);
      color: white;
      border: none;
    }

    .form-control::placeholder {
      color: rgba(255,255,255,0.7);
    }

    label {
      font-weight: 500;
      margin-top: 1rem;
    }

    .btn-group {
      display: flex;
      justify-content: space-between;
      gap: 0.5rem;
      margin-top: 2rem;
    }

    #backBtn {
      margin-top: 1.5rem;
    }
  </style>
</head>
<body>

<%
    String errorMsg = (String) request.getAttribute("error");
%>

<div class="mypage-card">
  <h2 class="text-center mb-4">마이페이지</h2>

  <form method="post" id="editProfileForm">
    <label for="userId">아이디</label>
    <input type="text" name="userId" id="userId" value="${user.userId}" class="form-control" readonly/>

    <label for="email">이메일</label>
    <input type="text" name="email" id="email" value="${user.email}" class="form-control" placeholder="이메일" required>

    <label for="name">이름</label>
    <input type="text" name="name" id="name" value="${user.name}" class="form-control" placeholder="이름" required>

    <label for="password">비밀번호</label>
    <input type="password" name="password" id="password" value="${user.password}" class="form-control" placeholder="비밀번호" required>

    <label for="confirmPassword">비밀번호 확인</label>
    <input type="password" id="confirmPassword" value="${user.password}" class="form-control" placeholder="비밀번호 확인" required>
    
    <div class="mb-3">
      <div class="btn-group w-100">
        <input type="submit" id="updatebtn" value="수정" formaction="${root}/user/update" class="btn btn-warning w-50">
        <input type="submit" value="삭제" formaction="${root}/user/delete" class="btn btn-danger w-50">
      </div>
    </div>
  </form>

  <a href="${root}/main" class="btn btn-secondary w-100 d-block py-2">돌아가기</a>
</div>

<script>
  document.getElementById("updatebtn").addEventListener("click", function (event) {

    const pw = document.getElementById("password").value;
    const cpw = document.getElementById("confirmPassword").value;

    if (pw !== cpw) {
    	event.preventDefault();
      alert("비밀번호가 일치하지 않습니다.");
    }
  });

  <% if (errorMsg != null) { %>
    alert("<%= errorMsg %>");
  <% } %>
</script>

</body>
</html>
