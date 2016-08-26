package com.vc.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class BaseEntity {

	protected String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.ALWAYS)
	protected Date updateDate;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.INSERT)
	protected Date generateDate;

	public BaseEntity() {
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "updateDate", nullable = false, insertable = true, updatable = true)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column(name = "generateDate", nullable = false, insertable = true, updatable = false)
	public Date getGenerateDate() {
		return generateDate;
	}

	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}

	public void preCreate() {
		generateDate = updateDate = new Date(System.currentTimeMillis());
	}

	public void preUpdate() {
		updateDate = new Date(System.currentTimeMillis());
	}

	public void postCreate() {
		System.out.println("===================================== postCreate =====================================");
		System.out.println("generateDate = "+(generateDate==null?"null":generateDate.toLocaleString()));
		System.out.println("updateDate = "+(updateDate==null?"null":updateDate.toLocaleString()));
	}

	public void postUpdate() {
		System.out.println("===================================== postUpdate =====================================");
		System.out.println("generateDate = "+(generateDate==null?"null":generateDate.toLocaleString()));
		System.out.println("updateDate = "+(updateDate==null?"null":updateDate.toLocaleString()));
	}
}
