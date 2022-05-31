package com.example.demo.data;

import java.util.List;

import com.example.demo.Picture;
import com.example.demo.Room;

public interface RoomRepository {
	public Room findOne(String roomID);
	public List<Room> findAllwithUser(String userID);
	public Room addRoom(Room room, String userID);
	public Room isRoom(Room room);
	public List<Room> findByName(String name);
}