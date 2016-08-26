package com.vc.service;

import com.vc.entity.Page;
import com.vc.entity.Room;
import com.vc.entity.RoomFiles;
import com.vc.entity.RoomPpt;

public interface RoomService {
	
	public Room createRoom(Room room);
	
	public Room updateRoom(Room room);
	
	public Page<Room> getRoomPage(int page, int limit, String where, Object[] params);
	
	public void deleteRoom(String username, String roomId);
	
	public void updateRoomFiles(RoomFiles roomFiles);
	
	public RoomFiles getFiles(String roomId);
	
	public void updateRoomPpt(RoomPpt roomPpt);
	
	public RoomPpt getPpts(String roomId);
}
