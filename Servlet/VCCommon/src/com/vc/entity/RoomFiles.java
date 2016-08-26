package com.vc.entity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "T_ROOM_FILES")
public class RoomFiles extends BaseEntity {
	private String room;
	private ArrayList<String> files;
	
//	@OneToOne(mappedBy = "Room")
//	@ManyToOne
//	@JoinColumn(name = "room_id")
	@Column(name = "room_id")
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	@Column(name = "files")
	public ArrayList<String> getFiles() {
		return files;
	}
	public void setFiles(ArrayList<String> files) {
		this.files = files;
	}
}
