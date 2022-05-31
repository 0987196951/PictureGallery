package com.example.demo.data;

import java.io.IOException;
import java.util.List;

import com.example.demo.Picture;
import com.example.demo.PicturePackage;

public interface PictureRepository {
	public Picture save(PicturePackage pic, String roomID) throws IOException;
	public int delete(String pictureId);
	public Iterable<Picture> getPictureOfRoom(String userID);
}
