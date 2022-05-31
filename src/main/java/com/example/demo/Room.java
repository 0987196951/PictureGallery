package com.example.demo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room implements Comparable<Room> {
	private String roomID;
	private String name;
	private String password;
	private Date createdDay;
	@Override
	public int compareTo(Room o) {
		return this.getName().compareTo(o.getName());
	}
	@Override
	public boolean equals(Object o) {
		if ( o == this ) return true;
		if (! (o instanceof Room )) {
			return false;
		}
		Room other = (Room) o;
		if(other.name.equals(other.getName())) return true;
		return false;
	}
}
