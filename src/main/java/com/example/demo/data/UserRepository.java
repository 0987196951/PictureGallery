package com.example.demo.data;

import com.example.demo.User;

public interface UserRepository {
	public User isRightUser(User user);
	public User save(User user);
}
