package com.example.demo.web;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.http.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.User;
import com.example.demo.data.JdbcUserRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/login")
@Controller
public class LoginController {
	private final JdbcUserRepository userJdbc;
	@Autowired
	public LoginController(JdbcUserRepository userJdbc) {
		this.userJdbc = userJdbc;
	}
	
	@GetMapping
	public String showFormLogin(Model model) {
		log.info("ok123");
		model.addAttribute("user", new User());
		return "login";
	}
	@PostMapping
	public String processLogin( User user,  Errors errors, HttpServletRequest request) throws ServletException, IOException {
		User gettedUser = userJdbc.isRightUser(user);
		request.getSession().setAttribute("user", gettedUser);
		log.info("getted User " + gettedUser.getUserID());
		if(gettedUser != null) {
			return "redirect:/store/current";
		}
		else {
			log.info("Not exist this user");
			return "login";
		}
	}
}
