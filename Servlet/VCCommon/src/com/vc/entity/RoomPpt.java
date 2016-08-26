package com.vc.entity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_ROOM_PPT")
public class RoomPpt extends BaseEntity {
	private String room;
	private ArrayList<String> ppts;
	
	@Column(name = "room_id")
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	@Column(name = "ppts")
	public ArrayList<String> getPpts() {
		return ppts;
	}
	public void setPpts(ArrayList<String> ppts) {
		this.ppts = ppts;
	}
}
