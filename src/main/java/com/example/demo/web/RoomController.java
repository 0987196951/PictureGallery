package com.example.demo.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Picture;
import com.example.demo.PicturePackage;
import com.example.demo.Room;
import com.example.demo.data.JdbcPictureRepository;
import com.example.demo.data.JdbcRoomRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/room")
public class RoomController {
	private JdbcPictureRepository jdbcPicture;
	private JdbcRoomRepository jdbcRoom;
	@Autowired
	public RoomController(JdbcPictureRepository jdbcPicture, JdbcRoomRepository jdbcRoom) {
		this.jdbcPicture = jdbcPicture;
	}
	@GetMapping("/current")
	public String showRoom(Model model, HttpSession session, HttpServletRequest request) {
		Room room = (Room) session.getAttribute("room");
		@SuppressWarnings("unchecked")
		List<Picture> listPictures = (List<Picture>) session.getAttribute("pictures");
		if(listPictures == null) {
			listPictures = jdbcPicture.getPictureOfRoom(room.getRoomID());
			session.setAttribute("pictures", listPictures);
		}
		model.addAttribute("pictures", listPictures);
		model.addAttribute("room", room);
		model.addAttribute("picture", new PicturePackage());
		return "room";
	}
	@PostMapping("/add_picture")
	public String addPicture(PicturePackage picturePackage, Model model, HttpSession session
			, HttpServletRequest request) throws IOException {
		Room room = (Room) session.getAttribute("room"); 
		Picture savedPic = jdbcPicture.save(picturePackage, room.getRoomID());
		@SuppressWarnings("unchecked")
		List<Picture> listPictures = (List<Picture>) session.getAttribute("pictures");
		listPictures.add(savedPic);
		request.getSession().setAttribute("pictures", listPictures);
		return "redirect:/room/current";
	}
	@GetMapping("/del/{id}")
	public String delPicture(@PathVariable("id") String pictureId,Model model,
			HttpSession session, HttpServletRequest request) {
		jdbcPicture.delete(pictureId);
		@SuppressWarnings("unchecked")
		List<Picture> listPictures = (List<Picture>) session.getAttribute("pictures");
		Room room = (Room) session.getAttribute("room");
		for(Picture p : listPictures) {
			if(p.getPicID().equals(pictureId)) {
				listPictures.remove(p);
				break;
			}
		}
		request.getSession().setAttribute("pictures", listPictures);
		return "redirect:/room/current";
	}
}
