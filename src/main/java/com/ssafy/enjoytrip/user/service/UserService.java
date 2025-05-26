package com.ssafy.enjoytrip.user.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.user.dao.IUserDAO;
import com.ssafy.enjoytrip.user.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	
	private final IUserDAO userDAO;

	@Override
	public int insert(UserDTO user) {
		try {
			return userDAO.insert(user);
		} catch (DuplicateKeyException e) {
			String msg = e.getMessage();
			if (msg.contains("username_UNIQUE")) {
				throw new DuplicateKeyException("중복된 아이디입니다.");
			} else if (msg.contains("email_UNIQUE")) {
				throw new DuplicateKeyException("중복된 이메일입니다.");
			} else {
				throw new RuntimeException("Unknown duplicate key");
			}
		}
	}

	@Override
	public int update(UserDTO user) {
		try {
			System.out.println(user.getId());
			return userDAO.update(user);
		} catch (DuplicateKeyException e) {
			String msg = e.getMessage();
			if (msg.contains("username_UNIQUE")) {
				throw new DuplicateKeyException("중복된 아이디입니다.");
			} else if (msg.contains("email_UNIQUE")) {
				throw new DuplicateKeyException("중복된 이메일입니다.");
			} else {
				throw new RuntimeException("Unknown duplicate key");
			}
		}
	}
	
	@Override
	public int delete(int id) {
		return userDAO.delete(id);
	}

	@Override
	public UserDTO login(String email, String password) {
		UserDTO user = userDAO.getUserByEmail(email);
		
		return user != null && password.equals(user.getPassword())? user : null;
	}
	
	
}
