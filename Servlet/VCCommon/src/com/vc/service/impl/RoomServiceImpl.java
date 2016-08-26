package com.vc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vc.dao.BaseDAO;
import com.vc.entity.Page;
import com.vc.entity.PageInfo;
import com.vc.entity.Room;
import com.vc.entity.RoomFiles;
import com.vc.entity.RoomPpt;
import com.vc.service.RoomService;
import com.vc.utils.TextUtil;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

	private static final int PER_PAGE = 20;
	private BaseDAO<Room> roomDao;
	private BaseDAO<RoomFiles> filesDao;
	private BaseDAO<RoomPpt> pptDao;

	public BaseDAO<Room> getRoomDao() {
		return roomDao;
	}

	@Autowired
	public void setRoomDao(BaseDAO<Room> roomDao) {
		this.roomDao = roomDao;
	}

	public BaseDAO<RoomFiles> getFilesDao() {
		return filesDao;
	}
	
	@Autowired
	public void setFilesDao(BaseDAO<RoomFiles> filesDao) {
		this.filesDao = filesDao;
	}

	public BaseDAO<RoomPpt> getPptDao() {
		return pptDao;
	}
	
	@Autowired
	public void setPptDao(BaseDAO<RoomPpt> pptDao) {
		this.pptDao = pptDao;
	}

	@Transactional
	@Override
	public Room createRoom(Room room) {
		getRoomDao().save(room);
		return room;
	}

	/**
	 * 
	 * @param page
	 *            查询第page页
	 * @param limit
	 *            每页limit条数据
	 * @param where sample:"as room where room.id=roomId", or null
	 * @return
	 */
	@Transactional
	@Override
	public Page<Room> getRoomPage(int page, int limit, String where, Object[] params) {
		PageInfo pageInfo = new PageInfo();
		String countHql = "select count(*) from Room";
		if(!TextUtil.isEmpty(where)) {
			countHql += " " + where;
		}
		long totalDataNum = getRoomDao().count(countHql, params);
		
		int perPageDataNum = limit == -1 ? PER_PAGE : limit;

		String findHql = "from Room";
		if(!TextUtil.isEmpty(where)) {
			findHql += " " + where;
		}
		findHql+=" order by updateDate desc";
		List<Room> roomList = getRoomDao().find(findHql, params, page,
				perPageDataNum);
		// 到此页为止(包括此页)一共的数据数量
		// 前一页的数量+这一页查询到的数据量
		long totalDataNumCurPage = (page * perPageDataNum - perPageDataNum)
				+ roomList.size();

		boolean hasNext = totalDataNumCurPage < totalDataNum;

		pageInfo.setCurPage(page);
		pageInfo.setHasNext(hasNext);
		pageInfo.setPerPageDataNum(perPageDataNum);
		pageInfo.setTotalDataNum(totalDataNum);
		pageInfo.setTotalDataNumCurPage(totalDataNumCurPage);
		pageInfo.setTotalPageNum((int) Math.ceil((double) totalDataNum
				/ (double) perPageDataNum));
		
		Page<Room> roomPage = new Page<Room>();
		roomPage.setData(roomList);
		roomPage.setPageInfo(pageInfo);

		return roomPage;
	}

	@Transactional
	@Override
	public void deleteRoom(String username, String roomId) {
		getRoomDao().delete("from Room room where id="+roomId+" and owner_name="+username);
	}

	@Transactional
	@Override
	public Room updateRoom(Room room) {
		Room roomOrigin = getRoomDao().get("from Room room where room.id = ?", new String[] { room.getId() });
		roomOrigin.update(room);
		getRoomDao().update(roomOrigin);
		return roomOrigin;
	}

	@Transactional
	@Override
	public void updateRoomFiles(RoomFiles roomFiles) {
		if(!TextUtil.isEmpty(roomFiles.getId())) {
			getFilesDao().update(roomFiles);
		} else {
			getFilesDao().save(roomFiles);
		}
	}

	@Transactional
	@Override
	public RoomFiles getFiles(String roomId) {
		return getFilesDao().get("from RoomFiles files where files.room = ?", new String[]{roomId});
	}

	@Transactional
	@Override
	public void updateRoomPpt(RoomPpt roomPpt) {
		if(!TextUtil.isEmpty(roomPpt.getId())) {
			getPptDao().update(roomPpt);
		} else {
			getPptDao().save(roomPpt);
		}
	}

	@Transactional
	@Override
	public RoomPpt getPpts(String roomId) {
		return getPptDao().get("from RoomPpt ppt where ppt.room = ?", new String[]{roomId});
	}
}
