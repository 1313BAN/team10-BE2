package com.ssafy.enjoytrip.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserDTO {
	
	private int id;
	private String username;
	private String email;
	private String password;
	private String name;
	private String role;
}
