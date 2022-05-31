package com.example.demo.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.demo.Picture;
import com.example.demo.PicturePackage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JdbcPictureRepository implements PictureRepository {
	private JdbcTemplate jdbc;
	private FileSystemRepository filesys = new FileSystemRepository();
	@Autowired
	public JdbcPictureRepository( JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	@Override
	public Picture save(PicturePackage pic, String roomID) throws IOException {
		String uuid = UUID.randomUUID().toString();
		String fileName = StringUtils.cleanPath(chuanHoa(pic.getMultipartFile().getOriginalFilename()));
		fileName = uuid + fileName;
		FileSystemRepository.saveFile("src/main/resources/static/user-photos", fileName, pic.getMultipartFile());
		Picture picture = null;
		try {
			jdbc.update("insert into tblPicture(picID, name, content, description, roomID) values (?, ?, ?, ?, ?)",
					uuid, fileName, pic.getMultipartFile().getBytes(),
					 pic.getDescription(), roomID);
			picture = new Picture(uuid, fileName, pic.getDescription());
			log.info(picture.getPath());
		}catch(Exception e) {
			log.info(e.toString());
		}
		return picture;
	}

	@Override
	public int delete(String pictureId) {
		return jdbc.update("delete from tblPicture where picID = ?", pictureId);
	}
	@Override
	public List<Picture> getPictureOfRoom(String roomID) {
		List<Picture> listPicture = new ArrayList<>();
		try {
			listPicture.addAll(jdbc.query("select * from tblPicture where roomID = ?",
					(rs, rowNum) -> {
						try {
							return mapRowToPicture(rs, rowNum);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}, roomID));
		}catch(Exception e) {
			log.info(e.toString());
		}
		return listPicture;
	}
	private Picture mapRowToPicture(ResultSet rs, int i) throws Exception {
		return new Picture(rs.getString("picID"), 
				rs.getString("name"),
				rs.getString("description"));
	}
	public static String chuanHoa(String s) {
		String ss[] = s.split("\\s+");
		String sss = "";
		for(String i : ss) {
			sss += i;
		}
		return sss;
	}
	private String encodeImage(byte[] bytes ) throws UnsupportedEncodingException {
		byte[] encodeBase64 = Base64.getEncoder().encode(bytes);
        String base64Encoded = new String(encodeBase64, "UTF-8");
        return base64Encoded;
	}
}
