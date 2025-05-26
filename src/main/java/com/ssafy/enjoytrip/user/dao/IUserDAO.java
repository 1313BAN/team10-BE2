package com.ssafy.enjoytrip.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.user.dto.UserDTO;

@Mapper
public interface IUserDAO {
	
	int insert(UserDTO user);
	int update(UserDTO user);
	int delete(int id);
	UserDTO getUserByEmail(@Param("email") String email);
}
