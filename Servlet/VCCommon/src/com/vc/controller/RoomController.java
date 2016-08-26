package com.vc.controller;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vc.entity.Page;
import com.vc.entity.Result;
import com.vc.entity.Room;
import com.vc.entity.RoomFiles;
import com.vc.entity.RoomPpt;
import com.vc.service.RoomService;
import com.vc.utils.TextUtil;

@Controller
@RequestMapping(value = "/room")
public class RoomController {
	@Autowired
	RoomService roomService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Result<Room> createRoom(@RequestBody Room room) {
//		Subject subject = SecurityUtils.getSubject();
//		String username = (String)subject.getPrincipal();
		Room roomCreated = null;
		if(!TextUtil.isEmpty(room.getId())){
			roomCreated = roomService.updateRoom(room);
		} else {
			roomCreated = roomService.createRoom(room);
		}
		if (roomCreated == null) {
			return new Result<Room>(false, "create failed", null);
		}
		return new Result<Room>(true, null, roomCreated);
		
	}

	@RequestMapping(value = "/page/{page}/{limit}", method = RequestMethod.GET)
	@ResponseBody
	public Result<Page<Room>> getPage(@PathVariable int page, @PathVariable int limit, @QueryParam(value = "ownerName") String ownerName) {
		Page<Room> body = roomService.getRoomPage(page, limit, TextUtil.isEmpty(ownerName)?null:"as room where room.ownerName=?", TextUtil.isEmpty(ownerName)?null:new String[]{ownerName});
		if (body==null) {
			return new Result<Page<Room>>(false, "find nothing", null);
		}
		return new Result<Page<Room>>(true, null, body);
	}
	
	@RequestMapping(value = "/files/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> updateRoomFiles(@RequestBody RoomFiles roomFiles) {
		if(TextUtil.isEmpty(roomFiles.getRoom())) {
			return new Result<Void>(false, "room is empty", null);
		}
		roomService.updateRoomFiles(roomFiles);
		return new Result<Void>(true, null, null);
	}
	
	@RequestMapping(value = "/files/getFiles", method = RequestMethod.POST)
	@ResponseBody
	public Result<RoomFiles> getRoomFiles(@RequestBody Room room) {
		if(room == null || TextUtil.isEmpty(room.getId())) {
			return new Result<RoomFiles>(false, "room is empty", null);
		}
		RoomFiles roomFiles = roomService.getFiles(room.getId());
		if(roomFiles != null) {
			return new Result<RoomFiles>(true, null, roomFiles);
		} else {
			roomFiles = new RoomFiles();
			roomFiles.setRoom(room.getId());
			return new Result<RoomFiles>(true, "房间文件列表为空", roomFiles);
		}
	}
	
	@RequestMapping(value = "/ppt/update", method = RequestMethod.POST)
	@ResponseBody
	public Result<Void> updateRoomPpt(@RequestBody RoomPpt roomPpt) {
		if(TextUtil.isEmpty(roomPpt.getRoom())) {
			return new Result<Void>(false, "room is empty", null);
		}
		roomService.updateRoomPpt(roomPpt);
		return new Result<Void>(true, null, null);
	}
	
	@RequestMapping(value = "/ppt/getPpts", method = RequestMethod.POST)
	@ResponseBody
	public Result<RoomPpt> getRoomPpts(@RequestBody Room room) {
		if(room == null || TextUtil.isEmpty(room.getId())) {
			return new Result<RoomPpt>(false, "room is empty", null);
		}
		RoomPpt roomPpts = roomService.getPpts(room.getId());
		if(roomPpts != null) {
			return new Result<RoomPpt>(true, null, roomPpts);
		} else {
			roomPpts = new RoomPpt();
			roomPpts.setRoom(room.getId());
			return new Result<RoomPpt>(true, "房间文件列表为空", roomPpts);
		}
	}
}
