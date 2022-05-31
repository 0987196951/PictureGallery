package com.example.demo.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SessionAttributeMethodArgumentResolver;

import com.example.demo.Picture;
import com.example.demo.PicturePackage;
import com.example.demo.Room;
import com.example.demo.User;
import com.example.demo.data.JdbcPictureRepository;
import com.example.demo.data.JdbcRoomRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/store")
public class StoreController {
	private JdbcPictureRepository jdbcPicture;
	private JdbcRoomRepository jdbcRoom;
	private JdbcPictureRepository jdbcPic;
	@Autowired
	public StoreController(JdbcPictureRepository jdbcPicture, JdbcRoomRepository jdbcRoom, JdbcPictureRepository jdbcPic) {
		this.jdbcPicture = jdbcPicture;
		this.jdbcRoom = jdbcRoom;
		this.jdbcPic = jdbcPic;
	}
	@GetMapping("/current")
	public String displayStore( Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		log.info(user.getUserID());
		List<Room> listRoom = jdbcRoom.findAllwithUser(user.getUserID());
		String msg = (String) session.getAttribute("msg");
		model.addAttribute("msg", msg);
		model.addAttribute("rooms", listRoom);
		model.addAttribute("user", user);
		model.addAttribute("room", new Room());
		return "store";	
	}
	@PostMapping("/add_room")
	public String addRoom(Room room, Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		log.info(user.getUserID());
		Room gettedRoom = jdbcRoom.addRoom(room, user.getUserID());
		if(gettedRoom != null) {
			session.setAttribute("msg", "add room sucessfully");
			session.setAttribute("gettedRoom", gettedRoom);
		}
		else {
			session.setAttribute("msg", "room is existed");
		}
		return "redirect:/store/current";
	}
	@PostMapping("/access_room")
	public String accessRoom(Room room, Model model, HttpSession session, HttpServletRequest request) {
		User user = (User)session.getAttribute("user");
		Room gettedRoom = jdbcRoom.isRoom(room);
		if(gettedRoom != null) {
			jdbcRoom.addAccess(user.getUserID(),room.getRoomID());
			session.setAttribute("room", gettedRoom);
			session.setAttribute("msg", "");
			return "redirect:/room/current";
		}
		session.setAttribute("msg", "password wrong");
		return "redirect:/store/current";
	}
	@PostMapping("/search_room")
	public String searchRoom(Room room, Model model, HttpSession session, HttpServletRequest request) {
		List<Room> listRoom = jdbcRoom.findByName(room.getName());
		User user = (User) session.getAttribute("user");
		model.addAttribute("rooms", listRoom);
		model.addAttribute("user", user);
		model.addAttribute("room", new Room());
		return "store";
	}
}
