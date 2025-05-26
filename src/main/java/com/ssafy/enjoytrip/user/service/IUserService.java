package com.ssafy.enjoytrip.user.service;

import com.ssafy.enjoytrip.user.dto.UserDTO;

public interface IUserService {

	int insert(UserDTO user);
	int update(UserDTO user);
	int delete(int id);
	UserDTO login(String email, String password);
}
