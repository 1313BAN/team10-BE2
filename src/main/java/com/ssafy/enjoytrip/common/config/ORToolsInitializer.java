package com.ssafy.enjoytrip.common.config;

import org.springframework.stereotype.Component;

import com.google.ortools.Loader;

import jakarta.annotation.PostConstruct;

@Component
public class ORToolsInitializer {

	@PostConstruct
	public void init() {
		Loader.loadNativeLibraries();
		System.out.println("OR-Tools initialized...");
	}
}
