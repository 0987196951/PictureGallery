package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.User;
import com.example.demo.data.JdbcUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/sign_up")
@Controller
public class SignUpController {
	private final JdbcUserRepository jdbcUser;
	@Autowired
	public SignUpController(JdbcUserRepository jdbcUser) {
		this.jdbcUser = jdbcUser;
	}
	@GetMapping
	public String showFormSignUp(Model model) {
		log.info("log user");
		model.addAttribute(new User());
		return "sign_up";
	}
	@PostMapping
	public String createUser(User user, Model model) {
		User checkedUser = jdbcUser.isRightUser(user);
		if(checkedUser == null) {
			log.info("save user");
			jdbcUser.save(user);
			model.addAttribute("msg", "sign up sucessfully");
			model.addAttribute("user", user);
			return "login";
		}
		else {
			model.addAttribute("msg", "user is existed");
			return "sign_up";
		}
	}
	
}
