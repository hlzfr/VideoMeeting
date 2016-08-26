package com.vc.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.vc.entity.account.User;
import com.vc.utils.TextUtil;

@Entity
@Table(name = "T_ROOM")
public class Room  extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "房间不能为空")
	private String roomName;
	
	private String ownerName; //房间管理者User的user name
	
	private String describe;
	
	private long startTime = System.currentTimeMillis();
	
	private long endTime = System.currentTimeMillis();
	
	private User owner;
	
	private List<User> participator;

	@Column(name = "roomname", length = 20)
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	@Column(name = "owner_name", length = 20)
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Column(name = "start_time")
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Column(name = "room_describe", length = 255)
	public String getDescribe() {
		return describe;
	}
	
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	@ManyToMany(targetEntity=User.class)
//	@JoinTable(name = "t_user_room", joinColumns = { @JoinColumn(name = "room_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	public List<User> getParticipator() {
		return participator;
	}

	public void setParticipator(List<User> participator) {
		this.participator = participator;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getOwner() {
		return this.owner;
	}
	
	public void update(Room room) {
		this.describe = room.getDescribe();
		this.endTime = room.getEndTime();
		this.startTime = room.getStartTime();
		this.roomName = room.getRoomName();
		if(!TextUtil.isEmpty(room.getOwnerName())){
			this.ownerName = room.getOwnerName();			
		}
		if (room.getOwner() != null && !TextUtil.isEmpty(room.getOwner().getId())) {
			this.owner = room.getOwner();
		}
		this.participator = room.getParticipator();
	}
}
