package com.example.demo.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Picture;
import com.example.demo.Room;
@Repository
public class JdbcRoomRepository implements RoomRepository{
	private JdbcTemplate jdbc;
	@Autowired
	public JdbcRoomRepository (JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	@Override
	public Room findOne(String roomID) {
		Room roomGetted = null;
		try {
			roomGetted  = jdbc.queryForObject("select * from tblRoom where roomID = ?",
					this::mapRowToRoom, roomID);
		}catch(EmptyResultDataAccessException e) {
			
		}
		return roomGetted;
	}

	@Override
	public List<Room> findAllwithUser(String userID) {
		List<Room> listRoom = new ArrayList<>();
		
		listRoom.addAll(jdbc.query("select r.roomID, r.name, r.password, r.createdDay from tblRoom r, tblUser u, tblUser_tblRoom ur "
				+ " where u.userID = ? and ur.userID = u.userID and ur.roomID = r.roomID",
				this::mapRowToRoom, userID));
		listRoom.sort(null);
		return listRoom;
	}
	public Room mapRowToRoom(ResultSet rs, int i) throws SQLException {
		return new Room(rs.getString("roomID"), rs.getString("name"),rs.getString("password"),rs.getDate("createdDay"));
	}
	@Override
	public Room addRoom(Room room, String userID) {
		UUID uuid = UUID.randomUUID();
		room.setRoomID(uuid.toString());
		room.setCreatedDay(new Date());
		try {
			Room checkRoom = jdbc.queryForObject("select * from tblRoom where name = ?",
					this::mapRowToRoom, room.getName());
		}catch(EmptyResultDataAccessException e) {
			
		}
		finally {
			save(room, userID);
		}
		return room;
	}
	private void save(Room room, String userID) {
		jdbc.update("insert into tblroom(roomID, name, password, createdDay) values (?, ?, ?, ?)",
				room.getRoomID(), room.getName(), room.getPassword(), room.getCreatedDay());
		jdbc.update("insert into tbluser_tblroom(userID, roomID) values(?, ?)", userID, room.getRoomID());
	}
	@Override
	public Room isRoom(Room room) {
		Room gettedRoom = null;
		try {
			gettedRoom = jdbc.queryForObject("select * from tblRoom where roomID = ? and password = ?",
					this::mapRowToRoom, room.getRoomID(), room.getPassword());
		}catch(EmptyResultDataAccessException e) {
			
		}
		return gettedRoom;
	}
	@Override
	public List<Room> findByName(String name) {
		List<Room> listRoom = new ArrayList<>();
		listRoom.addAll(jdbc.query("select r.roomID, r.name, r.password, r.createdDay from tblRoom r "
				+ " where r.name = ?",
				this::mapRowToRoom, name));
		listRoom.sort(null);
		return listRoom;
	}
}
