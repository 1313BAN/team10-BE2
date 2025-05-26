<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>로그인</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <style>
    body {
      background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)), 
                  url('${pageContext.request.contextPath}/img/main.jpg') no-repeat center center/cover;
      height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: 'Segoe UI', sans-serif;
    }
    .login-card {
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(10px);
      border-radius: 16px;
      padding: 2.5rem;
      color: white;
      width: 100%;
      max-width: 400px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    }
    .login-card input {
      background-color: rgba(255,255,255,0.2);
      color: white;
      border: none;
    }
    .login-card input::placeholder {
      color: rgba(255,255,255,0.7);
    }
    .login-card a {
      color: #ffc107;
      text-decoration: none;
    }
    .login-card a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="login-card">
    <h2 class="text-center mb-4">로그인</h2>
    <form method="post" action="${root}/user/login">
      <div class="mb-3">
        <label for="email" class="form-label">이메일</label>
        <input type="text" class="form-control" id="email" name="email" placeholder="you@example.com" required>
      </div>
      <div class="mb-3">
        <label for="password" class="form-label">비밀번호</label>
        <input type="password" class="form-control" id="password" name="password" placeholder="••••••••" required>
      </div>
      <button type="submit" class="btn btn-warning w-100">로그인</button>
    </form>

    <div class="d-flex justify-content-between mt-3">
      <a href="${root}/user/passwordForm">비밀번호 찾기</a>
      <a href="${root}/user/signUpForm">회원가입</a>
    </div>
  </div>

  <script>
	fetch("/api/user/login", {
		method: "POST"
	})
  </script>
</body>
</html>
