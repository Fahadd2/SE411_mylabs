package edu.psu.se411.lab16.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** HTML pages (templates/*.html). Who may open each page is decided in WebSecurityConfig. */
@Controller
public class PagesController {

	/** Public home page */
	@GetMapping("/")
	public String home() {
		return "index";
	}

	/** Role USER only */
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	/** Role ADMIN only */
	@GetMapping("/admindashboard")
	public String adminDashboard() {
		return "adminDashboard";
	}
}
