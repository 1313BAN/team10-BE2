package com.ssafy.enjoytrip.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping({"/", "/main"})
	public Object index() {
		return "index";
	}
}
