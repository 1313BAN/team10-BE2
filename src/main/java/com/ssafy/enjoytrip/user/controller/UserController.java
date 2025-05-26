package com.ssafy.enjoytrip.user.controller;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.user.dto.UserDTO;
import com.ssafy.enjoytrip.user.service.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	private final IUserService userService;
	
	@PostMapping("/login")
	protected ResponseEntity<?> login(
			@RequestBody Map<String, String> payload, 
			HttpSession session) 
			throws Exception {
		
		String email = payload.get("email");
		String password = payload.get("password");
		
		UserDTO user = userService.login(email, password);
		if (user != null) {
			session.setAttribute("user", user);
			
			return ResponseEntity.ok(Map.of("message", "로그인 성공", "user", user));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "아이디 또는 비밀번호가 일치하지 않습니다."));
		}
	}
	
	@PostMapping({"/signup", "/insert", "/register"})
	protected ResponseEntity<?> insert(@RequestBody UserDTO user) {
		try {
			userService.insert(user);
			return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
		} catch (DuplicateKeyException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
		}
	}
	
	@PutMapping
	protected ResponseEntity<?> update(@RequestBody UserDTO user, HttpSession session) {
		try {
			System.out.println(user.getId());
			userService.update(user);
			session.setAttribute("user", user);
			return ResponseEntity.ok(Map.of("message", "수정 성공", "user", user));
		} catch (DuplicateKeyException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
		}
	}

	@DeleteMapping
	protected ResponseEntity<?> delete(HttpSession session) {
		UserDTO user = (UserDTO) session.getAttribute("user");
		session.invalidate();
		userService.delete(user.getId());
		
		return ResponseEntity.ok(Map.of("message", "삭제 성공"));
	}
	
	@PostMapping("/logout")
	protected ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		
		return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
	}

}

