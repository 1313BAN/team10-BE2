<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>회원가입</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <style>
    body {
      background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)),
                  url('${pageContext.request.contextPath}/img/main.jpg') no-repeat center center/cover;
      height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: 'Segoe UI', sans-serif;
    }
    .signup-card {
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(10px);
      border-radius: 16px;
      padding: 2.5rem;
      color: white;
      width: 100%;
      max-width: 450px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    }
    .signup-card input {
      background-color: rgba(255,255,255,0.2);
      color: white;
      border: none;
    }
    .signup-card input::placeholder {
      color: rgba(255,255,255,0.7);
    }
  </style>
</head>
<body>

<%
    String errorMsg = (String) request.getAttribute("error");
%>

<div class="signup-card">
  <h2 class="text-center mb-4">회원가입</h2>
  <form method="post" id="signupForm" action="${root}/user/insert">
    <div class="mb-3">
      <label for="userId" class="form-label">아이디</label>
      <input type="text" class="form-control" name="userId" id="userId" placeholder="아이디 입력" required>
    </div>
    <div class="mb-3">
      <label for="email" class="form-label">이메일</label>
      <input type="text" class="form-control" name="email" id="email" placeholder="you@example.com" required>
    </div>
    <div class="mb-3">
      <label for="name" class="form-label">이름</label>
      <input type="text" class="form-control" name="name" id="name" placeholder="이름 입력" required>
    </div>
    <div class="mb-3">
      <label for="password" class="form-label">비밀번호</label>
      <input type="password" class="form-control" name="password" id="password" placeholder="비밀번호 입력" required>
    </div>
    <div class="mb-4">
      <label for="confirmPassword" class="form-label">비밀번호 확인</label>
      <input type="password" class="form-control" id="confirmPassword" placeholder="다시 입력" required>
    </div>
    <button type="submit" class="btn btn-warning w-100">회원가입</button>
  </form>
</div>

<script>
  document.getElementById("signupForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const pw = document.getElementById("password").value;
    const cpw = document.getElementById("confirmPassword").value;

    if (pw !== cpw) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }
    
    this.submit();
  });

  <% if (errorMsg != null) { %>
    alert("<%= errorMsg %>");
  <% } %>
</script>

</body>
</html>
