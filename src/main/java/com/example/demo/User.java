package com.example.demo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private	String userID;
}
