package com.example.demo;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
	private String picID;
	private String name;
	private String description;
	private String path;
	public Picture(String picID, String name, String description) {
		this.picID = picID;
		this.name = name;
		this.description = description;
		this.path = setPhotosImagePath();
	}
	public String setPhotosImagePath() {
		if(picID == null || name == null) return null;
        return "/user-photos/" + name;	
    }
}
