package com.example.demo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Repository;

import com.example.demo.User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Repository
public class JdbcUserRepository implements UserRepository {
	private final JdbcTemplate jdbc;
	@Autowired
	public JdbcUserRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	@Override
	public User isRightUser(User user) {
		User gettedUser = null;
		try {
			gettedUser = jdbc.queryForObject("select username, password, userID from tblUser where password = ? and  username = ?",
					this::mapRowToUser, user.getPassword(), user.getUsername());			
		}catch (EmptyResultDataAccessException e) {
			log.info(e.getMessage());
		}
		return gettedUser;
	}
	@Override
	public User save(User user) {
		UUID uuid = UUID.randomUUID();
		user.setUserID(uuid.toString());
		PreparedStatementCreatorFactory pscf = 
				new PreparedStatementCreatorFactory("insert into tblUser (userID, username, password) values (?, ?, ?)",
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);
		pscf.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
				Arrays.asList(user.getUserID(), user.getUsername(), user.getPassword()));
		jdbc.update(psc);
		return user;
	}
	private User mapRowToUser(ResultSet rs, int i) throws SQLException {
		return new User (
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("userID"));
	}
}
