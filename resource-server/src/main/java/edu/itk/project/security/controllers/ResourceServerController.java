package edu.itk.project.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceServerController {

	@GetMapping("/")
	public String index() {
		return "Hello, World!";
	}
	
}
